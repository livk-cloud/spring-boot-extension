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
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Redisson-based distributed lock factory implementation.
 * <p>
 * Uses {@link RedissonClient} to provide distributed lock functionality, supporting all
 * {@link LockType} values and asynchronous lock operations.
 * <p>
 * Lock type to Redisson API mapping:
 * <ul>
 * <li>{@link LockType#LOCK} - {@link RedissonClient#getLock(String)} reentrant lock</li>
 * <li>{@link LockType#FAIR} - {@link RedissonClient#getFairLock(String)} fair lock</li>
 * <li>{@link LockType#READ} - {@code getReadWriteLock(key).readLock()} read lock</li>
 * <li>{@link LockType#WRITE} - {@code getReadWriteLock(key).writeLock()} write lock</li>
 * </ul>
 *
 * @author livk
 * @see AbstractLockSupport
 * @see RedissonClient
 */
@Slf4j
@RequiredArgsConstructor
public class RedissonLockFactory extends AbstractLockSupport<RLock> {

	/**
	 * The Redisson client instance used to create various distributed locks.
	 */
	private final RedissonClient redissonClient;

	/**
	 * {@inheritDoc}
	 * <p>
	 * Obtains the corresponding {@link RLock} instance from {@link RedissonClient} based
	 * on lock type.
	 */
	@Override
	protected RLock getLock(LockType type, String key) {
		return switch (type) {
			case LOCK -> this.redissonClient.getLock(key);
			case FAIR -> this.redissonClient.getFairLock(key);
			case READ -> this.redissonClient.getReadWriteLock(key).readLock();
			case WRITE -> this.redissonClient.getReadWriteLock(key).writeLock();
		};
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Invokes {@link RLock#unlock()} to release the lock and verifies successful release.
	 */
	@Override
	protected boolean unlock(RLock lock) {
		lock.unlock();
		return !isLocked(lock);
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Uses {@link RLock#tryLockAsync(long, long, TimeUnit)} to asynchronously attempt
	 * lock acquisition.
	 */
	@Override
	protected boolean tryLockAsync(RLock lock, long leaseTime, long waitTime) throws LockException {
		return doFuture(lock.tryLockAsync(waitTime, leaseTime, TimeUnit.SECONDS));
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Uses {@link RLock#tryLock(long, long, TimeUnit)} to synchronously attempt lock
	 * acquisition.
	 */
	@Override
	protected boolean tryLock(RLock lock, long leaseTime, long waitTime) throws LockException {
		return doCallable(() -> lock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS));
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Uses the Redisson async API for blocking lock acquisition. If
	 * {@code leaseTime > 0}, sets auto-release time; otherwise uses the watchdog
	 * mechanism for auto-renewal.
	 */
	@Override
	protected void doLockAsync(RLock lock, long leaseTime) throws LockException {
		if (leaseTime > 0) {
			doFuture(lock.lockAsync(leaseTime, TimeUnit.SECONDS));
		}
		else {
			doFuture(lock.lockAsync());
		}
	}

	/**
	 * Execute a {@link Callable} that may throw checked exceptions, converting them to
	 * {@link LockException}.
	 * <p>
	 * If an {@link InterruptedException} is caught, the thread interrupt status is
	 * restored.
	 * @param <V> the return type
	 * @param callable the operation to execute
	 * @return the result of the operation
	 * @throws LockException if an exception occurs during execution
	 */
	private <V> V doCallable(Callable<V> callable) {
		try {
			return callable.call();
		}
		catch (InterruptedException ie) {
			log.warn("interrupted", ie);
			Thread.currentThread().interrupt();
			throw new LockException(ie);
		}
		catch (Exception ex) {
			throw new LockException(ex);
		}
	}

	/**
	 * Wait for a {@link Future} to complete and return the result, converting exceptions
	 * to {@link LockException}.
	 * @param <V> the return type
	 * @param future the async operation to wait for
	 * @return the result of the async operation
	 * @throws LockException if an exception occurs during waiting
	 */
	private <V> V doFuture(Future<V> future) {
		return doCallable(future::get);
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Uses the Redisson synchronous API for blocking lock acquisition. If
	 * {@code leaseTime > 0}, sets auto-release time; otherwise uses the watchdog
	 * mechanism for auto-renewal.
	 */
	@Override
	protected void doLock(RLock lock, long leaseTime) {
		if (leaseTime > 0) {
			lock.lock(leaseTime, TimeUnit.SECONDS);
		}
		else {
			lock.lock();
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Checks both {@link RLock#isLocked()} and {@link RLock#isHeldByCurrentThread()} to
	 * determine if the lock is held by the current thread.
	 */
	@Override
	protected boolean isLocked(RLock lock) {
		return lock.isLocked() && lock.isHeldByCurrentThread();
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Redisson natively supports async lock operations, returns {@code true}.
	 */
	@Override
	protected boolean supportAsync() {
		return true;
	}

}
