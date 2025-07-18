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

package com.livk.autoconfigure.curator.actuator;

import com.livk.autoconfigure.curator.CuratorProperties;
import com.livk.testcontainers.containers.ZookeeperContainer;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.jupiter.api.Test;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * <p>
 * CuratorHealthIndicatorTest
 * </p>
 *
 * @author livk
 */
@Testcontainers(disabledWithoutDocker = true, parallel = true)
class CuratorHealthIndicatorTest {

	@Test
	void curatorUp() throws InterruptedException {
		try (ZookeeperContainer zookeeper = new ZookeeperContainer()) {
			zookeeper.start();
			CuratorProperties properties = new CuratorProperties();
			properties.setConnectString(String.format("%s:%s", zookeeper.getHost(), zookeeper.getFirstMappedPort()));
			RetryPolicy retryPolicy = new ExponentialBackoffRetry(properties.getBaseSleepTimeMs(),
					properties.getMaxRetries(), properties.getMaxSleepMs());
			try (CuratorFramework framework = create(properties, retryPolicy)) {
				CuratorHealthIndicator indicator = new CuratorHealthIndicator(framework);
				Health health = indicator.health();
				assertEquals(Status.DOWN, health.getStatus());
				assertEquals(properties.getConnectString(), health.getDetails().get("connectionString"));
			}
		}
	}

	@Test
	void curatorDown() throws InterruptedException {
		CuratorProperties properties = new CuratorProperties();
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(properties.getBaseSleepTimeMs(),
				properties.getMaxRetries(), properties.getMaxSleepMs());
		try (CuratorFramework framework = create(properties, retryPolicy)) {
			CuratorHealthIndicator indicator = new CuratorHealthIndicator(framework);
			Health health = indicator.health();
			assertEquals(Status.DOWN, health.getStatus());
		}
	}

	public static CuratorFramework create(CuratorProperties properties, RetryPolicy retryPolicy)
			throws InterruptedException {
		CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder()
			.connectString(properties.getConnectString())
			.sessionTimeoutMs((int) properties.getSessionTimeout().toMillis())
			.connectionTimeoutMs((int) properties.getConnectionTimeout().toMillis())
			.retryPolicy(retryPolicy);
		CuratorFramework framework = builder.build();
		framework.blockUntilConnected(properties.getBlockUntilConnectedWait(), properties.getBlockUntilConnectedUnit());
		return framework;
	}

}
