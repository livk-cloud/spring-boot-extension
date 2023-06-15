/*
 * Copyright 2021 spring-boot-extension the original author or authors.
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
 *
 */

package com.livk.redisson.bloom;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

/**
 * <p>
 * RedissonBloom
 * </p>
 *
 * @author livk
 */
@Component
@RequiredArgsConstructor
public class RedissonBloom {

	private final RedissonClient redissonClient;

	private RBloomFilter<String> filter;

	@PostConstruct
	public void init() {
		this.filter = redissonClient.getBloomFilter("redisson:bloom:filter");
		this.filter.tryInit(Integer.MAX_VALUE / 10, 0.003);
	}

	public void addKey(String data) {
		filter.add(data);
	}

	public boolean hasKey(String data) {
		return filter.contains(data);
	}

}
