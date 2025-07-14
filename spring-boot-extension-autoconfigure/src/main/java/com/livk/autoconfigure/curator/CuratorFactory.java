/*
 * Copyright 2021-2024 spring-boot-extension the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.livk.autoconfigure.curator;

import com.livk.context.curator.CuratorException;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.drivers.TracerDriver;
import org.apache.curator.ensemble.EnsembleProvider;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * The type Curator factory.
 *
 * @author livk
 */
@Slf4j
abstract class CuratorFactory {

	/**
	 * Create Curator framework
	 * @param properties the properties
	 * @param retryPolicy the retry policy
	 * @param optionalCuratorFrameworkCustomizerProvider the optional curator framework
	 * customizer provider
	 * @param optionalEnsembleProvider the optional ensemble provider
	 * @param optionalTracerDriverProvider the optional tracer driver provider
	 * @return the curator framework
	 */
	public static CuratorFramework create(CuratorProperties properties, RetryPolicy retryPolicy,
			Supplier<Stream<CuratorFrameworkBuilderCustomizer>> optionalCuratorFrameworkCustomizerProvider,
			Supplier<EnsembleProvider> optionalEnsembleProvider, Supplier<TracerDriver> optionalTracerDriverProvider) {
		CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder();

		EnsembleProvider ensembleProvider = optionalEnsembleProvider.get();
		if (ensembleProvider != null) {
			builder.ensembleProvider(ensembleProvider);
		}
		else {
			builder.connectString(properties.getConnectString());
		}
		builder.sessionTimeoutMs((int) properties.getSessionTimeout().toMillis())
			.connectionTimeoutMs((int) properties.getConnectionTimeout().toMillis())
			.retryPolicy(retryPolicy);

		Stream<CuratorFrameworkBuilderCustomizer> customizers = optionalCuratorFrameworkCustomizerProvider.get();
		if (customizers != null) {
			customizers.forEach(curatorFrameworkCustomizer -> curatorFrameworkCustomizer.customize(builder));
		}

		CuratorFramework framework = builder.build();
		TracerDriver tracerDriver = optionalTracerDriverProvider.get();
		if (tracerDriver != null && framework.getZookeeperClient() != null) {
			framework.getZookeeperClient().setTracerDriver(tracerDriver);
		}

		if (log.isTraceEnabled()) {
			log.trace("blocking until connected to zookeeper for {}{}", properties.getBlockUntilConnectedWait(),
					properties.getBlockUntilConnectedUnit());
		}
		try {
			framework.blockUntilConnected(properties.getBlockUntilConnectedWait(),
					properties.getBlockUntilConnectedUnit());
		}
		catch (InterruptedException e) {
			log.warn("interrupted", e);
			Thread.currentThread().interrupt();
			throw new CuratorException(e);
		}
		if (log.isTraceEnabled()) {
			log.trace("connected to zookeeper");
		}
		return framework;
	}

	/**
	 * Retry policy retry policy.
	 * @param properties the properties
	 * @return the retry policy
	 */
	public static RetryPolicy retryPolicy(CuratorProperties properties) {
		return new ExponentialBackoffRetry(properties.getBaseSleepTimeMs(), properties.getMaxRetries(),
				properties.getMaxSleepMs());
	}

}
