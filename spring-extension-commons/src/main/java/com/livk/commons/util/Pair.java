/*
 * Copyright 2021 spring-boot-extension the original author or authors.
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
 *
 */

package com.livk.commons.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import com.fasterxml.jackson.databind.type.TypeBindings;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * <p>
 * 通用键值对
 * </p>
 *
 * @param <K> key type parameter
 * @param <V> value type parameter
 * @author livk
 */
@EqualsAndHashCode
@RequiredArgsConstructor(staticName = "of")
@JsonSerialize(using = Pair.PairJsonSerializer.class)
@JsonDeserialize(using = Pair.PairJsonDeserializer.class)
public class Pair<K, V> implements Serializable, Cloneable {

	/**
	 * 默认的空Pair
	 */
	public static final Pair<?, ?> EMPTY = Pair.of(null, null);

	@Serial
	private static final long serialVersionUID = -2303547536834226401L;

	/**
	 * key
	 */
	private final K key;

	/**
	 * value
	 */
	private final V value;

	/**
	 * 静态构造Map.Entry转Pair
	 * @param <K> key type parameter
	 * @param <V> value type parameter
	 * @param entry entry
	 * @return pair
	 */
	public static <K, V> Pair<K, V> of(Map.Entry<K, V> entry) {
		return of(entry.getKey(), entry.getValue());
	}

	/**
	 * 获取Key
	 * @return key
	 */
	public K key() {
		return key;
	}

	/**
	 * 获取value
	 * @return value
	 */
	public V value() {
		return value;
	}

	/**
	 * 转成Map
	 * @return the map
	 * @see Map#of(Object, Object)
	 */
	public Map<K, V> toMap() {
		return Map.of(key, value);
	}

	/**
	 * 转成Map.Entry
	 * @return entry
	 */
	public Map.Entry<K, V> toEntry() {
		return Map.entry(key, value);
	}

	/**
	 * 进行map转换
	 * @param <S> key转换后type
	 * @param <U> value转换后type
	 * @param keyFunction key function
	 * @param valueFunction value function
	 * @return pair
	 */
	public <S, U> Pair<S, U> map(Function<K, S> keyFunction, Function<V, U> valueFunction) {
		return of(keyFunction.apply(key), valueFunction.apply(value));
	}

	/**
	 * Key进行map转换
	 * @param <S> key转换后type
	 * @param keyFunction key function
	 * @return pair
	 */
	public <S> Pair<S, V> keyMap(Function<K, S> keyFunction) {
		return map(keyFunction, Function.identity());
	}

	/**
	 * Value进行map转换
	 * @param <U> value转换后type
	 * @param valueFunction value function
	 * @return pair
	 */
	public <U> Pair<K, U> valueMap(Function<V, U> valueFunction) {
		return map(Function.identity(), valueFunction);
	}

	/**
	 * 进行flatmap转换
	 * @param <S> key转换后type
	 * @param <U> value转换后type
	 * @param biFunction bi function
	 * @return pair
	 */
	public <S, U> Pair<S, U> flatMap(BiFunction<K, V, Pair<S, U>> biFunction) {
		return biFunction.apply(key, value);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Pair<K, V> clone() {
		try {
			return (Pair<K, V>) super.clone();
		}
		catch (CloneNotSupportedException e) {
			throw new AssertionError();
		}
	}

	@Override
	public String toString() {
		return "{" + key + ":" + value + "}";
	}

	/**
	 * Pair Jackson序列化工具
	 */
	static class PairJsonSerializer extends StdScalarSerializer<Pair<Object, Object>> implements ContextualSerializer {

		private JsonSerializer<Object> keySerializer;

		/**
		 * 构造器
		 */
		protected PairJsonSerializer() {
			super(Pair.class, false);
		}

		@Override
		public void serialize(Pair<Object, Object> pair, JsonGenerator gen, SerializerProvider provider)
				throws IOException {
			gen.writeStartObject(pair);
			keySerializer.serialize(pair.key(), gen, provider);
			gen.writeObject(pair.value());
			gen.writeEndObject();
		}

		@Override
		public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property)
				throws JsonMappingException {
			keySerializer = prov.findKeySerializer(Object.class, property);
			return this;
		}

	}

	/**
	 * Pair Jackson反序列化工具
	 */
	static class PairJsonDeserializer extends StdScalarDeserializer<Pair<Object, Object>>
			implements ContextualDeserializer {

		private KeyDeserializer keyDeserializer;

		private JavaType valueType;

		/**
		 * 构造器
		 */
		protected PairJsonDeserializer() {
			super(Pair.class);
		}

		@Override
		public Pair<Object, Object> deserialize(JsonParser p, DeserializationContext context) throws IOException {
			p.nextToken();
			String name = p.currentName();
			JsonNode valueNode = context.readTree(p).get(name);
			ObjectMapper mapper = (ObjectMapper) p.getCodec();
			return Pair.of(keyDeserializer.deserializeKey(name, context), mapper.convertValue(valueNode, valueType));
		}

		@Override
		public JsonDeserializer<?> createContextual(DeserializationContext context, BeanProperty property)
				throws JsonMappingException {
			JavaType contextualType = context.getContextualType();
			TypeBindings bindings = contextualType.getBindings();
			JavaType keyType = bindings.getBoundType(0);
			valueType = bindings.getBoundType(1);
			keyDeserializer = context.findKeyDeserializer(keyType, property);
			return this;
		}

	}

}
