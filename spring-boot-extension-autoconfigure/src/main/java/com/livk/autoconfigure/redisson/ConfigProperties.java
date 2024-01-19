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

package com.livk.autoconfigure.redisson;

import com.livk.commons.util.BeanUtils;
import io.netty.channel.EventLoopGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.redisson.api.NameMapper;
import org.redisson.api.NatMapper;
import org.redisson.client.DefaultCredentialsResolver;
import org.redisson.client.NettyHook;
import org.redisson.client.codec.Codec;
import org.redisson.config.*;
import org.redisson.connection.AddressResolverGroupFactory;
import org.redisson.connection.ConnectionListener;
import org.redisson.connection.balancer.LoadBalancer;
import org.redisson.connection.balancer.RoundRobinLoadBalancer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.core.GenericTypeResolver;
import org.springframework.util.Assert;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

/**
 * The type Config properties.
 *
 * @author livk
 */
@Data
@ConfigurationProperties(ConfigProperties.PREFIX)
public class ConfigProperties {

	/**
	 * The constant PREFIX.
	 */
	public static final String PREFIX = "spring.redisson";

	private RedissonConfig config;

	/**
	 * The type Redisson config.
	 */
	@EqualsAndHashCode(callSuper = true)
	@Data
	public static class RedissonConfig extends Config {

		/**
		 * Instantiates a new Redisson config.
		 * @param clusterServersConfig the cluster servers config
		 * @param replicatedServersConfig the replicated servers config
		 * @param singleServerConfig the single server config
		 * @param sentinelServersConfig the sentinel connection config
		 * @param masterSlaveServersConfig the master slave connection config
		 * @param nettyHook the netty hook
		 * @param codec the codec
		 * @param redissonReferenceEnabled the redisson reference enabled
		 * @param threads the threads
		 * @param transportMode the transport mode
		 * @param nettyThreads the netty threads
		 * @param executor the executor
		 * @param eventLoopGroup the event loop group
		 * @param lockWatchdogTimeout the lock watchdog timeout
		 * @param checkLockSyncedSlaves the check lock synced slaves
		 * @param keepPubSubOrder the keep pub sub order
		 * @param addressResolverGroupFactory the address resolver group factory
		 * @param useScriptCache the use script cache
		 * @param minCleanUpDelay the min clean up delay
		 * @param maxCleanUpDelay the max clean up delay
		 * @param cleanUpKeysAmount the clean up keys amount
		 * @param useThreadClassLoader the use thread class loader
		 * @param reliableTopicWatchdogTimeout the reliable topic watchdog timeout
		 * @param connectionListener the connection listener
		 * @param lazyInitialization the lazy initialization
		 * @param slavesSyncTimeout the slaves sync timeout
		 */
		@ConstructorBinding
		public RedissonConfig(ClusterServers clusterServersConfig, ReplicatedServers replicatedServersConfig,
				SingleServer singleServerConfig, SentinelServers sentinelServersConfig,
				MasterSlaveServers masterSlaveServersConfig,
				@DefaultValue("!<org.redisson.client.DefaultNettyHook> {}") NettyHook nettyHook, Codec codec,
				@DefaultValue("true") Boolean redissonReferenceEnabled, @DefaultValue("16") Integer threads,
				@DefaultValue("NIO") TransportMode transportMode, @DefaultValue("32") Integer nettyThreads,
				ExecutorService executor, EventLoopGroup eventLoopGroup,
				@DefaultValue("30000") Long lockWatchdogTimeout, @DefaultValue("true") Boolean checkLockSyncedSlaves,
				@DefaultValue("true") Boolean keepPubSubOrder,
				@DefaultValue("!<org.redisson.connection.SequentialDnsAddressResolverFactory> {}") AddressResolverGroupFactory addressResolverGroupFactory,
				@DefaultValue("false") Boolean useScriptCache, @DefaultValue("5") Integer minCleanUpDelay,
				@DefaultValue("1800") Integer maxCleanUpDelay, @DefaultValue("100") Integer cleanUpKeysAmount,
				@DefaultValue("true") Boolean useThreadClassLoader,
				@DefaultValue("600000") Long reliableTopicWatchdogTimeout, ConnectionListener connectionListener,
				Boolean lazyInitialization, @DefaultValue("1000") Long slavesSyncTimeout) {
			setConfig(clusterServersConfig, super::setClusterServersConfig);
			setConfig(replicatedServersConfig, super::setReplicatedServersConfig);
			setConfig(singleServerConfig, super::setSingleServerConfig);
			setConfig(sentinelServersConfig, super::setSentinelServersConfig);
			setConfig(masterSlaveServersConfig, super::setMasterSlaveServersConfig);
			set(nettyHook, super::setNettyHook);
			set(codec, super::setCodec);
			set(redissonReferenceEnabled, super::setReferenceEnabled);
			set(threads, super::setThreads);
			set(transportMode, super::setTransportMode);
			set(nettyThreads, super::setNettyThreads);
			set(executor, super::setExecutor);
			set(eventLoopGroup, super::setEventLoopGroup);
			set(lockWatchdogTimeout, super::setLockWatchdogTimeout);
			set(checkLockSyncedSlaves, super::setCheckLockSyncedSlaves);
			set(keepPubSubOrder, super::setKeepPubSubOrder);
			set(addressResolverGroupFactory, super::setAddressResolverGroupFactory);
			set(useScriptCache, super::setUseScriptCache);
			set(minCleanUpDelay, super::setMinCleanUpDelay);
			set(maxCleanUpDelay, super::setMaxCleanUpDelay);
			set(cleanUpKeysAmount, super::setCleanUpKeysAmount);
			set(useThreadClassLoader, super::setUseThreadClassLoader);
			set(reliableTopicWatchdogTimeout, super::setReliableTopicWatchdogTimeout);
			set(connectionListener, super::setConnectionListener);
			set(lazyInitialization, super::setLazyInitialization);
			set(slavesSyncTimeout, super::setSlavesSyncTimeout);
		}

