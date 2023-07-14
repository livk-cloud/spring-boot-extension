/*
 * Copyright 2021 spring-boot-extension the original author or authors.
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
 *
 */

package com.livk.autoconfigure.lock.support;

import com.livk.autoconfigure.lock.constant.LockType;
import com.livk.autoconfigure.lock.exception.LockException;
import com.livk.autoconfigure.lock.exception.UnSupportLockException;
import com.livk.commons.util.Pair;

/**
 * <p>
 * AbstractLock
 * </p>
 *
 * @param <T> the type parameter
 * @author livk
 */
public abstract class AbstractLockSupport<T> implements DistributedLock {
	/**
	 * The Thread local.
	 */
	protected final ThreadLocal<Pair<String, T>> threadLocal = new InheritableThreadLocal<>();

	@Override
	public boolean tryLock(LockType type, String key, long leaseTime, long waitTime, boolean async) {
		T lock = getLock(type, key);
		try {
			boolean isLocked = supportAsync() && async ?
				tryLockAsync(lock, leaseTime, waitTime) :
				tryLock(lock, waitTime, leaseTime);
			if (isLocked) {
				threadLocal.set(Pair.of(key, lock));
			}
			return isLocked;
		} catch (Exception e) {
			threadLocal.remove();
			throw new LockException(e);
		}
	}

	@Override
	public void lock(LockType type, String key, boolean async) {
		T lock = getLock(type, key);
		try {
			if (supportAsync() && async) {
				lockAsync(lock);
			} else {
				lock(lock);
			}
			threadLocal.set(Pair.of(key, lock));
		} catch (Exception e) {
			threadLocal.remove();
			throw new LockException(e);
		}
	}

	@Override
	public void unlock() {
		Pair<String, T> pair = threadLocal.get();
		if (pair != null) {
			String key = pair.key();
			T lock = pair.value();
			if (isLocked(lock) && unlock(key, lock)) {
				threadLocal.remove();
			}
		}
	}


	/**
	 * Gets lock.
	 *
	 * @param type the type
	 * @param key  the key
	 * @return the lock
	 */
	protected abstract T getLock(LockType type, String key);

	/**
	 * Unlock.
	 *
	 * @param key  the key
	 * @param lock the lock
	 * @return the boolean
	 */
	protected abstract boolean unlock(String key, T lock);

	/**
	 * Try lock async boolean.
	 *
	 * @param lock      the lock
	 * @param leaseTime the lease time
	 * @param waitTime  the wait time
	 * @return the boolean
	 * @throws Exception the exception
	 */
	protected boolean tryLockAsync(T lock, long leaseTime, long waitTime) throws Exception {
		throw new UnSupportLockException("Async lock of " + this.getClass().getSimpleName() + " isn't support");
	}

	/**
	 * Try lock boolean.
	 *
	 * @param lock      the lock
	 * @param leaseTime the lease time
	 * @param waitTime  the wait time
	 * @return the boolean
	 * @throws Exception the exception
	 */
	protected abstract boolean tryLock(T lock, long leaseTime, long waitTime) throws Exception;

	/**
	 * Lock async.
	 *
	 * @param lock the lock
	 * @throws Exception the exception
	 */
	protected void lockAsync(T lock) throws Exception {
		throw new UnSupportLockException("Async lock of " + this.getClass().getSimpleName() + " isn't support");
	}

	/**
	 * Lock.
	 *
	 * @param lock the lock
	 * @throws Exception the exception
	 */
	protected abstract void lock(T lock) throws Exception;

	/**
	 * Is locked boolean.
	 *
	 * @param lock the lock
	 * @return the boolean
	 */
	protected abstract boolean isLocked(T lock);

	/**
	 * Support async boolean.
	 *
	 * @return the boolean
	 */
	protected boolean supportAsync() {
		return false;
	}
}
