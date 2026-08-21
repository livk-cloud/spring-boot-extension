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

package com.livk.commons.jackson;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.type.CollectionType;
import tools.jackson.databind.type.MapType;
import com.livk.commons.jackson.support.JacksonSupport;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Map;

/**
 * Utility class for JSON operations using default JsonMapper.
 *
 * @author livk
 */
@UtilityClass
public class JsonMapperUtils {

	private static final JacksonSupport JSON = new JacksonSupport(new JsonMapper());

	/**
	 * Reads JSON data from obj and converts to the specified type.
	 * @param <T> 泛型
	 * @param obj 待读取的数据
	 * @param type 返回相关类型
	 * @return 相关实例
	 */
	public static <T> T readValue(Object obj, Class<T> type) {
		return JSON.readValue(obj, type);
	}

	/**
	 * Reads JSON data from obj and converts to the specified JavaType.
	 * @param <T> 泛型
	 * @param obj 待读取的数据
	 * @param type 相关类型
	 * @return 相关实例
	 */
	public static <T> T readValue(Object obj, JavaType type) {
		return JSON.readValue(obj, type);
	}

	/**
	 * Reads JSON data from obj and converts to the specified TypeReference.
	 * @param <T> 泛型
	 * @param obj 待读取的数据
	 * @param typeReference typeReference包装的类型
	 * @return 相关实例
	 */
	public static <T> T readValue(Object obj, TypeReference<T> typeReference) {
		return JSON.readValue(obj, typeReference);
	}

	/**
	 * Serializes obj to JSON string.
	 * @param obj obj
	 * @return json string
	 */
	public static String writeValueAsString(Object obj) {
		return JSON.writeValueAsString(obj);
	}

	/**
	 * Serializes obj to byte array.
	 * @param obj the obj
	 * @return the byte [ ]
	 */
	public static byte[] writeValueAsBytes(Object obj) {
		return JSON.writeValueAsBytes(obj);
	}

	/**
	 * Deserializes JSON to a List of the specified type.
	 * <p>
	 * 也可以看看{@link JacksonSupport#readValue(Object, TypeReference)} ,
	 * <p>
	 * {@link JacksonSupport#convertValue(Object, JavaType)}
	 * @param <T> 泛型
	 * @param obj the obj
	 * @param type 类型
	 * @return the list
	 */
	public static <T> List<T> readValueList(Object obj, Class<T> type) {
		CollectionType collectionType = TypeFactoryUtils.listType(type);
		return JSON.readValue(obj, collectionType);
	}

	/**
	 * Deserializes JSON to a Map with specified key and value types.
	 * <p>
	 * 也可以看看{@link JacksonSupport#readValue(Object, TypeReference)} ,
	 * <p>
	 * {@link JacksonSupport#convertValue(Object, JavaType)}
	 * @param <K> the type parameter
	 * @param <V> the type parameter
	 * @param obj the obj
	 * @param keyClass k Class
	 * @param valueClass v Class
	 * @return the map
	 */
	public static <K, V> Map<K, V> readValueMap(Object obj, Class<K> keyClass, Class<V> valueClass) {
		MapType mapType = TypeFactoryUtils.mapType(keyClass, valueClass);
		return JSON.readValue(obj, mapType);
	}

	/**
	 * Reads obj data and converts to JsonNode.
	 * @param obj the obj
	 * @return the json node
	 */
	public static JsonNode readTree(Object obj) {
		return JSON.readTree(obj);
	}

	/**
	 * Converts data using Jackson type conversion.
	 * @param <T> 泛型
	 * @param fromValue 待转换数据
	 * @param type 返回类型
	 * @return 相关实例
	 */
	public static <T> T convertValue(Object fromValue, Class<T> type) {
		return JSON.convertValue(fromValue, type);
	}

	/**
	 * Converts data using Jackson with TypeReference.
	 * @param <T> 泛型
	 * @param fromValue 待转换数据
	 * @param typeReference the type reference
	 * @return 相关实例
	 */
	public static <T> T convertValue(Object fromValue, TypeReference<T> typeReference) {
		return JSON.convertValue(fromValue, typeReference);
	}

	/**
	 * Converts data using Jackson with JavaType.
	 * @param <T> 泛型
	 * @param fromValue 待转换数据
	 * @param javaType 相关类型
	 * @return 相关实例
	 */
	public static <T> T convertValue(Object fromValue, JavaType javaType) {
		return JSON.convertValue(fromValue, javaType);
	}

	/**
	 * Converts data to a List using Jackson.
	 * @param <T> 泛型
	 * @param fromValue 待转换数据
	 * @param type 返回类型
	 * @return list
	 */
	public static <T> List<T> convertValueList(Object fromValue, Class<T> type) {
		CollectionType collectionType = TypeFactoryUtils.listType(type);
		return JSON.convertValue(fromValue, collectionType);
	}

	/**
	 * Converts data to a Map using Jackson.
	 * @param <K> the type parameter
	 * @param <V> the type parameter
	 * @param fromValue the fromValue
	 * @param keyClass the key class
	 * @param valueClass the value class
	 * @return map
	 */
	public static <K, V> Map<K, V> convertValueMap(Object fromValue, Class<K> keyClass, Class<V> valueClass) {
		MapType mapType = TypeFactoryUtils.mapType(keyClass, valueClass);
		return JSON.convertValue(fromValue, mapType);
	}

}
