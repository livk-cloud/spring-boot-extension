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

import com.livk.commons.util.ObjectUtils;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.codec.StringCodec;
import lombok.RequiredArgsConstructor;

import java.nio.ByteBuffer;

/**
 * The type Abstract redis codec.
 *
 * @param <T> the type parameter
 * @author livk
 */
@RequiredArgsConstructor
public abstract class AbstractRedisCodec<T> implements RedisCodec<String, T> {

	private final RedisCodec<String, ?> keyCodec;

	/**
	 * Instantiates a new Abstract redis codec.
	 */
	public AbstractRedisCodec() {
		this(new StringCodec());
	}

	@Override
	public final ByteBuffer encodeKey(String key) {
		return keyCodec.encodeKey(key);
	}

	@Override
	public final String decodeKey(ByteBuffer bytes) {
		return keyCodec.decodeKey(bytes);
	}

	@Override
	public final T decodeValue(ByteBuffer bytes) {
		ByteBuffer allocate = ByteBuffer.allocate(bytes.capacity());
		byte[] array = allocate.put(bytes).array();
		if (ObjectUtils.isEmpty(array)) {
			return null;
		}
		return deserialize(array);
	}

	@Override
	public final ByteBuffer encodeValue(T value) {
		if (value == null) {
			return ByteBuffer.wrap(new byte[0]);
		}
		byte[] bytes = serialize(value);
		return ByteBuffer.wrap(bytes);
	}

	/**
	 * Serialize the given object to binary data.
	 * @param value object to serialize. Can be {@literal null}.
	 * @return the equivalent binary data. Can be {@literal null}.
	 * @throws CodecException the codec exception
	 */
	protected abstract byte[] serialize(T value) throws CodecException;

	/**
	 * Deserialize an object from the given binary data.
	 * @param bytes object binary representation. Can be {@literal null}.
	 * @return the equivalent object instance. Can be {@literal null}.
	 * @throws CodecException the codec exception
	 */
	protected abstract T deserialize(byte[] bytes) throws CodecException;

}
