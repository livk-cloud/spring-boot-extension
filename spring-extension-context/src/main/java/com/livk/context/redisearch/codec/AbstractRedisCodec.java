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

package com.livk.context.redisearch.codec;

import com.livk.commons.util.ObjectUtils;
import io.lettuce.core.codec.RedisCodec;

import java.nio.ByteBuffer;
import java.util.function.Function;

/**
 * The type Abstract redis codec.
 *
 * @param <K> the type parameter
 * @param <V> the type parameter
 * @author livk
 */
public abstract class AbstractRedisCodec<K, V> implements RedisCodec<K, V> {

	@Override
	public final ByteBuffer encodeKey(K key) {
		return encode(this::serializeKey, key);
	}

	@Override
	public final K decodeKey(ByteBuffer bytes) {
		return decode(bytes, this::deserializeKey);
	}

	@Override
	public final V decodeValue(ByteBuffer bytes) {
		return decode(bytes, this::deserializeValue);
	}

	@Override
	public final ByteBuffer encodeValue(V value) {
		return encode(this::serializeValue, value);
	}

	private <T> T decode(ByteBuffer bytes, Function<byte[], T> function) {
		ByteBuffer allocate = ByteBuffer.allocate(bytes.capacity());
		byte[] array = allocate.put(bytes).array();
		if (ObjectUtils.isEmpty(array)) {
			return null;
		}
		return function.apply(array);
	}

	private <T> ByteBuffer encode(Function<T, byte[]> function, T value) {
		if (value == null) {
			return ByteBuffer.wrap(new byte[0]);
		}
		byte[] bytes = function.apply(value);
		return ByteBuffer.wrap(bytes);
	}

	/**
	 * Serialize the given object to binary data.
	 * @param value object to serialize. Can be {@literal null}.
	 * @return the equivalent binary data. Can be {@literal null}.
	 * @throws CodecException the codec exception
	 */
	protected abstract byte[] serializeKey(K value) throws CodecException;

	/**
	 * Deserialize key k.
	 * @param bytes the bytes
	 * @return the k
	 * @throws CodecException the codec exception
	 */
	protected abstract K deserializeKey(byte[] bytes) throws CodecException;

	/**
	 * Serialize the given object to binary data.
	 * @param value object to serialize. Can be {@literal null}.
	 * @return the equivalent binary data. Can be {@literal null}.
	 * @throws CodecException the codec exception
	 */
	protected abstract byte[] serializeValue(V value) throws CodecException;

	/**
	 * Deserialize an object from the given binary data.
	 * @param bytes object binary representation. Can be {@literal null}.
	 * @return the equivalent object instance. Can be {@literal null}.
	 * @throws CodecException the codec exception
	 */
	protected abstract V deserializeValue(byte[] bytes) throws CodecException;

}
