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

package com.livk.autoconfigure.redisearch.codec;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.util.Assert;

/**
 * The type Jackson redis codec.
 *
 * @param <T> the type parameter
 * @author livk
 */
@RequiredArgsConstructor
public class JacksonRedisCodec<T> extends AbstractRedisCodec<T> {

	private final ObjectMapper mapper;

	private final JavaType javaType;

	/**
	 * Creates a new {@link JacksonRedisCodec} for the given target {@link Class}.
	 * @param type must not be {@literal null}.
	 */
	public JacksonRedisCodec(Class<T> type) {
		this(new ObjectMapper(), type);
	}

	/**
	 * Creates a new {@link JacksonRedisCodec} for the given target {@link JavaType}.
	 * @param javaType must not be {@literal null}.
	 */
	public JacksonRedisCodec(JavaType javaType) {
		this(new ObjectMapper(), javaType);
	}

	/**
	 * Creates a new {@link JacksonRedisCodec} for the given target {@link Class}.
	 * @param mapper must not be {@literal null}.
	 * @param type must not be {@literal null}.
	 * @since 3.0
	 */
	public JacksonRedisCodec(ObjectMapper mapper, Class<T> type) {
		Assert.notNull(mapper, "ObjectMapper must not be null");
		Assert.notNull(type, "Java type must not be null");
		this.javaType = getJavaType(type);
		this.mapper = mapper;
	}

	private JavaType getJavaType(Class<?> clazz) {
		return TypeFactory.defaultInstance().constructType(clazz);
	}

	@Override
	protected byte[] serialize(T value) throws CodecException {
		try {
			return this.mapper.writeValueAsBytes(value);
		}
		catch (Exception ex) {
			throw new CodecException("Could not write JSON: " + ex.getMessage(), ex);
		}
	}

	@Override
	protected T deserialize(byte[] bytes) throws CodecException {
		try {
			return this.mapper.readValue(bytes, javaType);
		}
		catch (Exception ex) {
			throw new CodecException("Could not read JSON: " + ex.getMessage(), ex);
		}
	}

}
