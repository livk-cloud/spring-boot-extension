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

package com.livk.autoconfigure.redisson;

import com.livk.commons.util.BeanUtils;
import io.netty.channel.EventLoopGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.redisson.api.NameMapper;
import org.redisson.api.NatMapper;
import org.redisson.client.DefaultCredentialsResolver;
import org.redisson.client.FailedConnectionDetector;
import org.redisson.client.FailedNodeDetector;
import org.redisson.client.NettyHook;
import org.redisson.client.codec.Codec;
import org.redisson.config.BaseConfig;
import org.redisson.config.BaseMasterSlaveServersConfig;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.CommandMapper;
import org.redisson.config.Config;
import org.redisson.config.CredentialsResolver;
import org.redisson.config.DelayStrategy;
import org.redisson.config.EqualJitterDelay;
import org.redisson.config.MasterSlaveServersConfig;
import org.redisson.config.Protocol;
import org.redisson.config.ReadMode;
import org.redisson.config.ReplicatedServersConfig;
import org.redisson.config.SentinelServersConfig;
import org.redisson.config.ShardedSubscriptionMode;
import org.redisson.config.SingleServerConfig;
import org.redisson.config.SslProvider;
import org.redisson.config.SslVerificationMode;
import org.redisson.config.SubscriptionMode;
import org.redisson.config.TransportMode;
import org.redisson.config.ValkeyCapability;
import org.redisson.connection.AddressResolverGroupFactory;
import org.redisson.connection.ConnectionListener;
import org.redisson.connection.balancer.LoadBalancer;
import org.redisson.connection.balancer.RoundRobinLoadBalancer;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.boot.context.properties.bind.PlaceholdersResolver;
import org.springframework.boot.context.properties.bind.PropertySourcesPlaceholdersResolver;
import org.springframework.boot.context.properties.source.ConfigurationPropertySource;
import org.springframework.boot.context.properties.source.ConfigurationPropertySources;
import org.springframework.core.GenericTypeResolver;
import org.springframework.core.convert.support.ConfigurableConversionService;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.util.Assert;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

/**
 * The type Config properties.
 *
 * @author livk
 */
@Data
@ConfigurationProperties(RedissonProperties.PREFIX)
public class RedissonProperties {

	/**
	 * The constant PREFIX.
	 */
	public static final String PREFIX = "spring.redisson";

	private RedissonConfig config;

	public static RedissonProperties load(ConfigurableEnvironment environment) {
		Iterable<ConfigurationPropertySource> sources = ConfigurationPropertySources.get(environment);
		ConfigurableConversionService conversionService = environment.getConversionService();
		PlaceholdersResolver resolver = new PropertySourcesPlaceholdersResolver(environment);
		Consumer<PropertyEditorRegistry> consumer = registry -> new RedissonPropertyEditorRegistrar()
			.registerCustomEditors(registry);
		Binder binder = new Binder(sources, resolver, conversionService, consumer);
		return binder.bind(RedissonProperties.PREFIX, RedissonProperties.class).orElse(new RedissonProperties());
	}

	/**
	 * The type Redisson config.
	 */
	@EqualsAndHashCode(callSuper = true)
	@Data
	@NoArgsConstructor
	public static class RedissonConfig extends Config {

