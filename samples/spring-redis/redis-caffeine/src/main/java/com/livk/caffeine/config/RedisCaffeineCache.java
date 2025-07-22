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
import org.springframework.cache.support.AbstractValueAdaptingCache;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.Callable;
import java.util.function.Supplier;

/**
 * @author livk
 */
public class RedisCaffeineCache extends AbstractValueAdaptingCache {

	private final String name;

	private final CacheHandler<Object> cacheHandler;

	protected RedisCaffeineCache(String name, CacheHandler<Object> cacheHandler, boolean allowNullValues) {
		super(allowNullValues);
		this.name = name;
		this.cacheHandler = cacheHandler;
	}

	@Override
	protected Object lookup(Object key) {
		return cacheHandler.read(key.toString());
	}

	@NonNull
	@Override
	public String getName() {
		return this.name;
	}

	@NonNull
	@Override
	public CacheHandler<Object> getNativeCache() {
		return cacheHandler;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(Object key, @NonNull Callable<T> valueLoader) {
		return (T) fromStoreValue(this.cacheHandler.readAndPut(key.toString(), supplier(valueLoader).get()));
	}

	@Override
	public void put(Object key, Object value) {
		this.cacheHandler.put(key.toString(), toStoreValue(value));
	}

	@Override
	public void evict(Object key) {
		this.cacheHandler.delete(key.toString());
	}

	@Override
	public void clear() {
		this.cacheHandler.clear();
	}

	private <V> Supplier<V> supplier(Callable<V> callable) {
		return () -> {
			try {
				return callable.call();
			}
			catch (Exception e) {
				throw new RuntimeException(e);
			}
		};
	}

}
