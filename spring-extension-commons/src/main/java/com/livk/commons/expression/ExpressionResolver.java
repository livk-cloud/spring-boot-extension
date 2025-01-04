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

package com.livk.commons.expression;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * 表达式通用解析器
 *
 * @author livk
 * @see Context
 */
public interface ExpressionResolver {

	/**
	 * 根据context信息将表达式解析，并转成相应的类型
	 * @param <T> 泛型
	 * @param value 表达式
	 * @param context 解析上下文环境数据
	 * @param returnType 返回类型
	 * @return T
	 */
	<T> T evaluate(String value, Context context, Class<T> returnType);

	/**
	 * 根据Map环境信息将表达式解析，并转成相应的类型
	 * @param <T> 泛型
	 * @param value 表达式
	 * @param contextMap 解析上下文环境数据
	 * @param returnType 返回类型
	 * @return T
	 */
	<T> T evaluate(String value, Map<String, ?> contextMap, Class<T> returnType);

	/**
	 * 根据Method将表达式解析，并转成相对应的类型
	 * @param <T> 泛型
	 * @param value 表达式
	 * @param method method
	 * @param args args
	 * @param returnType 返回类型
	 * @return T
	 */
	<T> T evaluate(String value, Method method, Object[] args, Class<T> returnType);

	/**
	 * 根据Method和Map环境信息将表达式解析，并转成相对应的类型
	 * @param <T> 泛型
	 * @param value 表达式
	 * @param method method
	 * @param args args
	 * @param contextMap 解析上下文环境数据
	 * @param returnType 返回类型
	 * @return T
	 * @see #evaluate(String, Context, Class)
	 * @deprecated use {@link #evaluate(String, Context, Class)}
	 */
	@Deprecated(since = "1.4.2", forRemoval = true)
	default <T> T evaluate(String value, Method method, Object[] args, Map<String, ?> contextMap, Class<T> returnType) {
		Context context = ContextFactory.DEFAULT_FACTORY.create(method, args).putAll(contextMap);
		return evaluate(value, context, returnType);
	}

	/**
	 * 根据Method将表达式解析，并转成String
	 * @param value 表达式
	 * @param method method
	 * @param args args
	 * @return string
	 */
	default String evaluate(String value, Method method, Object[] args) {
		return evaluate(value, method, args, String.class);
	}

	/**
	 * 根据context信息将表达式解析，并转成String
	 * @param value 表达式
	 * @param context 解析上下文环境数据
	 * @return string
	 */
	default String evaluate(String value, Context context) {
		return evaluate(value, context, String.class);
	}

	/**
	 * 根据Map环境信息将表达式解析，并转成String
	 * @param value 表达式
	 * @param contextMap 解析上下文环境数据
	 * @return string
	 */
	default String evaluate(String value, Map<String, ?> contextMap) {
		return evaluate(value, contextMap, String.class);
	}

	/**
	 * 根据Method和Map环境信息将表达式解析，并转成String
	 * @param value 表达式
	 * @param method method
	 * @param args args
	 * @param contextMap 解析上下文环境数据
	 * @return string * @deprecated use {@link #evaluate(String, Context)}
	 */
	@Deprecated(since = "1.4.2", forRemoval = true)
	default String evaluate(String value, Method method, Object[] args, Map<String, ?> contextMap) {
		return evaluate(value, method, args, contextMap, String.class);
	}

}
