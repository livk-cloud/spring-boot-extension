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
import org.apache.curator.framework.recipes.locks.InterProcessSemaphoreMutex;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.junit.jupiter.api.Test;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author livk
 */
@SpringJUnitConfig(CuratorConfig.class)
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

	// --- createNode / getNode / deleteNode ---

	@Test
	void createNodeAndGetNode() {
		String path = curatorOperations.createNode("/createGet", "data".getBytes(StandardCharsets.UTF_8));
		assertThat(path).isEqualTo("/createGet");
		assertThat(curatorOperations.getNode("/createGet")).isEqualTo("data".getBytes(StandardCharsets.UTF_8));
		curatorOperations.deleteNode("/createGet");
	}

	@Test
	void deleteNodeRemovesNode() {
		curatorOperations.createNode("/toDelete", "data".getBytes(StandardCharsets.UTF_8));
		curatorOperations.deleteNode("/toDelete");
		assertThatThrownBy(() -> curatorOperations.getNode("/toDelete")).isInstanceOf(CuratorException.class);
	}

	// --- createTypeNode ---

	@Test
	void createPersistentNode() {
		String path = curatorOperations.createTypeNode(CreateMode.PERSISTENT, "/persistent",
				"data".getBytes(StandardCharsets.UTF_8));
		assertThat(path).isEqualTo("/persistent");
		curatorOperations.deleteNode("/persistent");
	}

	@Test
	void createEphemeralNode() {
		String path = curatorOperations.createTypeNode(CreateMode.EPHEMERAL, "/ephemeral",
				"data".getBytes(StandardCharsets.UTF_8));
		assertThat(path).isEqualTo("/ephemeral");
		curatorOperations.deleteNode("/ephemeral");
	}

	@Test
	void createPersistentSequentialNode() {
		String path = curatorOperations.createTypeNode(CreateMode.PERSISTENT_SEQUENTIAL, "/seq",
				"data".getBytes(StandardCharsets.UTF_8));
		assertThat(path).startsWith("/seq");
	}

	// --- createTypeSeqNode ---

	@Test
	void createTypeSeqNodeWithProtection() {
		String path = curatorOperations.createTypeSeqNode(CreateMode.EPHEMERAL_SEQUENTIAL, "/seqNode",
				"data".getBytes(StandardCharsets.UTF_8));
		assertThat(path).contains("seqNode");
	}

	// --- setData ---

	@Test
	void setDataUpdatesNodeContent() {
		curatorOperations.createNode("/setData", "original".getBytes(StandardCharsets.UTF_8));
		Stat stat = curatorOperations.setData("/setData", "updated".getBytes(StandardCharsets.UTF_8));
		assertThat(stat).isNotNull();
		assertThat(curatorOperations.getNode("/setData")).isEqualTo("updated".getBytes(StandardCharsets.UTF_8));
		curatorOperations.deleteNode("/setData");
	}

	// --- setDataAsync ---

	@Test
	void setDataAsyncUpdatesNodeContent() {
		curatorOperations.createNode("/asyncData", "original".getBytes(StandardCharsets.UTF_8));
		curatorOperations.setDataAsync("/asyncData", "updated".getBytes(StandardCharsets.UTF_8), (client, event) -> {
			if (CuratorEventType.SET_DATA.equals(event.getType())) {
				assertThat(event.getPath()).isEqualTo("/asyncData");
			}
		});
		assertThat(curatorOperations.getNode("/asyncData")).isEqualTo("updated".getBytes(StandardCharsets.UTF_8));
		curatorOperations.deleteNode("/asyncData");
	}

	// --- watchedGetChildren ---

	@Test
	void watchedGetChildrenReturnsChildNodes() {
		for (int i = 0; i < 3; i++) {
			curatorOperations.createNode("/parent/" + i, "data".getBytes(StandardCharsets.UTF_8));
		}
		List<String> children = curatorOperations.watchedGetChildren("/parent");
		assertThat(children).containsExactlyInAnyOrder("0", "1", "2");
		curatorOperations.deleteNode("/parent");
	}

	@Test
	void watchedGetChildrenWithWatcher() {
		for (int i = 0; i < 3; i++) {
			curatorOperations.createNode("/watched/" + i, "data".getBytes(StandardCharsets.UTF_8));
		}
		List<String> children = curatorOperations.watchedGetChildren("/watched", event -> {
		});
		assertThat(children).containsExactlyInAnyOrder("0", "1", "2");
		curatorOperations.deleteNode("/watched");
	}

	// --- getLock ---

	@Test
	void getReentrantLock() throws Exception {
		InterProcessLock lock = curatorOperations.getLock("/lockReentrant", ZkLockType.REENTRANT);
		assertThat(lock).isInstanceOf(InterProcessMutex.class);
		assertThat(lock.acquire(3, TimeUnit.SECONDS)).isTrue();
		lock.release();
		curatorOperations.deleteNode("/lockReentrant");
	}

	@Test
	void getNonReentrantLock() throws Exception {
		InterProcessLock lock = curatorOperations.getLock("/lockNonReentrant", ZkLockType.NON_REENTRANT);
		assertThat(lock).isInstanceOf(InterProcessSemaphoreMutex.class);
		assertThat(lock.acquire(3, TimeUnit.SECONDS)).isTrue();
		lock.release();
		curatorOperations.deleteNode("/lockNonReentrant");
	}

	@Test
	void getReadLock() throws Exception {
		InterProcessLock lock = curatorOperations.getLock("/lockRead", ZkLockType.READ);
		assertThat(lock).isInstanceOf(InterProcessReadWriteLock.ReadLock.class);
		assertThat(lock.acquire(3, TimeUnit.SECONDS)).isTrue();
		lock.release();
		curatorOperations.deleteNode("/lockRead");
	}

	@Test
	void getWriteLock() throws Exception {
		InterProcessLock lock = curatorOperations.getLock("/lockWrite", ZkLockType.WRITE);
		assertThat(lock).isInstanceOf(InterProcessReadWriteLock.WriteLock.class);
		assertThat(lock.acquire(3, TimeUnit.SECONDS)).isTrue();
		lock.release();
		curatorOperations.deleteNode("/lockWrite");
	}

	// --- getDistributedId ---

	@Test
	void getDistributedIdReturnsNonEmptyId() {
		curatorOperations.createNode("/distId", "data".getBytes(StandardCharsets.UTF_8));
		String id = curatorOperations.getDistributedId("/distId", "data".getBytes(StandardCharsets.UTF_8));
		assertThat(id).isNotNull().isNotEmpty();
		curatorOperations.deleteNode("/distId");
	}

}
