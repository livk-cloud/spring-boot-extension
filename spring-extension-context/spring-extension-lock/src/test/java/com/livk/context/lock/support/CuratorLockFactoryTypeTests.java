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
import com.livk.testcontainers.containers.ZookeeperContainer;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
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

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author livk
 */
@SpringJUnitConfig(CuratorLockFactoryTypeTests.Config.class)
@Testcontainers(disabledWithoutDocker = true, parallel = true)
class CuratorLockFactoryTypeTests {

	@Container
	@ServiceConnection
	static final ZookeeperContainer zookeeper = new ZookeeperContainer();

	@DynamicPropertySource
	static void properties(DynamicPropertyRegistry registry) {
		registry.add("curator.connectString",
				() -> String.format("%s:%s", zookeeper.getHost(), zookeeper.getFirstMappedPort()));
	}

	@Autowired
	CuratorFramework framework;

	@ParameterizedTest
	@EnumSource(LockType.class)
	void lockAndUnlockForAllTypes(LockType type) {
		CuratorLockFactory factory = new CuratorLockFactory(this.framework);
		String key = "type-" + type.name().toLowerCase();

		factory.lock(key, type).lock();
		assertThat(factory.lock(key, type).waitTime(1).tryLock()).isTrue();
		factory.lock(key, type).unlock();
		factory.lock(key, type).unlock();
	}

	@Test
	void prependsSlashForKeyWithoutPrefix() {
		CuratorLockFactory factory = new CuratorLockFactory(this.framework);
		factory.lock("no-slash").lock();
		factory.lock("no-slash").unlock();
		factory.lock("/with-slash").lock();
		factory.lock("/with-slash").unlock();
	}

	@Test
	void asyncFallsBackToSyncWhenUnsupported() {
		CuratorLockFactory factory = new CuratorLockFactory(this.framework);
		assertThat(factory.lock("async-fallback").async().waitTime(1).tryLock()).isTrue();
		factory.lock("async-fallback").unlock();
	}

	@Test
	void unlockWrapsReleaseException() throws Exception {
		InterProcessLock mockLock = mock(InterProcessLock.class);
		when(mockLock.isAcquiredInThisProcess()).thenReturn(true);
		doThrow(new Exception("release failed")).when(mockLock).release();

		CuratorLockFactory factory = new CuratorLockFactory(mock(CuratorFramework.class)) {
			@Override
			protected InterProcessLock getLock(LockType type, String key) {
				return mockLock;
			}

			@Override
			protected void doLock(InterProcessLock lock, long leaseTime) {
				// skip real acquire
			}
		};

		factory.lock("x").lock();
		assertThatExceptionOfType(LockException.class).isThrownBy(() -> factory.lock("x").unlock());
	}

	@Test
	void tryLockWrapsAcquireException() throws Exception {
		InterProcessLock mockLock = mock(InterProcessLock.class);
		when(mockLock.acquire(anyLong(), any(TimeUnit.class))).thenThrow(new Exception("acquire failed"));

		CuratorLockFactory factory = new CuratorLockFactory(mock(CuratorFramework.class)) {
			@Override
			protected InterProcessLock getLock(LockType type, String key) {
				return mockLock;
			}
		};

		assertThatExceptionOfType(LockException.class).isThrownBy(() -> factory.lock("x").waitTime(1).tryLock());
	}

	@TestConfiguration
	@Import({ ServiceConnectionAutoConfiguration.class, TestcontainersPropertySourceAutoConfiguration.class })
	static class Config {

		@Bean(initMethod = "start", destroyMethod = "close")
		CuratorFramework curatorFramework(@Value("${curator.connectString}") String connectString) {
			RetryPolicy retryPolicy = new ExponentialBackoffRetry(50, 10, 500);
			return CuratorFrameworkFactory.builder().retryPolicy(retryPolicy).connectString(connectString).build();
		}

	}

}
