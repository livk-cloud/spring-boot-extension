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

package com.livk.caffeine.handler;

import com.github.benmanes.caffeine.cache.Cache;
import com.livk.context.redis.RedisOps;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @author livk
 */
@Component
@RequiredArgsConstructor
public class CacheHandlerAdapter implements CacheHandler<Object> {

	private final RedisOps redisOps;

	private final Cache<String, Object> cache;

	@Override
	public void put(String key, Object proceed) {
		redisOps.opsForValue().set(key, proceed);
		cache.put(key, proceed);
	}

	@Override
	public void delete(String key) {
		cache.invalidate(key);
		redisOps.delete(key);
	}

	@Override
	public Object read(String key) {
		return cache.get(key, s -> redisOps.opsForValue().get(s));
	}

	@Override
	public void clear() {
		Set<String> keys = cache.asMap().keySet();
		cache.invalidateAll();
		redisOps.delete(keys);
	}

}
