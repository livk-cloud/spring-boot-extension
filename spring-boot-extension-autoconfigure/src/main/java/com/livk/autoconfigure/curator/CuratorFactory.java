package com.livk.autoconfigure.curator;

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
public abstract class CuratorFactory {

	/**
	 * Curator framework
	 * @param properties the properties
	 * @param retryPolicy the retry policy
	 * @param optionalCuratorFrameworkCustomizerProvider the optional curator framework
	 * customizer provider
	 * @param optionalEnsembleProvider the optional ensemble provider
	 * @param optionalTracerDriverProvider the optional tracer driver provider
	 * @return the curator framework
	 * @throws InterruptedException the exception
	 */
	public static CuratorFramework curatorFramework(CuratorProperties properties, RetryPolicy retryPolicy,
			Supplier<Stream<CuratorFrameworkBuilderCustomizer>> optionalCuratorFrameworkCustomizerProvider,
			Supplier<EnsembleProvider> optionalEnsembleProvider, Supplier<TracerDriver> optionalTracerDriverProvider)
			throws InterruptedException {
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

		CuratorFramework curator = builder.build();
		TracerDriver tracerDriver = optionalTracerDriverProvider.get();
		if (tracerDriver != null && curator.getZookeeperClient() != null) {
			curator.getZookeeperClient().setTracerDriver(tracerDriver);
		}

		curator.start();
		if (log.isTraceEnabled()) {
			log.trace("blocking until connected to zookeeper for " + properties.getBlockUntilConnectedWait()
					+ properties.getBlockUntilConnectedUnit());
		}
		curator.blockUntilConnected(properties.getBlockUntilConnectedWait(), properties.getBlockUntilConnectedUnit());
		if (log.isTraceEnabled()) {
			log.trace("connected to zookeeper");
		}
		return curator;
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
