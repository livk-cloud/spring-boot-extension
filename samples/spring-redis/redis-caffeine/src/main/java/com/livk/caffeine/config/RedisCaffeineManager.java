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

package com.livk.caffeine.config;

import com.livk.caffeine.handler.CacheHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.jspecify.annotations.NonNull;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author livk
 */
@RequiredArgsConstructor
public class RedisCaffeineManager implements CacheManager {

	private final CacheHandler<Object> adapter;

	private final Map<String, Cache> cacheMap = new ConcurrentHashMap<>(16);

	@Override
	public Cache getCache(@NonNull String name) {
		Cache cache = this.cacheMap.get(name);
		return (cache != null) ? cache : this.cacheMap.computeIfAbsent(name, this::createCache);
	}

	private Cache createCache(String name) {
		return new RedisCaffeineCache(name, adapter, true);
	}

	@NonNull
	@Override
	public Collection<String> getCacheNames() {
		return Collections.unmodifiableSet(this.cacheMap.keySet());
	}

}
