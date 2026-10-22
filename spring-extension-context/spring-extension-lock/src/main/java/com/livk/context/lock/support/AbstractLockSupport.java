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

import com.livk.context.lock.DistLockFactory;
import com.livk.context.lock.LockType;
import com.livk.context.lock.exception.LockException;
import com.livk.context.lock.exception.UnSupportLockException;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Abstract base class providing a skeletal {@link DistLockFactory} implementation.
 * <p>
 * This class uses the Template Method pattern to define the common lock operation flow;
 * subclasses only need to implement the concrete lock acquisition, release, and state
 * checking logic. Per-thread holdings are tracked by {@link ThreadLockContext} keyed by
 * {@link LockType} and business key, so the same thread can nest different keys and
 * re-enter the same key; unlock may use any {@link SpecLock} for that key on the same
 * thread.
 * <p>
 * Subclasses must implement the following abstract methods:
 * <ul>
 * <li>{@link #getLock(LockType, String)} - create a lock object by type and key</li>
 * <li>{@link #unlock(Object)} - release a lock</li>
 * <li>{@link #tryLock(Object, long, long)} - attempt to acquire a lock</li>
 * <li>{@link #doLock(Object, long)} - blocking lock acquisition</li>
 * <li>{@link #isLocked(Object)} - check if a lock is held</li>
 * </ul>
 * <p>
 * To support async lock operations, subclasses may override {@link #supportAsync()},
 * {@link #tryLockAsync}, and {@link #doLockAsync}.
 *
 * @param <T> the type of the underlying lock object, e.g. Redisson's {@code RLock} or
 * Curator's {@code InterProcessLock}
 * @author livk
 * @see DistLockFactory
 * @see RedissonLockFactory
 * @see CuratorLockFactory
 */
public abstract class AbstractLockSupport<T> implements DistLockFactory {

	private final ThreadLockContext<T> lockContext = new ThreadLockContext<>();

	/**
	 * {@inheritDoc}
	 * <p>
	 * Creates a lock spec using the default lock type {@link LockType#LOCK}.
	 */
	@Override
	public SpecLock lock(String key) {
		Assert.hasText(key, "Lock key must not be empty");
		return new DefaultLockSpec(key);
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Creates a lock spec using the specified lock type.
	 */
	@Override
	public SpecLock lock(String key, LockType type) {
		Assert.hasText(key, "Lock key must not be empty");
		Assert.notNull(type, "Lock type must not be null");
		return new DefaultLockSpec(key, type);
	}

	/**
	 * Create the underlying lock object based on lock type and key.
	 * <p>
	 * Subclasses should return the corresponding lock instance for each {@link LockType}.
	 * @param type the lock type
	 * @param key the unique lock key
	 * @return the underlying lock object instance
	 */
	protected abstract T getLock(LockType type, String key);

	/**
	 * Release the specified lock object.
	 * @param lock the lock object to release
	 * @return {@code true} if released successfully, {@code false} otherwise
	 */
	protected abstract boolean unlock(T lock);

	/**
	 * Attempt to acquire the lock asynchronously.
	 * <p>
	 * The default implementation throws {@link UnSupportLockException} indicating async
	 * is not supported. Subclasses supporting async should override this method and also
	 * override {@link #supportAsync()} to return {@code true}.
	 * @param lock the underlying lock object
	 * @param leaseTime the lease time in seconds, {@code -1} for no expiration
	 * @param waitTime the maximum wait time in seconds
	 * @return {@code true} if the lock was acquired, {@code false} otherwise
	 * @throws LockException if an exception occurs during lock acquisition
	 * @throws UnSupportLockException if the current implementation does not support async
	 */
	protected boolean tryLockAsync(T lock, long leaseTime, long waitTime) throws LockException {
		throw new UnSupportLockException("Async lock of " + this.getClass().getSimpleName() + " isn't support");
	}

	/**
	 * Synchronously attempt to acquire the lock, blocking within the specified wait time.
	 * @param lock the underlying lock object
	 * @param leaseTime the lease time in seconds, {@code -1} for no expiration
	 * @param waitTime the maximum wait time in seconds
	 * @return {@code true} if the lock was acquired, {@code false} otherwise
	 * @throws LockException if an exception occurs during lock acquisition
	 */
	protected abstract boolean tryLock(T lock, long leaseTime, long waitTime) throws LockException;

	/**
	 * Acquire the lock asynchronously in a blocking manner.
	 * <p>
	 * The default implementation throws {@link UnSupportLockException} indicating async
	 * is not supported. Subclasses supporting async should override this method.
	 * @param lock the underlying lock object
	 * @param leaseTime the lease time in seconds, {@code -1} for no expiration
	 * @throws LockException if an exception occurs during lock acquisition
	 * @throws UnSupportLockException if the current implementation does not support async
	 */
	protected void doLockAsync(T lock, long leaseTime) throws LockException {
		throw new UnSupportLockException("Async lock of " + this.getClass().getSimpleName() + " isn't support");
	}

	/**
	 * Synchronously acquire the lock, blocking until it becomes available.
	 * @param lock the underlying lock object
	 * @param leaseTime the lease time in seconds, {@code -1} for no expiration
	 * @throws LockException if an exception occurs during lock acquisition
	 */
	protected abstract void doLock(T lock, long leaseTime) throws LockException;

	/**
	 * Check whether the specified lock object is held by the current thread.
	 * @param lock the underlying lock object
	 * @return {@code true} if the lock is held by the current thread, {@code false}
	 * otherwise
	 */
	protected abstract boolean isLocked(T lock);

	/**
	 * Determine whether the current implementation supports async lock operations.
	 * <p>
	 * Returns {@code false} by default. Subclasses supporting async should override this
	 * to return {@code true}, and also implement {@link #tryLockAsync} and
	 * {@link #doLockAsync}.
	 * @return {@code true} if async operations are supported, {@code false} otherwise
	 */
	protected boolean supportAsync() {
		return false;
	}

	/**
	 * Per-thread lock holdings keyed by {@link LockType} and business key.
	 * <p>
	 * Encapsulates {@link ThreadLocal} lifecycle, re-entrant hold counts, and cleanup
	 * when the current thread no longer holds any locks.
	 *
	 * @param <L> the underlying lock object type
	 */
	private static final class ThreadLockContext<L> {

		private final ThreadLocal<Map<LockKey, LockHolder<L>>> threadLocks = ThreadLocal.withInitial(HashMap::new);

		private LockKey key(LockType type, String key) {
			return new LockKey(type, key);
		}

		private L get(LockKey key) {
			LockHolder<L> holder = this.threadLocks.get().get(key);
			return (holder != null) ? holder.lock : null;
		}

		private void hold(LockKey key, L lock) {
			Map<LockKey, LockHolder<L>> locks = this.threadLocks.get();
			LockHolder<L> holder = locks.get(key);
			if (holder == null) {
				locks.put(key, new LockHolder<>(lock));
			}
			else {
				holder.holdCount++;
			}
		}

		private void release(LockKey key, Predicate<L> isLocked, Predicate<L> unlock) {
			Map<LockKey, LockHolder<L>> locks = this.threadLocks.get();
			LockHolder<L> holder = locks.get(key);
			if (holder == null) {
				return;
			}
			if (!isLocked.test(holder.lock)) {
				locks.remove(key);
			}
			else if (unlock.test(holder.lock)) {
				if (--holder.holdCount == 0) {
					locks.remove(key);
				}
			}
			if (locks.isEmpty()) {
				this.threadLocks.remove();
			}
		}

		private record LockKey(LockType type, String key) {
		}

		private static final class LockHolder<L> {

			private final L lock;

			private int holdCount;

			private LockHolder(L lock) {
				this.lock = lock;
				this.holdCount = 1;
			}

		}

	}

	private final class DefaultLockSpec implements DistLockFactory.SpecLock {

		private final String key;

		private final LockType type;

		private long leaseTime = -1;

		private long waitTime = 3;

		private boolean async = false;

		private DefaultLockSpec(String key) {
			this(key, LockType.LOCK);
		}

		private DefaultLockSpec(String key, LockType type) {
			this.key = key;
			this.type = type;
		}

		@Override
		public DistLockFactory.SpecLock leaseTime(long leaseTime) {
			this.leaseTime = leaseTime;
			return this;
		}

		@Override
		public DistLockFactory.SpecLock waitTime(long waitTime) {
			this.waitTime = waitTime;
			return this;
		}

		@Override
		public DistLockFactory.SpecLock async() {
			this.async = true;
			return this;
		}

		@Override
		public boolean tryLock() {
			ThreadLockContext.LockKey lockKey = AbstractLockSupport.this.lockContext.key(this.type, this.key);
			T lock = AbstractLockSupport.this.lockContext.get(lockKey);
			if (lock == null) {
				lock = getLock(this.type, this.key);
			}
			boolean acquired = (supportAsync() && this.async) ? tryLockAsync(lock, this.leaseTime, this.waitTime)
					: AbstractLockSupport.this.tryLock(lock, this.leaseTime, this.waitTime);
			if (acquired) {
				AbstractLockSupport.this.lockContext.hold(lockKey, lock);
			}
			return acquired;
		}

		@Override
		public void lock() {
			ThreadLockContext.LockKey lockKey = AbstractLockSupport.this.lockContext.key(this.type, this.key);
			T lock = AbstractLockSupport.this.lockContext.get(lockKey);
			if (lock == null) {
				lock = getLock(this.type, this.key);
			}
			if (supportAsync() && this.async) {
				doLockAsync(lock, this.leaseTime);
			}
			else {
				doLock(lock, this.leaseTime);
			}
			AbstractLockSupport.this.lockContext.hold(lockKey, lock);
		}

		@Override
		public void unlock() {
			ThreadLockContext.LockKey lockKey = AbstractLockSupport.this.lockContext.key(this.type, this.key);
			AbstractLockSupport.this.lockContext.release(lockKey, AbstractLockSupport.this::isLocked,
					AbstractLockSupport.this::unlock);
		}

	}

}
