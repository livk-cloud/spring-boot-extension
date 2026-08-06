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

package com.livk.context.lock;

import org.springframework.core.Ordered;

/**
 * Fluent distributed lock interface that supports multiple lock implementations.
 * <p>
 * Usage example: <pre>{@code
 * DistributedLock distributedLock = ...;
 * boolean acquired = distributedLock.lock("myKey")
 *     .type(LockType.FAIR)
 *     .leaseTime(30)
 *     .waitTime(10)
 *     .tryLock();
 * try {
 *     if (acquired) {
 *         // do business logic
 *     }
 * } finally {
 *     if (acquired) {
 *         distributedLock.unlock();
 *     }
 * }
 * }</pre>
 *
 * @author livk
 */
public interface DistributedLock extends Ordered {

	/**
	 * Start configuring a lock operation for the given key.
	 * @param key the lock key
	 * @return a {@link LockSpec} for further configuration and execution
	 */
	LockSpec lock(String key);

	/**
	 * Unlock the currently held lock.
	 */
	void unlock();

	@Override
	default int getOrder() {
		return 0;
	}

	/**
	 * Specification interface for configuring and executing lock operations.
	 * <p>
	 * All configuration methods are optional and have sensible defaults:
	 * <ul>
	 * <li>{@code type} defaults to {@link LockType#LOCK}</li>
	 * <li>{@code leaseTime} defaults to {@code -1} (no expiration)</li>
	 * <li>{@code waitTime} defaults to {@code 3} seconds</li>
	 * <li>{@code async} defaults to {@code false}</li>
	 * </ul>
	 */
	interface LockSpec {

		/**
		 * Set the lock type.
		 * @param type the lock type
		 * @return this spec for further configuration
		 */
		LockSpec type(LockType type);

		/**
		 * Set the lease time in seconds. After this time the lock will be auto-released.
		 * @param leaseTime the lease time in seconds, {@code -1} for no expiration
		 * @return this spec for further configuration
		 */
		LockSpec leaseTime(long leaseTime);

		/**
		 * Set the maximum time to wait for lock acquisition.
		 * @param waitTime the wait time in seconds
		 * @return this spec for further configuration
		 */
		LockSpec waitTime(long waitTime);

		/**
		 * Set whether the lock operation should be asynchronous.
		 * @param async {@code true} for async lock
		 * @return this spec for further configuration
		 */
		LockSpec async(boolean async);

		/**
		 * Try to acquire the lock with the configured parameters.
		 * @return {@code true} if the lock was acquired, {@code false} otherwise
		 */
		boolean tryLock();

		/**
		 * Acquire the lock, blocking until it is available.
		 */
		void lock();

	}

}
