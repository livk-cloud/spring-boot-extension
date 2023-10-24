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

package com.livk.commons.jackson.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;
import com.livk.commons.jackson.core.JacksonSupport;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Map;

/**
 * The type Json mapper utils.
 *
 * @author livk
 */
@UtilityClass
public class JsonMapperUtils {

	private static final JacksonSupport<JsonMapper> JSON;


	static {
		JSON = JacksonSupport.create(new JsonMapper());
	}

	/**
	 * Read value t.
	 *
	 * @param <T>  the type parameter
	 * @param obj  the obj
	 * @param type the type
	 * @return the t
	 */
	public static <T> T readValue(Object obj, Class<T> type) {
		return JSON.readValue(obj, type);
	}

	/**
	 * Read value t.
	 *
	 * @param <T>  the type parameter
	 * @param obj  the obj
	 * @param type the type
	 * @return the t
	 */
	public static <T> T readValue(Object obj, JavaType type) {
		return JSON.readValue(obj, type);
	}

	/**
	 * Read value t.
	 *
	 * @param <T>           the type parameter
	 * @param obj           the obj
	 * @param typeReference the type reference
	 * @return the t
	 */
	public static <T> T readValue(Object obj, TypeReference<T> typeReference) {
		return JSON.readValue(obj, typeReference);
	}

	/**
	 * json序列化
	 *
	 * @param obj obj
	 * @return json string
	 */
	public static String writeValueAsString(Object obj) {
		return JSON.writeValueAsString(obj);
	}

	/**
	 * Write value as bytes byte [ ].
	 *
	 * @param obj the obj
	 * @return the byte [ ]
	 */
	public static byte[] writeValueAsBytes(Object obj) {
		return JSON.writeValueAsBytes(obj);
	}

	/**
	 * json反序列化成List
	 * <p>也可以看看{@link JacksonSupport#readValue(Object, TypeReference)} ,
	 * <p> {@link JacksonSupport#convertValue(Object, JavaType)}
	 *
	 * @param <T>  泛型
	 * @param obj  the obj
	 * @param type 类型
	 * @return the list
	 */
	public static <T> List<T> readValueList(Object obj, Class<T> type) {
		CollectionType collectionType = TypeFactoryUtils.collectionType(type);
		return JSON.readValue(obj, collectionType);
	}

	/**
	 * json反序列化成Map
	 * <p>也可以看看{@link JacksonSupport#readValue(Object, TypeReference)} ,
	 * <p> {@link JacksonSupport#convertValue(Object, JavaType)}
	 *
	 * @param <K>        the type parameter
	 * @param <V>        the type parameter
	 * @param obj        the obj
	 * @param keyClass   K Class
	 * @param valueClass V Class
	 * @return the map
	 */
	public static <K, V> Map<K, V> readValueMap(Object obj, Class<K> keyClass, Class<V> valueClass) {
		MapType mapType = TypeFactoryUtils.mapType(keyClass, valueClass);
		return JSON.readValue(obj, mapType);
	}

	/**
	 * 将json转化成JsonNode
	 *
	 * @param obj the obj
	 * @return the json node
	 */
	public static JsonNode readTree(Object obj) {
		return JSON.readTree(obj);
	}

	/**
	 * Convert object.
	 *
	 * @param <T>       the type parameter
	 * @param fromValue the  value
	 * @param type      the type
	 * @return the object
	 */
	public static <T> T convertValue(Object fromValue, Class<T> type) {
		return JSON.convertValue(fromValue, type);
	}

	/**
	 * Convert value t.
	 *
	 * @param <T>           the type parameter
	 * @param fromValue     the value
	 * @param typeReference the type reference
	 * @return the t
	 */
	public static <T> T convertValue(Object fromValue, TypeReference<T> typeReference) {
		return JSON.convertValue(fromValue, typeReference);
	}

	/**
	 * Convert object.
	 *
	 * @param <T>       the type parameter
	 * @param fromValue the  value
	 * @param javaType  the java type
	 * @return the object
	 */
	public static <T> T convertValue(Object fromValue, JavaType javaType) {
		return JSON.convertValue(fromValue, javaType);
	}

	/**
	 * Convert value list list.
	 *
	 * @param <T>       the type parameter
	 * @param fromValue the fromValue
	 * @param type      the type
	 * @return the list
	 */
	public static <T> List<T> convertValueList(Object fromValue, Class<T> type) {
		CollectionType collectionType = TypeFactoryUtils.collectionType(type);
		return JSON.convertValue(fromValue, collectionType);
	}

	/**
	 * Object to map map.
	 *
	 * @param <K>        the type parameter
	 * @param <V>        the type parameter
	 * @param fromValue  the fromValue
	 * @param keyClass   the key class
	 * @param valueClass the value class
	 * @return the map
	 */
	public static <K, V> Map<K, V> convertValueMap(Object fromValue, Class<K> keyClass, Class<V> valueClass) {
		MapType mapType = TypeFactoryUtils.mapType(keyClass, valueClass);
		return JSON.convertValue(fromValue, mapType);
	}
}
