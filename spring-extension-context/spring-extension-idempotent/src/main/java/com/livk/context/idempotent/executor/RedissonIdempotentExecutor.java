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
import org.jspecify.annotations.Nullable;
import org.redisson.api.RBucket;
import org.redisson.api.RScript;
import org.redisson.api.RedissonClient;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

/**
 * Idempotent token store backed by a Redisson bucket.
 * <p>
 * Keys are stored as {@code idempotent:{key}} and occupied with {@code SET NX} plus the
 * given ttl. Release uses a compare-and-delete script so only the owning token may clear
 * the key.
 *
 * @author livk
 */
@RequiredArgsConstructor
public class RedissonIdempotentExecutor implements IdempotentExecutor {

	/**
	 * Redis key prefix for idempotent tokens.
	 */
	public static final String KEY_PREFIX = "idempotent:";

	private static final String RELEASE_SCRIPT = """
			if redis.call('get', KEYS[1]) == ARGV[1] then
			  return redis.call('del', KEYS[1])
			else
			  return 0
			end
			""";

	private final RedissonClient redissonClient;

	@Override
	public @Nullable String tryAcquire(String key, Duration ttl) {
		if (ttl == null || ttl.isZero() || ttl.isNegative()) {
			throw new IdempotentException("Idempotent ttl must be positive");
		}
		String token = UUID.randomUUID().toString();
		if (bucket(key).setIfAbsent(token, ttl)) {
			return token;
		}
		return null;
	}

	@Override
	public void release(String key, String token) {
		if (!StringUtils.hasText(token)) {
			throw new IdempotentException("Idempotent token must not be empty");
		}
		this.redissonClient.getScript()
			.eval(RScript.Mode.READ_WRITE, RELEASE_SCRIPT, RScript.ReturnType.LONG, List.of(redisKey(key)), token);
	}

	private RBucket<String> bucket(String key) {
		return this.redissonClient.getBucket(redisKey(key));
	}

	private static String redisKey(String key) {
		if (!StringUtils.hasText(key)) {
			throw new IdempotentException("Idempotent key must not be empty");
		}
		return KEY_PREFIX + key;
	}

}
