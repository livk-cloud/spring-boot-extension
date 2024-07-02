/*
 * Copyright 2021-2024 spring-boot-extension the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *       https://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.livk.context.redisearch.codec;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.lettuce.core.codec.RedisCodec;

/**
 * @author livk
 */
public class RedisCodecs {

	public static <K, V> RedisCodec<K, V> json(ObjectMapper mapper, Class<K> keyType, Class<V> valueType) {
		return new JacksonRedisCodec<>(mapper, keyType, valueType);
	}

	public static <K, V> RedisCodec<K, V> json(ObjectMapper mapper, JavaType keyType, JavaType valueType) {
		return new JacksonRedisCodec<>(mapper, keyType, valueType);
	}

	public static RedisCodec<Object, Object> jdk(ClassLoader classLoader) {
		return new JdkRedisCodec(classLoader);
	}

	public static RedisCodec<Object, Object> jdk() {
		return new JdkRedisCodec();
	}

}
