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
import com.livk.testcontainers.containers.ZookeeperContainer;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
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
 * Tests for {@link CuratorLockFactory}.
 * <p>
 * Verifies distributed lock acquisition, mutual exclusion across threads, and
 * non-reentrant behavior using a real Zookeeper instance via Testcontainers.
 *
 * @author livk
 */
@SpringJUnitConfig(CuratorLockFactoryTests.CuratorLockConfig.class)
@Testcontainers(disabledWithoutDocker = true, parallel = true)
class CuratorLockFactoryTests {

	@Container
	@ServiceConnection
	static final ZookeeperContainer zookeeper = new ZookeeperContainer();

	@DynamicPropertySource
	static void properties(DynamicPropertyRegistry registry) {
		registry.add("curator.connectString",
				() -> String.format("%s:%s", zookeeper.getHost(), zookeeper.getFirstMappedPort()));
	}

	static final ExecutorService service = Executors.newVirtualThreadPerTaskExecutor();

	/**
	 * The {@link DistLockFactory} under test, backed by a {@link CuratorLockFactory}.
	 */
	final DistLockFactory lock;

	@Autowired
	CuratorLockFactoryTests(CuratorFramework framework) {
		lock = new CuratorLockFactory(framework);
	}

	@AfterAll
	static void close() {
		service.close();
	}

	/**
	 * Verifies lock exclusion across threads and non-reentrant behavior of Curator locks
	 * within the same thread. Unlock is performed via
	 * {@link DistLockFactory.SpecLock#unlock()} which releases the lock held in the
	 * current thread's {@code ThreadLocal}.
	 */
	@Test
	void tryLock() throws ExecutionException, InterruptedException {
		lock.lock("tryLock").lock();
		assertThat(service.submit(() -> lock.lock("tryLock").leaseTime(3).waitTime(3).tryLock()).get()).isFalse();
		assertThat(lock.lock("tryLock").leaseTime(3).waitTime(3).tryLock()).isFalse();
		assertThat(lock.lock("key").leaseTime(3).waitTime(3).tryLock()).isTrue();

		lock.lock("key").unlock();

		assertThat(lock.lock("key").leaseTime(3).waitTime(3).tryLock()).isTrue();

		lock.lock("key").unlock();
	}

	/**
	 * Test configuration that provides a {@link CuratorFramework} connected to the
	 * Testcontainers Zookeeper instance.
	 */
	@TestConfiguration
	@Import({ ServiceConnectionAutoConfiguration.class, TestcontainersPropertySourceAutoConfiguration.class })
	static class CuratorLockConfig {

		@Bean(initMethod = "start", destroyMethod = "close")
		public CuratorFramework curatorFramework(@Value("${curator.connectString}") String connectString) {
			RetryPolicy retryPolicy = new ExponentialBackoffRetry(50, 10, 500);
			return CuratorFrameworkFactory.builder().retryPolicy(retryPolicy).connectString(connectString).build();
		}

	}

}
