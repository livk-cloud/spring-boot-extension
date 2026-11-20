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

import com.livk.context.lock.LockType;
import com.livk.context.lock.exception.LockException;
import com.livk.testcontainers.DockerImageNames;
import com.redis.testcontainers.RedisContainer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.redisson.Redisson;
import org.redisson.api.RFuture;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
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
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author livk
 */
@SpringJUnitConfig(RedissonLockFactoryTypeTests.Config.class)
@Testcontainers(disabledWithoutDocker = true, parallel = true)
class RedissonLockFactoryTypeTests {

	@Container
	@ServiceConnection
	static final RedisContainer redis = new RedisContainer(DockerImageNames.redis());

	@DynamicPropertySource
	static void properties(DynamicPropertyRegistry registry) {
		registry.add("redisson.address", () -> "redis://" + redis.getHost() + ":" + redis.getFirstMappedPort());
	}

	@Autowired
	RedissonClient redissonClient;

	@ParameterizedTest
	@EnumSource(LockType.class)
	void lockAndUnlockForAllTypes(LockType type) {
		RedissonLockFactory factory = new RedissonLockFactory(this.redissonClient);
		String key = "type-" + type.name().toLowerCase();

		factory.lock(key, type).leaseTime(3).lock();
		assertThat(factory.lock(key, type).leaseTime(3).waitTime(1).tryLock()).isTrue();
		factory.lock(key, type).unlock();
		factory.lock(key, type).unlock();
	}

	@Test
	void asyncLockAndTryLockWithLease() {
		RedissonLockFactory factory = new RedissonLockFactory(this.redissonClient);

		factory.lock("async-lock").async().leaseTime(3).lock();
		assertThat(factory.lock("async-lock").async().leaseTime(3).waitTime(1).tryLock()).isTrue();
		factory.lock("async-lock").unlock();
		factory.lock("async-lock").unlock();

		factory.lock("async-watchdog").async().lock();
		factory.lock("async-watchdog").unlock();
	}

	@Test
	void syncLockWithoutLeaseUsesWatchdog() {
		RedissonLockFactory factory = new RedissonLockFactory(this.redissonClient);
		factory.lock("watchdog").lock();
		factory.lock("watchdog").unlock();
	}

	@Test
	void tryLockAsyncConvertsInterruptedException() throws Exception {
		RedissonClient client = mock(RedissonClient.class);
		RLock lock = mock(RLock.class);
		@SuppressWarnings("unchecked")
		RFuture<Boolean> future = mock(RFuture.class);
		when(client.getLock("err")).thenReturn(lock);
		when(lock.tryLockAsync(anyLong(), anyLong(), eq(TimeUnit.SECONDS))).thenReturn(future);
		when(future.get()).thenThrow(new InterruptedException("interrupted"));

		RedissonLockFactory factory = new RedissonLockFactory(client);
		assertThatExceptionOfType(LockException.class)
			.isThrownBy(() -> factory.lock("err").async().leaseTime(1).waitTime(1).tryLock());
		assertThat(Thread.currentThread().isInterrupted()).isTrue();
		Thread.interrupted();
	}

	@Test
	void tryLockAsyncConvertsExecutionException() throws Exception {
		RedissonClient client = mock(RedissonClient.class);
		RLock lock = mock(RLock.class);
		@SuppressWarnings("unchecked")
		RFuture<Boolean> future = mock(RFuture.class);
		when(client.getLock("err2")).thenReturn(lock);
		when(lock.tryLockAsync(anyLong(), anyLong(), eq(TimeUnit.SECONDS))).thenReturn(future);
		when(future.get()).thenThrow(new ExecutionException(new TimeoutException("timeout")));

		RedissonLockFactory factory = new RedissonLockFactory(client);
		assertThatExceptionOfType(LockException.class)
			.isThrownBy(() -> factory.lock("err2").async().leaseTime(1).waitTime(1).tryLock());
	}

	@Test
	void doLockAsyncWithAndWithoutLease() throws Exception {
		RedissonClient client = mock(RedissonClient.class);
		RLock lock = mock(RLock.class);
		@SuppressWarnings("unchecked")
		RFuture<Void> leased = mock(RFuture.class);
		@SuppressWarnings("unchecked")
		RFuture<Void> watchdog = mock(RFuture.class);
		when(client.getLock(any(String.class))).thenReturn(lock);
		when(lock.lockAsync(anyLong(), eq(TimeUnit.SECONDS))).thenReturn(leased);
		when(lock.lockAsync()).thenReturn(watchdog);
		when(leased.get()).thenReturn(null);
		when(watchdog.get()).thenReturn(null);
		when(lock.isLocked()).thenReturn(true);
		when(lock.isHeldByCurrentThread()).thenReturn(true);

		RedissonLockFactory factory = new RedissonLockFactory(client);
		factory.lock("leased").async().leaseTime(2).lock();
		verify(lock).lockAsync(2L, TimeUnit.SECONDS);

		factory.lock("watchdog-async").async().lock();
		verify(lock).lockAsync();
	}

	@TestConfiguration
	@Import({ ServiceConnectionAutoConfiguration.class, TestcontainersPropertySourceAutoConfiguration.class })
	static class Config {

		@Bean
		RedissonClient redissonClient(@Value("${redisson.address}") String address) {
			org.redisson.config.Config config = new org.redisson.config.Config();
			config.useSingleServer().setAddress(address);
			return Redisson.create(config);
		}

	}

}
