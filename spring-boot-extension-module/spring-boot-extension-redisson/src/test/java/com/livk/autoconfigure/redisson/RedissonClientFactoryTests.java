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

import com.livk.testcontainers.DockerImageNames;
import com.redis.testcontainers.RedisContainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.data.redis.autoconfigure.DataRedisProperties;
import org.springframework.boot.testcontainers.properties.TestcontainersPropertySourceAutoConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.boot.testcontainers.service.connection.ServiceConnectionAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

/**
 * @author livk
 */
class RedissonClientFactoryTests {

	@Nested
	@SpringJUnitConfig
	@Testcontainers(disabledWithoutDocker = true, parallel = true)
	@Import({ ServiceConnectionAutoConfiguration.class, TestcontainersPropertySourceAutoConfiguration.class })
	class IntegrationTests {

		@Container
		@ServiceConnection
		static final RedisContainer redis = new RedisContainer(DockerImageNames.redis());

		@DynamicPropertySource
		static void redisProperties(DynamicPropertyRegistry registry) {
			registry.add("spring.redisson.config.single-server-config.address",
					() -> "redis://" + redis.getHost() + ":" + redis.getFirstMappedPort());
			registry.add("spring.redisson.config.codec", () -> "!<org.redisson.codec.JsonJackson3Codec> {}");
			registry.add("spring.data.redis.host", redis::getHost);
			registry.add("spring.data.redis.port", redis::getFirstMappedPort);
		}

		@Autowired
		ConfigurableEnvironment environment;

		@Autowired
		ObjectProvider<ConfigCustomizer> configCustomizers;

		RedissonProperties redissonProperties;

		DataRedisProperties redisProperties;

		@BeforeEach
		void before() {
			this.redissonProperties = RedissonProperties.load(environment);
			this.redisProperties = Binder.get(environment).bind("spring.data.redis", DataRedisProperties.class).get();
		}

		@Test
		void createTest() {
			RedissonClient redissonClient = RedissonClientFactory.create(redissonProperties, configCustomizers);

			assertThat(redissonClient).isNotNull();
			redissonClient.shutdown();
		}

		@Test
		void createByRedisPropertiesTest() {
			RedissonClient redissonClient = RedissonClientFactory.create(redisProperties, configCustomizers);

			assertThat(redissonClient).isNotNull();
			redissonClient.shutdown();
		}

		@Test
		void createAppliesConfigCustomizer() {
			AtomicBoolean customized = new AtomicBoolean();
			ObjectProvider<ConfigCustomizer> customizers = provider((config) -> customized.set(true));

			RedissonClient redissonClient = RedissonClientFactory.create(redisProperties, customizers);

			assertThat(customized).isTrue();
			assertThat(redissonClient).isNotNull();
			redissonClient.shutdown();
		}

	}

	@Nested
	class CreateConfigTests {

		@Test
		void createRejectsNullRedissonConfig() {
			RedissonProperties properties = new RedissonProperties();
			properties.setConfig(null);

			assertThatIllegalArgumentException().isThrownBy(() -> RedissonClientFactory.create(properties, provider()))
				.withMessageContaining("Redisson config must not be null");
		}

		@Test
		void singleServerUsesDefaults() {
			DataRedisProperties properties = singleServer("127.0.0.1", 6379);

			Config config = RedissonClientFactory.createConfig(properties);

			assertThat(config.useSingleServer().getAddress()).isEqualTo("redis://127.0.0.1:6379");
			assertThat(config.useSingleServer().getDatabase()).isEqualTo(0);
			assertThat(config.useSingleServer().getConnectTimeout()).isEqualTo(10000);
			assertThat(config.getPassword()).isNull();
		}

		@Test
		void singleServerUsesSslPasswordTimeoutAndDatabase() {
			DataRedisProperties properties = singleServer("redis.example", 6380);
			properties.setPassword("secret");
			properties.setDatabase(3);
			properties.setTimeout(Duration.ofSeconds(5));
			properties.getSsl().setEnabled(true);

			Config config = RedissonClientFactory.createConfig(properties);

			assertThat(config.useSingleServer().getAddress()).isEqualTo("rediss://redis.example:6380");
			assertThat(config.useSingleServer().getDatabase()).isEqualTo(3);
			assertThat(config.useSingleServer().getConnectTimeout()).isEqualTo(5000);
			assertThat(config.getPassword()).isEqualTo("secret");
		}

