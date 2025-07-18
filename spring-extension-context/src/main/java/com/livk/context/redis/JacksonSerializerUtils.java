/*
 * Copyright 2021-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * you may obtain a copy of the License at
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

package com.livk.context.redis;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.livk.commons.jackson.TypeFactoryUtils;
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
	 * @param <T> the type parameter
	 * @param targetClass the target class
	 * @return the redis serializer
	 */
	public <T> RedisSerializer<T> json(Class<T> targetClass) {
		return json(TypeFactoryUtils.javaType(targetClass));
	}

	/**
	 * Json redis serializer.
	 * @param <T> the type parameter
	 * @param javaType the target java type
	 * @return the redis serializer
	 */
	public <T> RedisSerializer<T> json(JavaType javaType) {
		ObjectMapper mapper = JsonMapper.builder().build();
		mapper.registerModules(new JavaTimeModule());
		return new Jackson2JsonRedisSerializer<>(mapper, javaType);
	}

	/**
	 * Json redis serializer.
	 * @return the redis serializer
	 */
	public RedisSerializer<Object> json() {
		return json(Object.class);
	}

}
