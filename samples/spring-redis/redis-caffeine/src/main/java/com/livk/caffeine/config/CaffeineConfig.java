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

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.livk.caffeine.handler.CacheHandler;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * @author livk
 */
@Configuration
public class CaffeineConfig {

	@Bean
	public Cache<String, Object> caffeineCache() {
		return Caffeine.newBuilder()
			.initialCapacity(128)
			.maximumSize(1024)
			.expireAfterWrite(60, TimeUnit.SECONDS)
			.build();
	}

	@Bean
	public CacheManager cacheManager(CacheHandler<Object> cacheHandler) {
		return new RedisCaffeineManager(cacheHandler);
	}

}
