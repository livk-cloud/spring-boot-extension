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
import lombok.RequiredArgsConstructor;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.locks.InterProcessReadWriteLock;

import java.util.concurrent.TimeUnit;

/**
 * Apache Curator-based distributed lock factory implementation.
 * <p>
 * Uses {@link CuratorFramework} to provide ZooKeeper-backed distributed locks, supporting
 * reentrant mutex and read-write locks.
 * <p>
 * Lock type to Curator API mapping:
 * <ul>
 * <li>{@link LockType#LOCK} - {@link InterProcessMutex} reentrant mutex lock</li>
 * <li>{@link LockType#FAIR} - {@link InterProcessMutex} (ZooKeeper inherently guarantees
 * fairness)</li>
 * <li>{@link LockType#READ} - {@link InterProcessReadWriteLock#readLock()} read lock</li>
 * <li>{@link LockType#WRITE} - {@link InterProcessReadWriteLock#writeLock()} write
 * lock</li>
 * </ul>
 * <p>
 * Note: This implementation does not support async lock operations
 * ({@link #supportAsync()} returns {@code false}). Lock keys are used as ZooKeeper node
 * paths; if a key does not start with "/", the prefix is added automatically.
 *
 * @author livk
 * @see AbstractLockSupport
 * @see CuratorFramework
 * @see InterProcessLock
 */
@RequiredArgsConstructor
public class CuratorLockFactory extends AbstractLockSupport<InterProcessLock> {

	/**
	 * The Curator framework client instance for ZooKeeper interaction.
	 */
	private final CuratorFramework framework;

	/**
	 * {@inheritDoc}
	 * <p>
	 * Creates the corresponding Curator lock instance based on lock type. If the key does
	 * not start with "/", the prefix is added automatically since ZooKeeper requires node
	 * paths to begin with "/".
	 */
	@Override
	protected InterProcessLock getLock(LockType type, String key) {
		if (!key.startsWith("/")) {
			key = "/".concat(key);
		}
		return switch (type) {
			case LOCK, FAIR -> new InterProcessMutex(this.framework, key);
			case READ -> new InterProcessReadWriteLock(this.framework, key).readLock();
			case WRITE -> new InterProcessReadWriteLock(this.framework, key).writeLock();
		};
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Invokes {@link InterProcessLock#release()} to release the lock.
	 * @throws LockException if an exception occurs during lock release
	 */
	@Override
	protected boolean unlock(InterProcessLock lock) {
		try {
			lock.release();
			return !isLocked(lock);
		}
		catch (Exception ex) {
			throw new LockException(ex);
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Uses {@link InterProcessLock#acquire(long, TimeUnit)} to attempt lock acquisition
	 * within the specified wait time. Note that Curator's acquire does not support the
	 * leaseTime parameter; locks will not auto-release.
	 */
	@Override
	protected boolean tryLock(InterProcessLock lock, long leaseTime, long waitTime) throws LockException {
		try {
			return lock.acquire(waitTime, TimeUnit.SECONDS);
		}
		catch (Exception ex) {
			throw new LockException(ex);
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Uses {@link InterProcessLock#acquire()} to block until the lock is available. Note
	 * that Curator does not support leaseTime auto-release; locks must be explicitly
	 * released.
	 */
	@Override
	protected void doLock(InterProcessLock lock, long leaseTime) throws LockException {
		try {
			lock.acquire();
		}
		catch (Exception ex) {
			throw new LockException(ex);
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Uses {@link InterProcessLock#isAcquiredInThisProcess()} to determine if the lock is
	 * held in the current process.
	 */
	@Override
	protected boolean isLocked(InterProcessLock lock) {
		return lock.isAcquiredInThisProcess();
	}

}
