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

package com.livk.context.curator;

import com.livk.context.curator.lock.ZkLockType;
import com.livk.testcontainers.containers.ZookeeperContainer;
import org.apache.curator.framework.api.CuratorEventType;
import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.locks.InterProcessReadWriteLock;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.data.Stat;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
@SpringJUnitConfig(CuratorConfig.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Testcontainers(disabledWithoutDocker = true, parallel = true)
class CuratorOperationsTests {

	@Container
	@ServiceConnection
	static final ZookeeperContainer zookeeper = new ZookeeperContainer();

	@DynamicPropertySource
	static void properties(DynamicPropertyRegistry registry) {
		registry.add("curator.connectString",
				() -> String.format("%s:%s", zookeeper.getHost(), zookeeper.getFirstMappedPort()));
	}

	@Autowired
	CuratorOperations curatorOperations;

	@Test
	@Order(1)
	void createNode() {
		String data = curatorOperations.createNode("/node", "data".getBytes(StandardCharsets.UTF_8));
		assertThat(data).isEqualTo("/node");
		curatorOperations.deleteNode("/node");
	}

	@Test
	@Order(2)
	void getNode() {
		String data = curatorOperations.createNode("/node", "data".getBytes(StandardCharsets.UTF_8));
		assertThat(data).isEqualTo("/node");
		assertThat(curatorOperations.getNode("/node")).containsExactly("data".getBytes(StandardCharsets.UTF_8));
		curatorOperations.deleteNode("/node");
	}

	@Test
	@Order(3)
	void createTypeNode() {
		String persistent = curatorOperations.createTypeNode(CreateMode.PERSISTENT, "/node",
				"data".getBytes(StandardCharsets.UTF_8));
		assertThat(persistent).isEqualTo("/node");
		curatorOperations.deleteNode("/node");

		String persistentSequential = curatorOperations.createTypeNode(CreateMode.PERSISTENT_SEQUENTIAL, "/node",
				"data".getBytes(StandardCharsets.UTF_8));
		assertThat(persistentSequential).startsWith("/node");

		String ephemeral = curatorOperations.createTypeNode(CreateMode.EPHEMERAL, "/node",
				"data".getBytes(StandardCharsets.UTF_8));
		assertThat(ephemeral).isEqualTo("/node");
		curatorOperations.deleteNode("/node");

		String ephemeralSequential = curatorOperations.createTypeNode(CreateMode.EPHEMERAL_SEQUENTIAL, "/node",
				"data".getBytes(StandardCharsets.UTF_8));
		assertThat(ephemeralSequential).startsWith("/node");

		String container = curatorOperations.createTypeNode(CreateMode.CONTAINER, "/node",
				"data".getBytes(StandardCharsets.UTF_8));
		assertThat(container).isEqualTo("/node");
		curatorOperations.deleteNode("/node");
	}

	@Test
	@Order(4)
	void createTypeSeqNode() {
		String persistent = curatorOperations.createTypeSeqNode(CreateMode.PERSISTENT, "/node",
				"data".getBytes(StandardCharsets.UTF_8));
		assertThat(persistent).endsWith("node");

		String persistentSequential = curatorOperations.createTypeSeqNode(CreateMode.PERSISTENT_SEQUENTIAL, "/node",
				"data".getBytes(StandardCharsets.UTF_8));
		assertThat(persistentSequential).contains("node");

		String ephemeral = curatorOperations.createTypeSeqNode(CreateMode.EPHEMERAL, "/node",
				"data".getBytes(StandardCharsets.UTF_8));
		assertThat(ephemeral).endsWith("node");

		String ephemeralSequential = curatorOperations.createTypeSeqNode(CreateMode.EPHEMERAL_SEQUENTIAL, "/node",
				"data".getBytes(StandardCharsets.UTF_8));
		assertThat(ephemeralSequential).contains("node");

		String container = curatorOperations.createTypeSeqNode(CreateMode.CONTAINER, "/node",
				"data".getBytes(StandardCharsets.UTF_8));
		assertThat(container).endsWith("node");
	}

	@Test
	@Order(5)
	void setData() {
		curatorOperations.createNode("/node", "data".getBytes(StandardCharsets.UTF_8));
		assertThat(curatorOperations.getNode("/node")).containsExactly("data".getBytes(StandardCharsets.UTF_8));
		Stat data = curatorOperations.setData("/node", "setData".getBytes(StandardCharsets.UTF_8));
		assertThat(data).isNotNull();
		assertThat(curatorOperations.getNode("/node")).containsExactly("setData".getBytes());
		curatorOperations.deleteNode("/node");
	}

	@Test
	@Order(6)
	void setDataAsync() {
		curatorOperations.createNode("/node", "data".getBytes(StandardCharsets.UTF_8));
		assertThat(curatorOperations.getNode("/node")).containsExactly("data".getBytes(StandardCharsets.UTF_8));

		curatorOperations.setDataAsync("/node", "setData".getBytes(StandardCharsets.UTF_8), (client, event) -> {
			if (CuratorEventType.SET_DATA.equals(event.getType())) {
				assertThat(event.getPath()).isEqualTo("/node");
				assertThat(event.getStat()).isNotNull();
			}
		});

		assertThat(curatorOperations.getNode("/node")).containsExactly("setData".getBytes(StandardCharsets.UTF_8));
		curatorOperations.deleteNode("/node");
	}

	@Test
	@Order(7)
	void deleteNode() {
		curatorOperations.createNode("/node", "data".getBytes(StandardCharsets.UTF_8));
		curatorOperations.deleteNode("/node");
	}

	@Test
	@Order(8)
	void watchedGetChildren() {
		for (int i = 0; i < 10; i++) {
			String data = curatorOperations.createNode("/node/" + i, "data".getBytes(StandardCharsets.UTF_8));
			assertThat(data).as("Created node path /node/%d", i).isEqualTo("/node/" + i);
		}

		List<String> paths = curatorOperations.watchedGetChildren("/node");
		assertThat(paths).as("Children of /node").containsExactly("0", "1", "2", "3", "4", "5", "6", "7", "8", "9");
		curatorOperations.deleteNode("/node");
	}

	@Test
	@Order(9)
	void testWatchedGetChildren() {
		for (int i = 0; i < 10; i++) {
			String data = curatorOperations.createNode("/node/" + i, "data".getBytes(StandardCharsets.UTF_8));
			assertThat(data).as("Created node path /node/%d", i).isEqualTo("/node/" + i);
		}

		List<String> paths = curatorOperations.watchedGetChildren("/node", event -> {
			assertThat(event.getState()).as("Watcher event state").isEqualTo(Watcher.Event.KeeperState.SyncConnected);
			assertThat(event.getType()).as("Watcher event type").isEqualTo(Watcher.Event.EventType.NodeChildrenChanged);
			assertThat(event.getPath()).as("Watcher event path").isEqualTo("/node");
		});

		assertThat(paths).as("Children of /node").containsExactly("0", "1", "2", "3", "4", "5", "6", "7", "8", "9");

		curatorOperations.deleteNode("/node");
	}

	@Test
	@Order(10)
	void getLock() throws Exception {
		InterProcessLock lock = curatorOperations.getLock("/node/lock", ZkLockType.REENTRANT);
		assertThat(lock.getClass()).isEqualTo(InterProcessMutex.class);
		assertThat(lock.acquire(3, TimeUnit.SECONDS)).isTrue();
		lock.release();

		InterProcessLock read = curatorOperations.getLock("/node/lock", ZkLockType.READ);
		assertThat(read.getClass()).isEqualTo(InterProcessReadWriteLock.ReadLock.class);
		assertThat(read.acquire(3, TimeUnit.SECONDS)).isTrue();
		read.release();

		InterProcessLock write = curatorOperations.getLock("/node/lock", ZkLockType.WRITE);
		assertThat(write.getClass()).isEqualTo(InterProcessReadWriteLock.WriteLock.class);
		assertThat(write.acquire(3, TimeUnit.SECONDS)).isTrue();
		write.release();

		curatorOperations.deleteNode("/node");
	}

	@Test
	@Order(11)
	void getDistributedId() {
		String data = curatorOperations.createNode("/node", "data".getBytes(StandardCharsets.UTF_8));
		assertThat(data).as("Created node path").isEqualTo("/node");

		String distributedId = curatorOperations.getDistributedId("/node", "data".getBytes(StandardCharsets.UTF_8));
		assertThat(distributedId).as("Distributed ID").isNotNull();

		curatorOperations.deleteNode("/node");
	}

}
