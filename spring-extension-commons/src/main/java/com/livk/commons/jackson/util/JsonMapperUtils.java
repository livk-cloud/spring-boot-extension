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
 * JsonMapper工具类，使用默认JsonMapper
 *
 * @author livk
 */
@UtilityClass
public class JsonMapperUtils {

	private static final JacksonSupport JSON;

	static {
		JSON = new JacksonSupport(new JsonMapper());
	}

	/**
	 * 从obj读取json数据转成相应实体类
	 * @param <T> 泛型
	 * @param obj 待读取的数据
	 * @param type 返回相关类型
	 * @return 相关实例
	 */
	public static <T> T readValue(Object obj, Class<T> type) {
		return JSON.readValue(obj, type);
	}

	/**
	 * 从obj读取json数据转成相应实体类
	 * @param <T> 泛型
	 * @param obj 待读取的数据
	 * @param type 相关类型
	 * @return 相关实例
	 */
	public static <T> T readValue(Object obj, JavaType type) {
		return JSON.readValue(obj, type);
	}

	/**
	 * 从obj读取json数据转成相应实体类
	 * @param <T> 泛型
	 * @param obj 待读取的数据
	 * @param typeReference TypeReference包装的类型
	 * @return 相关实例
	 */
	public static <T> T readValue(Object obj, TypeReference<T> typeReference) {
		return JSON.readValue(obj, typeReference);
	}

	/**
	 * obj序列化成string
	 * @param obj obj
	 * @return json string
	 */
	public static String writeValueAsString(Object obj) {
		return JSON.writeValueAsString(obj);
	}

	/**
	 * obj序列化成byte[]
	 * @param obj the obj
	 * @return the byte [ ]
	 */
	public static byte[] writeValueAsBytes(Object obj) {
		return JSON.writeValueAsBytes(obj);
	}

	/**
	 * json反序列化成List
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
		CollectionType collectionType = TypeFactoryUtils.collectionType(type);
		return JSON.readValue(obj, collectionType);
	}

	/**
	 * json反序列化成Map
	 * <p>
	 * 也可以看看{@link JacksonSupport#readValue(Object, TypeReference)} ,
	 * <p>
	 * {@link JacksonSupport#convertValue(Object, JavaType)}
	 * @param <K> the type parameter
	 * @param <V> the type parameter
	 * @param obj the obj
	 * @param keyClass K Class
	 * @param valueClass V Class
	 * @return the map
	 */
	public static <K, V> Map<K, V> readValueMap(Object obj, Class<K> keyClass, Class<V> valueClass) {
		MapType mapType = TypeFactoryUtils.mapType(keyClass, valueClass);
		return JSON.readValue(obj, mapType);
	}

	/**
	 * obj读取数据转化成JsonNode
	 * @param obj the obj
	 * @return the json node
	 */
	public static JsonNode readTree(Object obj) {
		return JSON.readTree(obj);
	}

	/**
	 * jackson数据转换
	 * @param <T> 泛型
	 * @param fromValue 待转换数据
	 * @param type 返回类型
	 * @return 相关实例
	 */
	public static <T> T convertValue(Object fromValue, Class<T> type) {
		return JSON.convertValue(fromValue, type);
	}

	/**
	 * jackson数据转换
	 * @param <T> 泛型
	 * @param fromValue 待转换数据
	 * @param typeReference the type reference
	 * @return 相关实例
	 */
	public static <T> T convertValue(Object fromValue, TypeReference<T> typeReference) {
		return JSON.convertValue(fromValue, typeReference);
	}

	/**
	 * jackson数据转换
	 * @param <T> 泛型
	 * @param fromValue 待转换数据
	 * @param javaType 相关类型
	 * @return 相关实例
	 */
	public static <T> T convertValue(Object fromValue, JavaType javaType) {
		return JSON.convertValue(fromValue, javaType);
	}

	/**
	 * jackson数据转换List
	 * @param <T> 泛型
	 * @param fromValue 待转换数据
	 * @param type 返回类型
	 * @return List
	 */
	public static <T> List<T> convertValueList(Object fromValue, Class<T> type) {
		CollectionType collectionType = TypeFactoryUtils.collectionType(type);
		return JSON.convertValue(fromValue, collectionType);
	}

	/**
	 * jackson数据转换Map
	 * @param <K> the type parameter
	 * @param <V> the type parameter
	 * @param fromValue the fromValue
	 * @param keyClass the key class
	 * @param valueClass the value class
	 * @return MAP
	 */
	public static <K, V> Map<K, V> convertValueMap(Object fromValue, Class<K> keyClass, Class<V> valueClass) {
		MapType mapType = TypeFactoryUtils.mapType(keyClass, valueClass);
		return JSON.convertValue(fromValue, mapType);
	}

}