		@Test
		void sentinelConfigConvertsBareAndPrefixedNodes() {
			DataRedisProperties properties = new DataRedisProperties();
			properties.setPassword("sentinel-pass");
			properties.setDatabase(2);
			properties.setTimeout(Duration.ofMillis(2500));
			DataRedisProperties.Sentinel sentinel = new DataRedisProperties.Sentinel();
			sentinel.setMaster("mymaster");
			sentinel.setNodes(List.of("127.0.0.1:26379", "redis://127.0.0.1:26380", "rediss://127.0.0.1:26381"));
			properties.setSentinel(sentinel);

			Config config = RedissonClientFactory.createConfig(properties);

			assertThat(config.useSentinelServers().getMasterName()).isEqualTo("mymaster");
			assertThat(config.useSentinelServers().getSentinelAddresses()).containsExactly("redis://127.0.0.1:26379",
					"redis://127.0.0.1:26380", "rediss://127.0.0.1:26381");
			assertThat(config.useSentinelServers().getDatabase()).isEqualTo(2);
			assertThat(config.useSentinelServers().getConnectTimeout()).isEqualTo(2500);
			assertThat(config.getPassword()).isEqualTo("sentinel-pass");
		}

		@Test
		void sentinelConfigAllowsEmptyNodes() {
			DataRedisProperties properties = new DataRedisProperties();
			DataRedisProperties.Sentinel sentinel = new DataRedisProperties.Sentinel();
			sentinel.setMaster("mymaster");
			sentinel.setNodes(List.of());
			properties.setSentinel(sentinel);

			Config config = RedissonClientFactory.createConfig(properties);

			assertThat(config.useSentinelServers().getMasterName()).isEqualTo("mymaster");
			assertThat(config.useSentinelServers().getSentinelAddresses()).isEmpty();
		}

		@Test
		void clusterConfigConvertsBareAndPrefixedNodes() {
			DataRedisProperties properties = new DataRedisProperties();
			properties.setPassword("cluster-pass");
			properties.setTimeout(Duration.ofSeconds(3));
			DataRedisProperties.Cluster cluster = new DataRedisProperties.Cluster();
			cluster.setNodes(List.of("127.0.0.1:7000", "redis://127.0.0.1:7001", "rediss://127.0.0.1:7002"));
			properties.setCluster(cluster);

			Config config = RedissonClientFactory.createConfig(properties);

			assertThat(config.useClusterServers().getNodeAddresses()).containsExactly("redis://127.0.0.1:7000",
					"redis://127.0.0.1:7001", "rediss://127.0.0.1:7002");
			assertThat(config.useClusterServers().getConnectTimeout()).isEqualTo(3000);
			assertThat(config.getPassword()).isEqualTo("cluster-pass");
		}

		@Test
		void clusterConfigAllowsNullNodes() {
			DataRedisProperties properties = new DataRedisProperties();
			DataRedisProperties.Cluster cluster = new DataRedisProperties.Cluster();
			cluster.setNodes(null);
			properties.setCluster(cluster);

			Config config = RedissonClientFactory.createConfig(properties);

			assertThat(config.useClusterServers().getNodeAddresses()).isEmpty();
			assertThat(config.useClusterServers().getConnectTimeout()).isEqualTo(10000);
		}

		@Test
		void sentinelTakesPrecedenceOverCluster() {
			DataRedisProperties properties = new DataRedisProperties();
			DataRedisProperties.Sentinel sentinel = new DataRedisProperties.Sentinel();
			sentinel.setMaster("mymaster");
			sentinel.setNodes(List.of("127.0.0.1:26379"));
			properties.setSentinel(sentinel);
			DataRedisProperties.Cluster cluster = new DataRedisProperties.Cluster();
			cluster.setNodes(List.of("127.0.0.1:7000"));
			properties.setCluster(cluster);

			Config config = RedissonClientFactory.createConfig(properties);

			assertThat(config.isSentinelConfig()).isTrue();
			assertThat(config.isClusterConfig()).isFalse();
		}

	}

	private static DataRedisProperties singleServer(String host, int port) {
		DataRedisProperties properties = new DataRedisProperties();
		properties.setHost(host);
		properties.setPort(port);
		return properties;
	}

	@SuppressWarnings("all")
	private static ObjectProvider<ConfigCustomizer> provider(ConfigCustomizer... customizers) {
		return new ObjectProvider<>() {
			@Override
			public Stream<ConfigCustomizer> orderedStream() {
				return Stream.of(customizers);
			}

			@Override
			public ConfigCustomizer getObject() {
				return customizers.length == 0 ? null : customizers[0];
			}

			@Override
			public ConfigCustomizer getObject(Object... args) {
				return getObject();
			}

			@Override
			public ConfigCustomizer getIfAvailable() {
				return getObject();
			}

			@Override
			public ConfigCustomizer getIfUnique() {
				return customizers.length == 1 ? customizers[0] : null;
			}
		};
	}

}
