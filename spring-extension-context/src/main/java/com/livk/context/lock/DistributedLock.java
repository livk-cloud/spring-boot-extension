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

package com.livk.context.lock;

import org.springframework.core.Ordered;

/**
 * <p>
 * DistributedLock
 * </p>
 *
 * @author livk
 */
public interface DistributedLock extends Ordered {

	/**
	 * Try lock boolean.
	 * @param type the type
	 * @param key the key
	 * @param leaseTime the lease time
	 * @param waitTime the wait time
	 * @param async the async
	 * @return the boolean
	 */
	boolean tryLock(LockType type, String key, long leaseTime, long waitTime, boolean async);

	/**
	 * Lock.
	 * @param type the type
	 * @param key the key
	 * @param async the async
	 */
	void lock(LockType type, String key, boolean async);

	/**
	 * Unlock.
	 */
	void unlock();

	/**
	 * Scope lock scope.
	 * @return the lock scope
	 */
	LockScope scope();

	@Override
	default int getOrder() {
		return 0;
	}

}
