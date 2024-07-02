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

package com.livk.context.redisearch.codec;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

/**
 * The type Jackson redis codec.
 *
 * @author livk
 */
@RequiredArgsConstructor
class JacksonRedisCodec<K, V> extends AbstractRedisCodec<K, V> {

	private final ObjectMapper mapper;

	private final JavaType keyType;

	private final JavaType valueType;

	public JacksonRedisCodec(ObjectMapper mapper, Class<K> type, Class<V> valueType) {
		this.mapper = mapper;
		this.keyType = getJavaType(type);
		this.valueType = getJavaType(valueType);
	}

	private JavaType getJavaType(Class<?> clazz) {
		return TypeFactory.defaultInstance().constructType(clazz);
	}

	@Override
	protected byte[] serializeKey(K value) throws CodecException {
		return serialize(value);
	}

	@Override
	protected K deserializeKey(byte[] bytes) throws CodecException {
		return deserialize(bytes, keyType);
	}

	@Override
	protected byte[] serializeValue(V value) throws CodecException {
		return serialize(value);
	}

	@Override
	protected V deserializeValue(byte[] bytes) throws CodecException {
		return deserialize(bytes, valueType);
	}

	private byte[] serialize(Object value) throws CodecException {
		try {
			return mapper.writeValueAsBytes(value);
		}
		catch (JsonProcessingException e) {
			throw new CodecException("Could not serialize value", e);
		}
	}

	private <T> T deserialize(byte[] bytes, JavaType type) throws CodecException {
		try {
			return mapper.readValue(bytes, type);
		}
		catch (IOException e) {
			throw new CodecException("Could not deserialize value", e);
		}
	}

}
