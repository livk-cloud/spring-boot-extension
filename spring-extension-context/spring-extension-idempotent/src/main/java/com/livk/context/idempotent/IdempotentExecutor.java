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

import org.jspecify.annotations.Nullable;
import org.springframework.core.Ordered;

import java.time.Duration;

/**
 * Storage SPI for idempotent tokens.
 * <p>
 * A non-null return from {@link #tryAcquire(String, Duration)} means the caller may
 * proceed and owns the returned token. The key stays occupied until {@code ttl} elapses,
 * or until {@link #release(String, String)} is called with the same ownership token after
 * a failed invocation.
 *
 * @author livk
 */
public interface IdempotentExecutor extends Ordered {

	/**
	 * Try to occupy the idempotent key with a unique ownership token.
	 * @param key the business key
	 * @param ttl how long a successful occupation lasts
	 * @return the ownership token when acquired, or {@code null} when the key is already
	 * occupied
	 */
	@Nullable String tryAcquire(String key, Duration ttl);

	/**
	 * Release a previously acquired token so the operation can be retried.
	 * <p>
	 * Implementations must delete the key only when {@code token} still owns it, so a
	 * stale failure cannot drop a newer acquisition after TTL expiry.
	 * @param key the business key
	 * @param token the ownership token returned by {@link #tryAcquire(String, Duration)}
	 */
	void release(String key, String token);

	@Override
	default int getOrder() {
		return 0;
	}

}
