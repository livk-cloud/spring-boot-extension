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

package com.livk.context.curator.lock;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.framework.recipes.locks.InterProcessMultiLock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.locks.InterProcessReadWriteLock;
import org.apache.curator.framework.recipes.locks.InterProcessSemaphoreMutex;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

/**
 * @author livk
 */
class ZkLockTypeTests {

	final CuratorFramework framework;

	ZkLockTypeTests() {
		framework = mock(CuratorFramework.class, org.mockito.Answers.RETURNS_DEEP_STUBS);
		given(framework.getNamespace()).willReturn("");
	}

	@Test
	void reentrantReturnsInterProcessMutex() {
		InterProcessLock lock = ZkLockType.REENTRANT.getLock(framework, "/lock");
		assertThat(lock).isInstanceOf(InterProcessMutex.class);
	}

	@Test
	void nonReentrantReturnsInterProcessSemaphoreMutex() {
		InterProcessLock lock = ZkLockType.NON_REENTRANT.getLock(framework, "/lock");
		assertThat(lock).isInstanceOf(InterProcessSemaphoreMutex.class);
	}

	@Test
	void readReturnsReadLock() {
		InterProcessLock lock = ZkLockType.READ.getLock(framework, "/lock");
		assertThat(lock).isInstanceOf(InterProcessReadWriteLock.ReadLock.class);
	}

	@Test
	void writeReturnsWriteLock() {
		InterProcessLock lock = ZkLockType.WRITE.getLock(framework, "/lock");
		assertThat(lock).isInstanceOf(InterProcessReadWriteLock.WriteLock.class);
	}

	@Test
	void multiLockReturnsInterProcessMultiLock() {
		InterProcessLock lock1 = ZkLockType.REENTRANT.getLock(framework, "/lock1");
		InterProcessLock lock2 = ZkLockType.REENTRANT.getLock(framework, "/lock2");
		InterProcessMultiLock multiLock = ZkLockType.multiLock(List.of(lock1, lock2));
		assertThat(multiLock).isNotNull().isInstanceOf(InterProcessMultiLock.class);
	}

	@Test
	void allEnumValuesReturnNonNullLock() {
		for (ZkLockType type : ZkLockType.values()) {
			assertThat(type.getLock(framework, "/lock")).isNotNull();
		}
	}

	@Test
	void enumValuesCount() {
		assertThat(ZkLockType.values()).hasSize(4);
	}

}
