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

import com.livk.context.lock.DistributedLock;
import com.livk.context.lock.LockType;
import com.livk.context.lock.exception.LockException;
import com.livk.context.lock.exception.UnSupportLockException;
import org.springframework.util.Assert;

/**
 * @param <T> the type parameter
 * @author livk
 */
public abstract class AbstractLockSupport<T> implements DistributedLock {

	/**
	 * The Thread local.
	 */
	protected final ThreadLocal<T> threadLocal = new ThreadLocal<>();

	@Override
	public LockSpec lock(String key) {
		Assert.hasText(key, "Lock key must not be empty");
		return new DefaultLockSpec(key);
	}

	@Override
	public final void unlock() {
		T lock = threadLocal.get();
		if (lock != null && isLocked(lock) && unlock(lock)) {
			threadLocal.remove();
		}
	}

	/**
	 * Gets lock.
	 * @param type the type
	 * @param key the key
	 * @return the lock
	 */
	protected abstract T getLock(LockType type, String key);

	/**
	 * Unlock.
	 * @param lock the lock
	 * @return the boolean
	 */
	protected abstract boolean unlock(T lock);

	/**
	 * Try lock async boolean.
	 * @param lock the lock
	 * @param leaseTime the lease time
	 * @param waitTime the wait time
	 * @return the boolean
	 * @throws LockException the exception
	 */
	protected boolean tryLockAsync(T lock, long leaseTime, long waitTime) throws LockException {
		throw new UnSupportLockException("Async lock of " + this.getClass().getSimpleName() + " isn't support");
	}

	/**
	 * Try lock boolean.
	 * @param lock the lock
	 * @param leaseTime the lease time
	 * @param waitTime the wait time
	 * @return the boolean
	 * @throws LockException the exception
	 */
	protected abstract boolean tryLock(T lock, long leaseTime, long waitTime) throws LockException;

	/**
	 * Lock async.
	 * @param lock the lock
	 * @throws LockException the exception
	 */
	protected void doLockAsync(T lock) throws LockException {
		throw new UnSupportLockException("Async lock of " + this.getClass().getSimpleName() + " isn't support");
	}

	/**
	 * Perform the actual blocking lock acquisition.
	 * @param lock the lock
	 * @throws LockException the exception
	 */
	protected abstract void doLock(T lock) throws LockException;

	/**
	 * Is locked boolean.
	 * @param lock the lock
	 * @return the boolean
	 */
	protected abstract boolean isLocked(T lock);

	/**
	 * Support async boolean.
	 * @return the boolean
	 */
	protected boolean supportAsync() {
		return false;
	}

	private final class DefaultLockSpec implements LockSpec {

		private final String key;

		private LockType type = LockType.LOCK;

		private long leaseTime = -1;

		private long waitTime = 3;

		private boolean async = false;

		private DefaultLockSpec(String key) {
			this.key = key;
		}

		@Override
		public LockSpec type(LockType type) {
			Assert.notNull(type, "LockType must not be null");
			this.type = type;
			return this;
		}

		@Override
		public LockSpec leaseTime(long leaseTime) {
			this.leaseTime = leaseTime;
			return this;
		}

		@Override
		public LockSpec waitTime(long waitTime) {
			this.waitTime = waitTime;
			return this;
		}

		@Override
		public LockSpec async(boolean async) {
			this.async = async;
			return this;
		}

		@Override
		public boolean tryLock() {
			T lock = getLock(this.type, this.key);
			try {
				boolean isLocked = supportAsync() && this.async ? tryLockAsync(lock, this.leaseTime, this.waitTime)
						: AbstractLockSupport.this.tryLock(lock, this.leaseTime, this.waitTime);
				if (isLocked) {
					threadLocal.set(lock);
				}
				return isLocked;
			}
			catch (LockException ex) {
				threadLocal.remove();
				throw ex;
			}
		}

		@Override
		public void lock() {
			T lock = getLock(this.type, this.key);
			try {
				if (supportAsync() && this.async) {
					doLockAsync(lock);
				}
				else {
					doLock(lock);
				}
				threadLocal.set(lock);
			}
			catch (LockException ex) {
				threadLocal.remove();
				throw ex;
			}
		}

	}

}