		private <T extends Base<R>, R extends BaseConfig<R>> void setConfig(T t, Consumer<R> consumer) {
			if (t != null) {
				consumer.accept(t.convert());
			}
		}

		private <T> void set(T t, Consumer<T> consumer) {
			if (t != null) {
				consumer.accept(t);
			}
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

		private boolean checkSlotsCoverage = true;

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
	public static abstract class BaseMasterSlaveServers<T extends BaseMasterSlaveServersConfig<T>> extends Base<T> {

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

		private int failedSlaveCheckInterval = 180000;

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

	}

	/**
	 * The type Base.
	 *
	 * @param <T> the type parameter
	 */
	@Data
	public static abstract class Base<T extends BaseConfig<T>> {

		/**
		 * If pooled connection not used for a <code>timeout</code> time and current
		 * connections amount bigger than minimum idle connections pool size, then it will
		 * closed and removed from pool. Value in milliseconds.
		 */

		private int idleConnectionTimeout = 10000;

		/**
		 * Timeout during connecting to any Redis server. Value in milliseconds.
		 */

		private int connectTimeout = 10000;

		/**
		 * Redis server response timeout. Starts to countdown when Redis command was
		 * succesfully sent. Value in milliseconds.
		 */

		private int timeout = 3000;

		private int subscriptionTimeout = 7500;

		private int retryAttempts = 3;

		private int retryInterval = 1500;

		/**
		 * Password for Redis authentication. Should be null if not needed
		 */

		private String password;

		private String username;

		private CredentialsResolver credentialsResolver = new DefaultCredentialsResolver();

		/**
		 * Subscriptions per Redis connection limit
		 */

		private int subscriptionsPerConnection = 5;

		/**
		 * Name of client connection
		 */

		private String clientName;

		private boolean sslEnableEndpointIdentification = true;

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
