/*
 * Copyright 2021-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.livk.autoconfigure.curator;

import com.livk.auto.service.annotation.SpringAutoService;
import com.livk.context.curator.CuratorException;
import com.livk.context.curator.CuratorTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.drivers.TracerDriver;
import org.apache.curator.ensemble.EnsembleProvider;
import org.apache.curator.ensemble.fixed.FixedEnsembleProvider;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.DefaultTracerDriver;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author livk
 */
@Slf4j
@SpringAutoService
@AutoConfiguration
@ConditionalOnClass(CuratorFramework.class)
@EnableConfigurationProperties(CuratorProperties.class)
@ConditionalOnProperty(value = "spring.zookeeper.curator.enabled", matchIfMissing = true)
public class CuratorAutoConfiguration {

	/**
	 * Curator framework.
	 * @param properties the properties
	 * @param retryPolicy the retry policy
	 * @param curatorFrameworkBuilderCustomizers the curator framework builder customizers
	 * @param ensembleProviders the ensemble providers
	 * @param tracerDrivers the tracer drivers
	 * @return the curator framework
	 */
	@Bean(initMethod = "start", destroyMethod = "close")
	@ConditionalOnMissingBean
	public CuratorFramework curatorFramework(CuratorProperties properties, RetryPolicy retryPolicy,
			ObjectProvider<CuratorFrameworkBuilderCustomizer> curatorFrameworkBuilderCustomizers,
			ObjectProvider<EnsembleProvider> ensembleProviders, ObjectProvider<TracerDriver> tracerDrivers) {
		CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder();

		EnsembleProvider ensembleProvider = ensembleProviders.getIfAvailable();
		if (ensembleProvider != null) {
			builder.ensembleProvider(ensembleProvider);
		}
		else {
			builder.connectString(properties.getConnectString());
		}
		builder.sessionTimeoutMs((int) properties.getSessionTimeout().toMillis())
			.connectionTimeoutMs((int) properties.getConnectionTimeout().toMillis())
			.retryPolicy(retryPolicy);

		curatorFrameworkBuilderCustomizers.orderedStream().forEach(customizer -> customizer.customize(builder));

		CuratorFramework framework = builder.build();
		TracerDriver tracerDriver = tracerDrivers.getIfAvailable();
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
		catch (InterruptedException ex) {
			log.warn("interrupted", ex);
			Thread.currentThread().interrupt();
			throw new CuratorException(ex);
		}
		if (log.isTraceEnabled()) {
			log.trace("connected to zookeeper");
		}
		return framework;
	}

	@Bean
	@ConditionalOnMissingBean
	public TracerDriver tracerDriver() {
		return new DefaultTracerDriver();
	}

	@Bean
	@ConditionalOnMissingBean
	public EnsembleProvider fixedEnsembleProvider(CuratorProperties properties) {
		return new FixedEnsembleProvider(properties.getConnectString());
	}

	/**
	 * Exponential backoff retry policy.
	 * @param properties the properties
	 * @return the retry policy
	 */
	@Bean
	@ConditionalOnMissingBean
	public RetryPolicy exponentialBackoffRetry(CuratorProperties properties) {
		return new ExponentialBackoffRetry(properties.getBaseSleepTimeMs(), properties.getMaxRetries(),
				properties.getMaxSleepMs());
	}

	/**
	 * Curator template curator template.
	 * @param framework the curator framework
	 * @return the curator template
	 */
	@Bean
	@ConditionalOnMissingBean
	public CuratorTemplate curatorTemplate(CuratorFramework framework) {
		return new CuratorTemplate(framework);
	}

}
