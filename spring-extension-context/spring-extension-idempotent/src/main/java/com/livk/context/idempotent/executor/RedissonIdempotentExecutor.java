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

package com.livk.context.idempotent.executor;

import com.livk.context.idempotent.IdempotentExecutor;
import com.livk.context.idempotent.exception.IdempotentException;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.util.StringUtils;

import java.time.Duration;

/**
 * Idempotent token store backed by a Redisson bucket.
 * <p>
 * Keys are stored as {@code idempotent:{key}} and occupied with {@code SET NX} plus the
 * given ttl.
 *
 * @author livk
 */
@RequiredArgsConstructor
public class RedissonIdempotentExecutor implements IdempotentExecutor {

	/**
	 * Redis key prefix for idempotent tokens.
	 */
	public static final String KEY_PREFIX = "idempotent:";

	private static final String TOKEN = "1";

	private final RedissonClient redissonClient;

	@Override
	public boolean tryAcquire(String key, Duration ttl) {
		if (ttl == null || ttl.isZero() || ttl.isNegative()) {
			throw new IdempotentException("Idempotent ttl must be positive");
		}
		return bucket(key).setIfAbsent(TOKEN, ttl);
	}

	@Override
	public void release(String key) {
		bucket(key).delete();
	}

	private RBucket<String> bucket(String key) {
		if (!StringUtils.hasText(key)) {
			throw new IdempotentException("Idempotent key must not be empty");
		}
		return this.redissonClient.getBucket(KEY_PREFIX + key);
	}

}
