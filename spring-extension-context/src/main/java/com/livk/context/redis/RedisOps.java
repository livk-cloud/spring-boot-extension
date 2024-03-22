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

package com.livk.context.redis;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * The type Universal redis template.
 *
 * @author livk
 */
public class RedisOps extends RedisTemplate<String, Object> {

	/**
	 * Instantiates a new Universal redis template.
	 */
	private RedisOps(RedisSerializer<?> serializer) {
		this.setKeySerializer(RedisSerializer.string());
		this.setHashKeySerializer(RedisSerializer.string());
		this.setValueSerializer(serializer);
		this.setHashValueSerializer(serializer);
	}

	/**
	 * Instantiates a new Universal redis template.
	 * @param redisConnectionFactory the redis connection factory
	 * @param serializer the serializer
	 */
	public RedisOps(RedisConnectionFactory redisConnectionFactory, RedisSerializer<?> serializer) {
		this(serializer);
		this.setConnectionFactory(redisConnectionFactory);
		this.afterPropertiesSet();
	}

	/**
	 * Instantiates a new Redis ops.
	 * @param redisConnectionFactory the redis connection factory
	 */
	public RedisOps(RedisConnectionFactory redisConnectionFactory) {
		this(redisConnectionFactory, JacksonSerializerUtils.json());
	}

}
