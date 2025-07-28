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

package com.livk.admin.server.config;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.livk.commons.jackson.TypeFactoryUtils;
import com.livk.context.redis.JacksonSerializerUtils;
import com.livk.context.redis.ReactiveRedisOps;
import de.codecentric.boot.admin.server.domain.events.InstanceEvent;
import de.codecentric.boot.admin.server.domain.values.InstanceId;
import de.codecentric.boot.admin.server.utils.jackson.AdminServerModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.util.List;

/**
 * @author livk
 */
@Configuration
public class AdminRedisConfig {

	@Bean
	public ReactiveRedisOps reactiveRedisOps(AdminServerModule adminJacksonModule,
			ReactiveRedisConnectionFactory redisConnectionFactory) {
		JsonMapper mapper = JsonMapper.builder().addModule(adminJacksonModule).addModule(new JavaTimeModule()).build();
		Jackson2JsonRedisSerializer<InstanceId> hashKeySerializer = new Jackson2JsonRedisSerializer<>(mapper,
				InstanceId.class);
		CollectionType collectionType = TypeFactoryUtils.listType(InstanceEvent.class);
		Jackson2JsonRedisSerializer<List<InstanceEvent>> hashValueSerializer = new Jackson2JsonRedisSerializer<>(mapper,
				collectionType);
		RedisSerializationContext<String, Object> serializationContext = RedisSerializationContext
			.<String, Object>newSerializationContext()
			.key(RedisSerializer.string())
			.value(JacksonSerializerUtils.json())
			.hashKey(hashKeySerializer)
			.hashValue(hashValueSerializer)
			.build();
		return new ReactiveRedisOps(redisConnectionFactory, serializationContext);
	}

}
