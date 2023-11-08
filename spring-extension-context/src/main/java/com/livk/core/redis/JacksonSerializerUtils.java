/*
 * Copyright 2021 spring-boot-extension the original author or authors.
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
 *
 */

package com.livk.core.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.experimental.UtilityClass;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * The type Jackson serializer utils.
 */
@UtilityClass
public class JacksonSerializerUtils {

	/**
	 * Json redis serializer.
	 *
	 * @param <T>         the type parameter
	 * @param targetClass the target class
	 * @param mapper      the mapper
	 * @return the redis serializer
	 */
	public <T> RedisSerializer<T> json(Class<T> targetClass, ObjectMapper mapper) {
		return new Jackson2JsonRedisSerializer<>(mapper, targetClass);
	}

	/**
	 * Json redis serializer.
	 *
	 * @param <T>         the type parameter
	 * @param targetClass the target class
	 * @return the redis serializer
	 */
	public <T> RedisSerializer<T> json(Class<T> targetClass) {
		ObjectMapper mapper = JsonMapper.builder().build();
		mapper.registerModules(new JavaTimeModule());
		return json(targetClass, mapper);
	}

	/**
	 * Json redis serializer.
	 *
	 * @return the redis serializer
	 */
	public RedisSerializer<Object> json() {
		return json(Object.class);
	}

}
