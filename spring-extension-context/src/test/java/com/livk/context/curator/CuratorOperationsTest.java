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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertLinesMatch;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author livk
 */
@SpringJUnitConfig(CuratorConfig.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Testcontainers(disabledWithoutDocker = true, parallel = true)
class CuratorOperationsTest {

	@Container
	@ServiceConnection
	static ZookeeperContainer zookeeper = new ZookeeperContainer();

	@DynamicPropertySource
	static void properties(DynamicPropertyRegistry registry) {
		registry.add("curator.connectString",
				() -> String.format("%s:%s", zookeeper.getHost(), zookeeper.getMappedPort(2181)));
	}

	@Autowired
	CuratorOperations curatorOperations;

	@Test
	@Order(1)
	void createNode() throws Exception {
		String data = curatorOperations.createNode("/node", "data".getBytes(StandardCharsets.UTF_8));
		assertEquals("/node", data);
		curatorOperations.deleteNode("/node");
	}

	@Test
	@Order(2)
	void getNode() throws Exception {
		String data = curatorOperations.createNode("/node", "data".getBytes(StandardCharsets.UTF_8));
		assertEquals("/node", data);
		assertArrayEquals("data".getBytes(StandardCharsets.UTF_8), curatorOperations.getNode("/node"));
		curatorOperations.deleteNode("/node");
	}

	@Test
	@Order(3)
	void createTypeNode() throws Exception {
		String persistent = curatorOperations.createTypeNode(CreateMode.PERSISTENT, "/node",
				"data".getBytes(StandardCharsets.UTF_8));
		assertEquals("/node", persistent);
		curatorOperations.deleteNode("/node");

		String persistentSequential = curatorOperations.createTypeNode(CreateMode.PERSISTENT_SEQUENTIAL, "/node",
				"data".getBytes(StandardCharsets.UTF_8));
		assertTrue(persistentSequential.startsWith("/node"));

		String ephemeral = curatorOperations.createTypeNode(CreateMode.EPHEMERAL, "/node",
				"data".getBytes(StandardCharsets.UTF_8));
		assertEquals("/node", ephemeral);
		curatorOperations.deleteNode("/node");

		String ephemeralSequential = curatorOperations.createTypeNode(CreateMode.EPHEMERAL_SEQUENTIAL, "/node",
				"data".getBytes(StandardCharsets.UTF_8));
		assertTrue(ephemeralSequential.startsWith("/node"));

		String container = curatorOperations.createTypeNode(CreateMode.CONTAINER, "/node",
				"data".getBytes(StandardCharsets.UTF_8));
		assertEquals("/node", container);
		curatorOperations.deleteNode("/node");
	}

	@Test
	@Order(4)
	void createTypeSeqNode() throws Exception {
		String persistent = curatorOperations.createTypeSeqNode(CreateMode.PERSISTENT, "/node",
				"data".getBytes(StandardCharsets.UTF_8));
		assertTrue(persistent.endsWith("node"));

		String persistentSequential = curatorOperations.createTypeSeqNode(CreateMode.PERSISTENT_SEQUENTIAL, "/node",
				"data".getBytes(StandardCharsets.UTF_8));
		assertTrue(persistentSequential.contains("node"));

		String ephemeral = curatorOperations.createTypeSeqNode(CreateMode.EPHEMERAL, "/node",
				"data".getBytes(StandardCharsets.UTF_8));
		assertTrue(ephemeral.endsWith("node"));

		String ephemeralSequential = curatorOperations.createTypeSeqNode(CreateMode.EPHEMERAL_SEQUENTIAL, "/node",
				"data".getBytes(StandardCharsets.UTF_8));
		assertTrue(ephemeralSequential.contains("node"));

		String container = curatorOperations.createTypeSeqNode(CreateMode.CONTAINER, "/node",
				"data".getBytes(StandardCharsets.UTF_8));
		assertTrue(container.endsWith("node"));
	}

	@Test
	@Order(5)
	void setData() throws Exception {
		curatorOperations.createNode("/node", "data".getBytes(StandardCharsets.UTF_8));
		assertArrayEquals("data".getBytes(StandardCharsets.UTF_8), curatorOperations.getNode("/node"));
		Stat data = curatorOperations.setData("/node", "setData".getBytes(StandardCharsets.UTF_8));
		assertNotNull(data);
		assertArrayEquals("setData".getBytes(), curatorOperations.getNode("/node"));
		curatorOperations.deleteNode("/node");
	}

	@Test
	@Order(6)
	void setDataAsync() throws Exception {
		curatorOperations.createNode("/node", "data".getBytes(StandardCharsets.UTF_8));
		assertArrayEquals("data".getBytes(), curatorOperations.getNode("/node"));
		curatorOperations.setDataAsync("/node", "setData".getBytes(StandardCharsets.UTF_8), (client, event) -> {
			if (CuratorEventType.SET_DATA.equals(event.getType())) {
				assertEquals("/node", event.getPath());
				assertNotNull(event.getStat());
			}
		});
		assertArrayEquals("setData".getBytes(), curatorOperations.getNode("/node"));
		curatorOperations.deleteNode("/node");
	}

	@Test
	@Order(7)
	void deleteNode() throws Exception {
		curatorOperations.createNode("/node", "data".getBytes(StandardCharsets.UTF_8));
		curatorOperations.deleteNode("/node");
	}

	@Test
	@Order(8)
	void watchedGetChildren() throws Exception {
		for (int i = 0; i < 10; i++) {
			String data = curatorOperations.createNode("/node/" + i, "data".getBytes(StandardCharsets.UTF_8));
			assertEquals("/node/" + i, data);
		}
		List<String> paths = curatorOperations.watchedGetChildren("/node");
		assertLinesMatch(List.of("0", "1", "2", "3", "4", "5", "6", "7", "8", "9"), paths);
		curatorOperations.deleteNode("/node");
	}

	@Test
	@Order(9)
	void testWatchedGetChildren() throws Exception {
		for (int i = 0; i < 10; i++) {
			String data = curatorOperations.createNode("/node/" + i, "data".getBytes(StandardCharsets.UTF_8));
			assertEquals("/node/" + i, data);
		}
		List<String> paths = curatorOperations.watchedGetChildren("/node", event -> {
			assertEquals(Watcher.Event.KeeperState.SyncConnected, event.getState());
			assertEquals(Watcher.Event.EventType.NodeChildrenChanged, event.getType());
			assertEquals("/node", event.getPath());
		});
		assertLinesMatch(List.of("0", "1", "2", "3", "4", "5", "6", "7", "8", "9"), paths);

		curatorOperations.deleteNode("/node");
	}

	@Test
	@Order(10)
	void getLock() throws Exception {
		InterProcessLock lock = curatorOperations.getLock("/node/lock", ZkLockType.LOCK);
		assertEquals(InterProcessMutex.class, lock.getClass());
		assertTrue(lock.acquire(3, TimeUnit.SECONDS));
		lock.release();

		InterProcessLock read = curatorOperations.getLock("/node/lock", ZkLockType.READ);
		assertEquals(InterProcessReadWriteLock.ReadLock.class, read.getClass());
		assertTrue(read.acquire(3, TimeUnit.SECONDS));
		read.release();

		InterProcessLock write = curatorOperations.getLock("/node/lock", ZkLockType.WRITE);
		assertEquals(InterProcessReadWriteLock.WriteLock.class, write.getClass());
		assertTrue(write.acquire(3, TimeUnit.SECONDS));
		write.release();

		curatorOperations.deleteNode("/node");
	}

	@Test
	@Order(11)
	void getDistributedId() throws Exception {
		String data = curatorOperations.createNode("/node", "data".getBytes(StandardCharsets.UTF_8));
		assertEquals("/node", data);
		String distributedId = curatorOperations.getDistributedId("/node", "data".getBytes(StandardCharsets.UTF_8));
		assertNotNull(distributedId);
		curatorOperations.deleteNode("/node");
	}

}