		/**
		 * Instantiates a new Redisson config.
		 */
		@ConstructorBinding
		public RedissonConfig(SentinelServers sentinelServersConfig, MasterSlaveServers masterSlaveServersConfig,
				SingleServer singleServerConfig, ClusterServers clusterServersConfig,
				ReplicatedServers replicatedServersConfig, @DefaultValue("16") Integer threads,
				@DefaultValue("32") Integer nettyThreads, Executor nettyExecutor, Codec codec, ExecutorService executor,
				@DefaultValue("true") Boolean referenceEnabled, @DefaultValue("NIO") TransportMode transportMode,
				EventLoopGroup eventLoopGroup, @DefaultValue("30000") Long lockWatchdogTimeout,
				@DefaultValue("100") Integer lockWatchdogBatchSize, @DefaultValue("300000") Integer fairLockWaitTimeout,
				@DefaultValue("true") Boolean checkLockSyncedSlaves, @DefaultValue("1000") Long slavesSyncTimeout,
				@DefaultValue("600000") Long reliableTopicWatchdogTimeout,
				@DefaultValue("true") Boolean keepPubSubOrder, @DefaultValue("true") Boolean useScriptCache,
				@DefaultValue("5") Integer minCleanUpDelay, @DefaultValue("1800") Integer maxCleanUpDelay,
				@DefaultValue("100") Integer cleanUpKeysAmount,
				@DefaultValue("!<org.redisson.client.DefaultNettyHook> {}") NettyHook nettyHook,
				ConnectionListener connectionListener, @DefaultValue("true") Boolean useThreadClassLoader,
				@DefaultValue("!<org.redisson.connection.SequentialDnsAddressResolverFactory> {}") AddressResolverGroupFactory addressResolverGroupFactory,
				Boolean lazyInitialization, @DefaultValue("RESP2") Protocol protocol,
				Set<ValkeyCapability> valkeyCapabilities) {
			PropertyMapper mapper = PropertyMapper.get().alwaysApplyingWhenNonNull();

			mapper.from(sentinelServersConfig).as(Base::convert).to(this::setSentinelServersConfig);
			mapper.from(masterSlaveServersConfig).as(Base::convert).to(this::setMasterSlaveServersConfig);
			mapper.from(singleServerConfig).as(Base::convert).to(this::setSingleServerConfig);
			mapper.from(clusterServersConfig).as(Base::convert).to(this::setClusterServersConfig);
			mapper.from(replicatedServersConfig).as(Base::convert).to(this::setReplicatedServersConfig);
			setThreads(threads);
			setNettyThreads(nettyThreads);
			mapper.from(nettyExecutor).to(this::setNettyExecutor);
			mapper.from(codec).to(this::setCodec);
			mapper.from(executor).to(this::setExecutor);
			setReferenceEnabled(referenceEnabled);
			setTransportMode(transportMode);
			mapper.from(eventLoopGroup).to(this::setEventLoopGroup);
			setLockWatchdogTimeout(lockWatchdogTimeout);
			setLockWatchdogBatchSize(lockWatchdogBatchSize);
			setFairLockWaitTimeout(fairLockWaitTimeout);
			setCheckLockSyncedSlaves(checkLockSyncedSlaves);
			setSlavesSyncTimeout(slavesSyncTimeout);
			setReliableTopicWatchdogTimeout(reliableTopicWatchdogTimeout);
			setKeepPubSubOrder(keepPubSubOrder);
			setUseScriptCache(useScriptCache);
			setMinCleanUpDelay(minCleanUpDelay);
			setMaxCleanUpDelay(maxCleanUpDelay);
			setCleanUpKeysAmount(cleanUpKeysAmount);
			setNettyHook(nettyHook);
			mapper.from(connectionListener).to(this::setConnectionListener);
			setUseThreadClassLoader(useThreadClassLoader);
			setAddressResolverGroupFactory(addressResolverGroupFactory);
			mapper.from(lazyInitialization).to(this::setLazyInitialization);
			setProtocol(protocol);
			mapper.from(valkeyCapabilities).to(this::setValkeyCapabilities);
		}

	}

	/**
	 * The type Cluster servers.
	 */
	@EqualsAndHashCode(callSuper = true)
	@Data
	public static class ClusterServers extends BaseMasterSlaveServers<ClusterServersConfig> {

		private NatMapper natMapper = NatMapper.direct();

		/**
		 * Redis cluster node urls list
		 */

		private List<String> nodeAddresses = new ArrayList<>();

		/**
		 * Redis cluster scan interval in milliseconds
		 */

		private int scanInterval = 5000;

		private boolean checkSlotsCoverage = true;

		private ShardedSubscriptionMode shardedSubscriptionMode = ShardedSubscriptionMode.AUTO;

	}

	/**
	 * The type Master slave servers.
	 */
	@EqualsAndHashCode(callSuper = true)
	@Data
	public static class MasterSlaveServers extends BaseMasterSlaveServers<MasterSlaveServersConfig> {

		private NatMapper natMapper = NatMapper.direct();

		/**
		 * Redis cluster node urls list
		 */

		private List<String> nodeAddresses = new ArrayList<>();

		/**
		 * Redis cluster scan interval in milliseconds
		 */

		private int scanInterval = 5000;

	}

	/**
	 * The type Replicated servers.
	 */
	@EqualsAndHashCode(callSuper = true)
	@Data
	public static class ReplicatedServers extends BaseMasterSlaveServers<ReplicatedServersConfig> {

		/**
		 * Replication group node urls list
		 */

		private List<String> nodeAddresses = new ArrayList<>();

		/**
		 * Replication group scan interval in milliseconds
		 */

		private int scanInterval = 1000;

		/**
		 * Database index used for Redis connection
		 */

		private int database = 0;

		private boolean monitorIPChanges = false;

	}

	/**
	 * The type Sentinel servers.
	 */
	@EqualsAndHashCode(callSuper = true)
	@Data
	public static class SentinelServers extends BaseMasterSlaveServers<SentinelServersConfig> {

		private List<String> sentinelAddresses = new ArrayList<>();

		private NatMapper natMapper = NatMapper.direct();

		private String masterName;

		private String sentinelUsername;

		private String sentinelPassword;

		/**
		 * Database index used for Redis connection
		 */

		private int database = 0;

		/**
		 * Sentinel scan interval in milliseconds
		 */

		private int scanInterval = 1000;

		private boolean checkSentinelsList = true;

		private boolean checkSlaveStatusWithSyncing = true;

		private boolean sentinelsDiscovery = true;

	}

	/**
	 * The type Single server.
	 */
	@EqualsAndHashCode(callSuper = true)
	@Data
	public static class SingleServer extends Base<SingleServerConfig> {

		/**
		 * Redis server address
		 */

		private String address;

		/**
		 * Minimum idle subscription connection amount
		 */

