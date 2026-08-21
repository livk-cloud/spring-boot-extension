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

package com.livk.commons.jackson.support;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.JsonNode;

/**
 * Interface defining Jackson operations.
 *
 * @author livk
 */
public interface JacksonOps {

	/**
	 * Reads JSON data from obj and converts to the specified Class type.
	 * @param <T> 泛型
	 * @param readVal 待读取的数据
	 * @param type 返回相关类型
	 * @return 相关实例
	 */
	<T> T readValue(Object readVal, Class<T> type);

	/**
	 * Reads JSON data from obj and converts using TypeReference.
	 * @param <T> 泛型
	 * @param readVal 待读取的数据
	 * @param typeReference typeReference包装的类型
	 * @return 相关实例
	 */
	<T> T readValue(Object readVal, TypeReference<T> typeReference);

	/**
	 * Reads JSON data from obj and converts using JavaType.
	 * @param <T> 泛型
	 * @param readVal 待读取的数据
	 * @param type 相关类型
	 * @return 相关实例
	 */
	<T> T readValue(Object readVal, JavaType type);

	/**
	 * Serializes obj to JSON string.
	 * @param writeVal obj
	 * @return json string
	 */
	String writeValueAsString(Object writeVal);

	/**
	 * Serializes obj to byte array.
	 * @param writeVal the obj
	 * @return the byte [ ]
	 */
	byte[] writeValueAsBytes(Object writeVal);

	/**
	 * Reads obj data and converts to JsonNode.
	 * @param readVal the obj
	 * @return the json node
	 */
	JsonNode readTree(Object readVal);

	/**
	 * Converts data using Jackson with Class type.
	 * @param <T> 泛型
	 * @param fromValue 待转换数据
	 * @param type 返回类型
	 * @return 相关实例
	 */
	<T> T convertValue(Object fromValue, Class<T> type);

	/**
	 * Converts data using Jackson with TypeReference.
	 * @param <T> 泛型
	 * @param fromValue 待转换数据
	 * @param typeReference the type reference
	 * @return 相关实例
	 */
	<T> T convertValue(Object fromValue, TypeReference<T> typeReference);

	/**
	 * Converts data using Jackson with JavaType.
	 * @param <T> 泛型
	 * @param fromValue 待转换数据
	 * @param javaType 相关类型
	 * @return 相关实例
	 */
	<T> T convertValue(Object fromValue, JavaType javaType);

}
