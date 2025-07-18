/*
 * Copyright 2021-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * you may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.livk.autoconfigure.lock;

import com.livk.commons.util.ClassUtils;
import com.livk.commons.util.GenericsByteBuddy;
import com.livk.context.lock.intercept.DistributedLockInterceptor;
import com.livk.context.lock.support.CuratorLock;
import com.livk.context.lock.support.RedissonLock;
import com.livk.testcontainers.DockerImageNames;
import com.livk.testcontainers.containers.ZookeeperContainer;
import com.redis.testcontainers.RedisContainer;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.jupiter.api.Test;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.boot.testcontainers.properties.TestcontainersPropertySourceAutoConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.boot.testcontainers.service.connection.ServiceConnectionAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
@SpringJUnitConfig(LockAutoConfigurationTest.Config.class)
@Testcontainers(disabledWithoutDocker = true, parallel = true)
@Import({ ServiceConnectionAutoConfiguration.class, TestcontainersPropertySourceAutoConfiguration.class })
class LockAutoConfigurationTest {

	static {
		try (DynamicType.Unloaded<Object> unloaded = new GenericsByteBuddy().subclass(Object.class)
			.name("com.livk.lock.marker.LockMarker")
			.make()) {
			Class<?> loaded = unloaded.load(ClassUtils.getDefaultClassLoader(), ClassLoadingStrategy.Default.INJECTION)
				.getLoaded();
			assertThat(loaded).isNotNull();
		}
	}

	@Container
	@ServiceConnection
	static final RedisContainer redis = new RedisContainer(DockerImageNames.redis());

	@Container
	@ServiceConnection
	static final ZookeeperContainer zookeeper = new ZookeeperContainer();

	@DynamicPropertySource
	static void redisProperties(DynamicPropertyRegistry registry) {
		registry.add("redisson.single-server-config.address",
				() -> "redis://" + redis.getHost() + ":" + redis.getFirstMappedPort());

		registry.add("curator.connectString",
				() -> String.format("%s:%s", zookeeper.getHost(), zookeeper.getFirstMappedPort()));
	}

	@Autowired
	ApplicationContext applicationContext;

	final ApplicationContextRunner contextRunner = new ApplicationContextRunner(new Supplier<>() {
		@Override
		public ConfigurableApplicationContext get() {
			return new GenericApplicationContext(applicationContext);
		}
	}).withConfiguration(AutoConfigurations.of(LockAutoConfiguration.class));

	@Test
	void test() {
		this.contextRunner.run((context) -> {
			assertThat(context).hasSingleBean(DistributedLockInterceptor.class);
			assertThat(context).hasSingleBean(RedissonLock.class);
			assertThat(context).hasSingleBean(CuratorLock.class);
		});
	}

	@TestConfiguration
	static class Config {

		@Bean
		RedissonClient redissonClient(@Value("${redisson.single-server-config.address}") String address) {
			org.redisson.config.Config config = new org.redisson.config.Config();
			config.useSingleServer().setAddress(address);
			return Redisson.create(config);
		}

		@Bean
		CuratorFramework curatorFramework(@Value("${curator.connectString}") String connectString) {
			RetryPolicy retryPolicy = new ExponentialBackoffRetry(50, 10, 500);
			return CuratorFrameworkFactory.builder().retryPolicy(retryPolicy).connectString(connectString).build();
		}

	}

}
