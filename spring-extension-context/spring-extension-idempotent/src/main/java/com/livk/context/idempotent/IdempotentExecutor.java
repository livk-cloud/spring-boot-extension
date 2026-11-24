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

package com.livk.context.idempotent;

import org.springframework.core.Ordered;

import java.time.Duration;

/**
 * Storage SPI for idempotent tokens.
 * <p>
 * A successful {@link #tryAcquire(String, Duration)} means the caller may proceed. The
 * token stays occupied until {@code ttl} elapses, or until {@link #release(String)} is
 * called after a failed invocation.
 *
 * @author livk
 */
public interface IdempotentExecutor extends Ordered {

	/**
	 * Try to occupy the idempotent key.
	 * @param key the business key
	 * @param ttl how long a successful occupation lasts
	 * @return {@code true} when this caller acquired the token, {@code false} when it is
	 * already occupied
	 */
	boolean tryAcquire(String key, Duration ttl);

	/**
	 * Release a previously acquired token so the operation can be retried.
	 * @param key the business key
	 */
	void release(String key);

	@Override
	default int getOrder() {
		return 0;
	}

}
