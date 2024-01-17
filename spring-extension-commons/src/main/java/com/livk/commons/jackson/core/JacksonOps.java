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

package com.livk.commons.jackson.core;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Jackson相关操作接口
 *
 * @author livk
 */
public interface JacksonOps {

	/**
	 * 从obj读取json数据转成相应实体类
	 * @param <T> 泛型
	 * @param obj 待读取的数据
	 * @param type 返回相关类型
	 * @return 相关实例
	 */
	<T> T readValue(Object obj, Class<T> type);

	/**
	 * 从obj读取json数据转成相应实体类
	 * @param <T> 泛型
	 * @param obj 待读取的数据
	 * @param typeReference TypeReference包装的类型
	 * @return 相关实例
	 */
	<T> T readValue(Object obj, TypeReference<T> typeReference);

	/**
	 * 从obj读取json数据转成相应实体类
	 * @param <T> 泛型
	 * @param obj 待读取的数据
	 * @param type 相关类型
	 * @return 相关实例
	 */
	<T> T readValue(Object obj, JavaType type);

	/**
	 * obj序列化成string
	 * @param obj obj
	 * @return json string
	 */
	String writeValueAsString(Object obj);

	/**
	 * obj序列化成byte[]
	 * @param obj the obj
	 * @return the byte [ ]
	 */
	byte[] writeValueAsBytes(Object obj);

	/**
	 * obj读取数据转化成JsonNode
	 * @param obj the obj
	 * @return the json node
	 */
	JsonNode readTree(Object obj);

	/**
	 * jackson数据转换
	 * @param <T> 泛型
	 * @param fromValue 待转换数据
	 * @param type 返回类型
	 * @return 相关实例
	 */
	<T> T convertValue(Object fromValue, Class<T> type);

	/**
	 * jackson数据转换
	 * @param <T> 泛型
	 * @param fromValue 待转换数据
	 * @param typeReference the type reference
	 * @return 相关实例
	 */
	<T> T convertValue(Object fromValue, TypeReference<T> typeReference);

	/**
	 * jackson数据转换
	 * @param <T> 泛型
	 * @param fromValue 待转换数据
	 * @param javaType 相关类型
	 * @return 相关实例
	 */
	<T> T convertValue(Object fromValue, JavaType javaType);

}
