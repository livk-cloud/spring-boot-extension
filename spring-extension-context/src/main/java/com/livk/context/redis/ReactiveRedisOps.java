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

package com.livk.context.redis;

import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;

/**
 * The type Universal reactive redis template.
 *
 * @author livk
 */
public class ReactiveRedisOps extends ReactiveRedisTemplate<String, Object> {

	/**
	 * Instantiates a new Universal reactive redis template.
	 * @param connectionFactory the connection factory
	 * @param serializationContext the serialization context
	 */
	public ReactiveRedisOps(ReactiveRedisConnectionFactory connectionFactory,
			RedisSerializationContext<String, Object> serializationContext) {
		this(connectionFactory, serializationContext, false);
	}

	/**
	 * Instantiates a new Universal reactive redis template.
	 * @param connectionFactory the connection factory
	 * @param serializationContext the serialization context
	 * @param exposeConnection the expose connection
	 */
	public ReactiveRedisOps(ReactiveRedisConnectionFactory connectionFactory,
			RedisSerializationContext<String, Object> serializationContext, boolean exposeConnection) {
		super(connectionFactory, serializationContext, exposeConnection);
	}

}
