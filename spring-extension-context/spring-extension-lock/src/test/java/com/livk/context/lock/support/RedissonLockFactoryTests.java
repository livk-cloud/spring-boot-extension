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

package com.livk.context.lock.support;

import com.livk.context.lock.DistLockFactory;
import com.livk.testcontainers.DockerImageNames;
import com.redis.testcontainers.RedisContainer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.properties.TestcontainersPropertySourceAutoConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.boot.testcontainers.service.connection.ServiceConnectionAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link RedissonLockFactory}.
 * <p>
 * Verifies distributed lock acquisition, reentrant behavior, and cross-thread exclusion
 * using a real Redis instance via Testcontainers.
 *
 * @author livk
 */
@SpringJUnitConfig(RedissonLockFactoryTests.RedissonLockConfig.class)
@Testcontainers(disabledWithoutDocker = true, parallel = true)
class RedissonLockFactoryTests {

	@Container
	@ServiceConnection
	static final RedisContainer redis = new RedisContainer(DockerImageNames.redis());

	@DynamicPropertySource
	static void properties(DynamicPropertyRegistry registry) {
		registry.add("redisson.address", () -> "redis://" + redis.getHost() + ":" + redis.getFirstMappedPort());
	}

	static final ExecutorService service = Executors.newVirtualThreadPerTaskExecutor();

	/**
	 * The {@link DistLockFactory} under test, backed by a {@link RedissonLockFactory}.
	 */
	final DistLockFactory lock;

	@Autowired
	RedissonLockFactoryTests(RedissonClient redissonClient) {
		lock = new RedissonLockFactory(redissonClient);
	}

	@AfterAll
	static void close() {
		service.close();
	}

	/**
	 * Verifies lock exclusion across threads and reentrant acquisition within the same
	 * thread. Unlock is performed via {@link DistLockFactory.SpecLock#unlock()} which
	 * releases the lock held in the current thread's {@code ThreadLocal}.
	 */
	@Test
	void tryLock() throws ExecutionException, InterruptedException {
		lock.lock("tryLock").lock();
		assertThat(service.submit(() -> lock.lock("tryLock").leaseTime(3).waitTime(3).tryLock()).get()).isFalse();
		assertThat(lock.lock("tryLock").leaseTime(3).waitTime(3).tryLock()).isTrue();
		assertThat(lock.lock("key").leaseTime(3).waitTime(3).tryLock()).isTrue();

		lock.lock("key").unlock();

		assertThat(lock.lock("key").leaseTime(3).waitTime(3).tryLock()).isTrue();

		lock.lock("key").unlock();
	}

	/**
	 * Test configuration that provides a {@link RedissonClient} connected to the
	 * Testcontainers Redis instance.
	 */
	@TestConfiguration
	@Import({ ServiceConnectionAutoConfiguration.class, TestcontainersPropertySourceAutoConfiguration.class })
	static class RedissonLockConfig {

		@Bean
		public RedissonClient redissonLock(@Value("${redisson.address}") String address) {
			Config config = new Config();
			config.useSingleServer().setAddress(address);
			return Redisson.create(config);
		}

	}

}
