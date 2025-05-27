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

package com.livk.context.curator.lock;

import lombok.RequiredArgsConstructor;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.framework.recipes.locks.InterProcessMultiLock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.locks.InterProcessReadWriteLock;
import org.apache.curator.framework.recipes.locks.InterProcessSemaphoreMutex;

import java.util.List;

/**
 * <p>
 * ZkLockType
 * </p>
 *
 * @author livk
 */
@RequiredArgsConstructor
public enum ZkLockType implements LockProcess {

	/**
	 * 可重入锁
	 */
	REENTRANT(new ReentrantLockProcess()),
	/**
	 * 不可重入锁
	 */
	NON_REENTRANT(new NonReentrantLockProcess()),
	/**
	 * 读锁
	 */
	READ(new ReadLockProcess()),
	/**
	 * 写锁
	 */
	WRITE(new WriteLockProcess());

	private final LockProcess delegate;

	@Override
	public InterProcessLock getLock(CuratorFramework framework, String path) {
		return delegate.getLock(framework, path);
	}

	public static InterProcessMultiLock multiLock(List<InterProcessLock> locks) {
		return new InterProcessMultiLock(locks);
	}

	private static class ReentrantLockProcess implements LockProcess {

		@Override
		public InterProcessLock getLock(CuratorFramework framework, String path) {
			return new InterProcessMutex(framework, path);
		}

	}

	private static class NonReentrantLockProcess implements LockProcess {

		@Override
		public InterProcessLock getLock(CuratorFramework framework, String path) {
			return new InterProcessSemaphoreMutex(framework, path);
		}

	}

	private static class ReadLockProcess implements LockProcess {

		@Override
		public InterProcessLock getLock(CuratorFramework framework, String path) {
			return new InterProcessReadWriteLock(framework, path).readLock();
		}

	}

	private static class WriteLockProcess implements LockProcess {

		@Override
		public InterProcessLock getLock(CuratorFramework framework, String path) {
			return new InterProcessReadWriteLock(framework, path).writeLock();
		}

	}

}