		private int subscriptionConnectionMinimumIdleSize = 1;

		/**
		 * Redis subscription connection maximum pool size
		 */

		private int subscriptionConnectionPoolSize = 50;

		/**
		 * Minimum idle Redis connection amount
		 */

		private int connectionMinimumIdleSize = 24;

		/**
		 * Redis connection maximum pool size
		 */

		private int connectionPoolSize = 64;

		/**
		 * Database index used for Redis connection
		 */

		private int database = 0;

		/**
		 * Interval in milliseconds to check DNS
		 */

		private long dnsMonitoringInterval = 5000;

	}

	/**
	 * The type Base master slave servers.
	 *
	 * @param <T> the type parameter
	 */
	@EqualsAndHashCode(callSuper = true)
	@Data
	public abstract static class BaseMasterSlaveServers<T extends BaseMasterSlaveServersConfig<T>> extends Base<T> {

		private LoadBalancer loadBalancer = new RoundRobinLoadBalancer();

		/**
		 * Redis 'slave' node minimum idle connection amount for <b>each</b> slave node
		 */

		private int slaveConnectionMinimumIdleSize = 24;

		/**
		 * Redis 'slave' node maximum connection pool size for <b>each</b> slave node
		 */

		private int slaveConnectionPoolSize = 64;

		private int failedSlaveReconnectionInterval = 3000;

		/**
		 * Redis 'master' node minimum idle connection amount for <b>each</b> slave node
		 */

		private int masterConnectionMinimumIdleSize = 24;

		/**
		 * Redis 'master' node maximum connection pool size
		 */

		private int masterConnectionPoolSize = 64;

		private ReadMode readMode = ReadMode.SLAVE;

		private SubscriptionMode subscriptionMode = SubscriptionMode.MASTER;

		/**
		 * Redis 'slave' node minimum idle subscription (pub/sub) connection amount for
		 * <b>each</b> slave node
		 */

		private int subscriptionConnectionMinimumIdleSize = 1;

		/**
		 * Redis 'slave' node maximum subscription (pub/sub) connection pool size for
		 * <b>each</b> slave node
		 */

		private int subscriptionConnectionPoolSize = 50;

		private long dnsMonitoringInterval = 5000;

		private FailedNodeDetector failedSlaveNodeDetector = new FailedConnectionDetector();

	}

	/**
	 * The type Base.
	 *
	 * @param <T> the type parameter
	 */
	@Data
	public abstract static class Base<T extends BaseConfig<T>> {

		/**
		 * If pooled connection not used for a <code>timeout</code> time and current
		 * connections amount bigger than minimum idle connections pool size, then it will
		 * close and removed from pool. Value in milliseconds.
		 */

		private int idleConnectionTimeout = 10000;

		/**
		 * Timeout during connecting to any Redis server. Value in milliseconds.
		 */

		private int connectTimeout = 10000;

		/**
		 * Redis server response timeout. Starts to countdown when Redis command was
		 * successfully sent. Value in milliseconds.
		 */

		private int timeout = 3000;

		private int subscriptionTimeout = 7500;

		private int retryAttempts = 4;

		private DelayStrategy retryDelay = new EqualJitterDelay(Duration.ofMillis(1000), Duration.ofSeconds(2));

		private DelayStrategy reconnectionDelay = new EqualJitterDelay(Duration.ofMillis(100), Duration.ofSeconds(10));

		/**
		 * Password for Redis authentication. Should be null if not needed
		 */

		private String password;

		private String username;

		private CredentialsResolver credentialsResolver = new DefaultCredentialsResolver();

		private int credentialsReapplyInterval = 0;

		/**
		 * Subscriptions per Redis connection limit
		 */

		private int subscriptionsPerConnection = 5;

		/**
		 * Name of client connection
		 */

		private String clientName;

		private SslVerificationMode sslVerificationMode = SslVerificationMode.STRICT;

		private String sslKeystoreType;

		private SslProvider sslProvider = SslProvider.JDK;

		private URL sslTruststore;

		private String sslTruststorePassword;

		private URL sslKeystore;

		private String sslKeystorePassword;

		private String[] sslProtocols;

		private String[] sslCiphers;

		private TrustManagerFactory sslTrustManagerFactory;

		private KeyManagerFactory sslKeyManagerFactory;

		private int pingConnectionInterval = 30000;

		private boolean keepAlive;

		private int tcpKeepAliveCount;

		private int tcpKeepAliveIdle;

		private int tcpKeepAliveInterval;

		private int tcpUserTimeout;

		private boolean tcpNoDelay = true;

		private NameMapper nameMapper = NameMapper.direct();

		private CommandMapper commandMapper = CommandMapper.direct();

		/**
		 * Convert t.
		 * @return the t
		 */
		public T convert() {
			@SuppressWarnings("unchecked")
			Class<T> type = (Class<T>) GenericTypeResolver.resolveTypeArgument(this.getClass(), Base.class);
			Assert.notNull(type, "type must not be null");
			return BeanUtils.copy(this, type);
		}

	}

}
