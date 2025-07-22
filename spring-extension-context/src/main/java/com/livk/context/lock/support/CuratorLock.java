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
 * @author livk
 */
@RequiredArgsConstructor
public class CuratorLock extends AbstractLockSupport<InterProcessLock> {

	private final CuratorFramework framework;

	@Override
	protected InterProcessLock getLock(LockType type, String key) {
		if (!key.startsWith("/")) {
			key = "/".concat(key);
		}
		return switch (type) {
			case LOCK, FAIR -> new InterProcessMutex(framework, key);
			case READ -> new InterProcessReadWriteLock(framework, key).readLock();
			case WRITE -> new InterProcessReadWriteLock(framework, key).writeLock();
		};
	}

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

	@Override
	protected boolean tryLock(InterProcessLock lock, long leaseTime, long waitTime) throws LockException {
		try {
			return lock.acquire(waitTime, TimeUnit.SECONDS);
		}
		catch (Exception ex) {
			throw new LockException(ex);
		}
	}

	@Override
	protected void lock(InterProcessLock lock) throws LockException {
		try {
			lock.acquire();
		}
		catch (Exception ex) {
			throw new LockException(ex);
		}
	}

	@Override
	protected boolean isLocked(InterProcessLock lock) {
		return lock.isAcquiredInThisProcess();
	}

}
