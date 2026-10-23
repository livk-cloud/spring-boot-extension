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

/**
 * Distributed lock type enum defining the supported lock modes.
 * <p>
 * Different lock types are suited for different concurrency control scenarios:
 * <ul>
 * <li>{@link #LOCK} - Reentrant mutual exclusion lock for most mutual exclusion
 * scenarios</li>
 * <li>{@link #FAIR} - Fair lock that acquires in request order, avoiding thread
 * starvation</li>
 * <li>{@link #READ} - Read lock allowing concurrent read operations</li>
 * <li>{@link #WRITE} - Write lock for exclusive write access, mutually exclusive with
 * reads</li>
 * </ul>
 *
 * @author livk
 * @see com.livk.context.lock.annotation.DistLock
 * @see com.livk.context.lock.DistLockFactory
 */
public enum LockType {

	/**
	 * Reentrant mutual exclusion lock.
	 * <p>
	 * Only one thread can hold this lock at any given time; the same thread can acquire
	 * it multiple times (reentrant).
	 */
	LOCK,

	/**
	 * Fair lock.
	 * <p>
	 * Allocates the lock in request order, ensuring the longest-waiting thread acquires
	 * the lock first, preventing thread starvation.
	 */
	FAIR,

	/**
	 * Read lock (shared lock).
	 * <p>
	 * Allows multiple threads to concurrently acquire read locks for parallel reading,
	 * but is mutually exclusive with write locks.
	 */
	READ,

	/**
	 * Write lock (exclusive lock).
	 * <p>
	 * An exclusive lock; while a write lock is held, no other thread can acquire read or
	 * write locks.
	 */
	WRITE

}
