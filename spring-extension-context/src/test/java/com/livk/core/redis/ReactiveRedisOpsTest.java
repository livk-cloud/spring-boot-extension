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

package com.livk.core.redis;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.ReactiveRedisConnection;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * <p>
 * ReactiveRedisOpsTest
 * </p>
 *
 * @author livk
 */
@ContextConfiguration(classes = { RedisFactoryConfig.class })
@ExtendWith(SpringExtension.class)
class ReactiveRedisOpsTest {

	@Autowired
	ReactiveRedisConnectionFactory connectionFactory;

	@Test
	void test() {
		RedisSerializationContext<String, Object> context = RedisSerializationContext
			.<String, Object>newSerializationContext()
			.key(RedisSerializer.string())
			.value(JacksonSerializerUtils.json())
			.hashKey(RedisSerializer.string())
			.hashValue(JacksonSerializerUtils.json())
			.build();
		ReactiveRedisOps ops = new ReactiveRedisOps(connectionFactory, context);
		assertEquals("PONG", ops.execute(ReactiveRedisConnection::ping).blockFirst());
	}

}
