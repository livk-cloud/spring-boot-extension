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
import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.locks.InterProcessReadWriteLock;
import org.junit.jupiter.api.MethodOrderer;
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
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
@SpringJUnitConfig(CuratorConfig.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Testcontainers(disabledWithoutDocker = true, parallel = true)
class CuratorTemplateTests {

	@Container
	@ServiceConnection
	static final ZookeeperContainer zookeeper = new ZookeeperContainer();

	@DynamicPropertySource
	static void properties(DynamicPropertyRegistry registry) {
		registry.add("curator.connectString",
				() -> String.format("%s:%s", zookeeper.getHost(), zookeeper.getFirstMappedPort()));
	}

	@Autowired
	CuratorTemplate template;

	@Test
	void setDataAsync() {
		template.createNode("/node", "data".getBytes(StandardCharsets.UTF_8));
		assertThat(template.getNode("/node")).containsExactly("data".getBytes());
		template.setDataAsync("/node", "setData".getBytes(StandardCharsets.UTF_8));
		assertThat(template.getNode("/node")).containsExactly("setData".getBytes());
		template.deleteNode("/node");
	}

	@Test
	void getLock() throws Exception {
		InterProcessLock lock = template.getLock("/node/lock", ZkLockType.REENTRANT);
		assertThat(lock.getClass()).isEqualTo(InterProcessMutex.class);
		assertThat(lock.acquire(3, TimeUnit.SECONDS)).isTrue();
		lock.release();
		template.deleteNode("/node");

		InterProcessLock read = template.getLock("/node/lock", ZkLockType.READ);
		assertThat(read.getClass()).isEqualTo(InterProcessReadWriteLock.ReadLock.class);
		assertThat(read.acquire(3, TimeUnit.SECONDS)).isTrue();
		read.release();
		template.deleteNode("/node");

		InterProcessLock write = template.getLock("/node/lock", ZkLockType.WRITE);
		assertThat(write.getClass()).isEqualTo(InterProcessReadWriteLock.WriteLock.class);
		assertThat(write.acquire(3, TimeUnit.SECONDS)).isTrue();
		write.release();

		template.deleteNode("/node");
	}

}
