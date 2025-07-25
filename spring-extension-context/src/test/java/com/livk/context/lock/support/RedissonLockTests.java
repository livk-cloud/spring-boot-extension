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

import com.livk.context.lock.DistributedLock;
import com.livk.context.lock.LockType;
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
 * @author livk
 */
@SpringJUnitConfig(RedissonLockTests.RedissonLockConfig.class)
@Testcontainers(disabledWithoutDocker = true, parallel = true)
class RedissonLockTests {

	@Container
	@ServiceConnection
	static final RedisContainer redis = new RedisContainer(DockerImageNames.redis());

	@DynamicPropertySource
	static void properties(DynamicPropertyRegistry registry) {
		registry.add("redisson.address", () -> "redis://" + redis.getHost() + ":" + redis.getFirstMappedPort());
	}

	static final ExecutorService service = Executors.newVirtualThreadPerTaskExecutor();

	@Autowired
	RedissonLockTests(RedissonClient redissonClient) {
		lock = new RedissonLock(redissonClient);
	}

	final DistributedLock lock;

	@AfterAll
	static void close() {
		service.close();
	}

	@Test
	void tryLock() throws ExecutionException, InterruptedException {
		lock.lock(LockType.LOCK, "tryLock", false);
		assertThat(service.submit(() -> lock.tryLock(LockType.LOCK, "tryLock", 3, 3, false)).get()).isFalse();
		assertThat(lock.tryLock(LockType.LOCK, "tryLock", 3, 3, false)).isTrue();
		assertThat(lock.tryLock(LockType.LOCK, "key", 3, 3, false)).isTrue();

		lock.unlock();

		assertThat(lock.tryLock(LockType.LOCK, "key", 3, 3, false)).isTrue();

		lock.unlock();
	}

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
