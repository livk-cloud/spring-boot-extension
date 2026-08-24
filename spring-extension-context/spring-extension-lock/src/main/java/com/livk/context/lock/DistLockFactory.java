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
 * The distributed lock factory interface defining the core API for creating and
 * configuring distributed locks.
 * <p>
 * This interface uses a fluent API design. Obtain a {@link SpecLock} specification object
 * via {@link #lock(String)} or {@link #lock(String, LockType)}, then configure lock
 * parameters (lease time, wait time, async mode) through chained calls, and finally
 * perform lock/unlock operations.
 * <p>
 * Usage example: <pre>{@code
 * DistLockFactory factory = ...;
 * DistLockFactory.SpecLock specLock = factory.lock("order:123", LockType.LOCK)
 *     .leaseTime(30)
 *     .waitTime(5);
 * if (specLock.tryLock()) {
 *     try {
 *         // execute business logic
 *     } finally {
 *         specLock.unlock();
 *     }
 * }
 * }</pre>
 * <p>
 * Implementations can control priority among multiple factory instances via
 * {@link Ordered#getOrder()}, with a default priority of 0.
 *
 * @author livk
 * @see SpecLock
 * @see DistributedLock
 * @see LockType
 * @see org.springframework.core.Ordered
 */
public interface DistLockFactory extends Ordered {

	/**
	 * Create a lock spec with the default lock type ({@link LockType#LOCK}).
	 * @param key the unique lock key, must not be empty
	 * @return a {@link SpecLock} instance for further configuration and execution
	 */
	SpecLock lock(String key);

	/**
	 * Create a lock spec with the specified lock type.
	 * @param key the unique lock key, must not be empty
	 * @param type the lock type, must not be {@code null}
	 * @return a {@link SpecLock} instance for further configuration and execution
	 */
	SpecLock lock(String key, LockType type);

	/**
	 * Return the priority order of this factory, default is 0.
	 * <p>
	 * When multiple {@code DistLockFactory} implementations exist, the
	 * {@link com.livk.context.lock.intercept.DistributedLockInterceptor
	 * DistributedLockInterceptor} selects the one with the highest priority (lowest
	 * value).
	 * @return the priority order value
	 */
	@Override
	default int getOrder() {
		return 0;
	}

	/**
	 * The distributed lock operations interface defining basic lock and unlock
	 * operations.
	 *
	 * @see SpecLock
	 */
	interface DistributedLock {

		/**
		 * Try to acquire the lock without blocking indefinitely.
		 * <p>
		 * Attempts to acquire the lock within the configured wait time, returns
		 * {@code false} on timeout.
		 * @return {@code true} if the lock was acquired, {@code false} otherwise
		 */
		boolean tryLock();

		/**
		 * Acquire the lock in a blocking manner until it becomes available.
		 * <p>
		 * If the lock is unavailable, the current thread blocks until acquisition
		 * succeeds.
		 * @throws com.livk.context.lock.exception.LockException if an exception occurs
		 * during lock acquisition
		 */
		void lock();

		/**
		 * Release the currently held lock.
		 * <p>
		 * Only releases if the current thread holds the lock, otherwise no operation is
		 * performed.
		 */
		void unlock();

	}

	/**
	 * The lock specification configuration interface extending {@link DistributedLock}
	 * with fluent parameter configuration.
	 * <p>
	 * After configuring lock parameters via chained calls, invoke {@link #tryLock()} or
	 * {@link #lock()} to perform the lock operation.
	 *
	 * @see DistributedLock
	 */
	interface SpecLock extends DistributedLock {

		/**
		 * Set the lease time in seconds after which the lock is auto-released.
		 * @param leaseTime the lease time in seconds, {@code -1} for no expiration
		 * @return this spec instance for chaining
		 */
		SpecLock leaseTime(long leaseTime);

		/**
		 * Set the maximum wait time in seconds for lock acquisition.
		 * <p>
		 * In {@link #tryLock()}, returns {@code false} if the lock cannot be acquired
		 * within the specified time.
		 * @param waitTime the wait time in seconds
		 * @return this spec instance for chaining
		 */
		SpecLock waitTime(long waitTime);

		/**
		 * Enable asynchronous lock mode.
		 * <p>
		 * When enabled, the lock operation executes in a non-blocking manner. Note that
		 * not all implementations support async mode; unsupported implementations throw
		 * {@link com.livk.context.lock.exception.UnSupportLockException}.
		 * @return this spec instance for chaining
		 */
		SpecLock async();

	}

}
