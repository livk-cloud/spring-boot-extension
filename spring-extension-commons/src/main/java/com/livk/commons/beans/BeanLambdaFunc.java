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

package com.livk.commons.beans;

import lombok.SneakyThrows;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.function.Function;

/**
 * 用于使用lambda获取相关字段已经方法
 *
 * @param <T> the type parameter
 * @author livk
 */
@FunctionalInterface
public interface BeanLambdaFunc<T> extends Function<T, Object>, Serializable {

	/**
	 * 获取方法名称
	 * @param <T> 相关泛型
	 * @param function BeanLambdaFunc表达式
	 * @return methodName
	 */
	static <T> String methodName(BeanLambdaFunc<T> function) {
		return BeanLambdaDescriptor.create(function).getMethodName();
	}

	/**
	 * 获取方法
	 * @param <T> 相关泛型
	 * @param function BeanLambdaFunc表达式
	 * @return method
	 */
	static <T> Method method(BeanLambdaFunc<T> function) {
		return BeanLambdaDescriptor.create(function).getMethod();
	}

	/**
	 * 获取字段名称
	 * @param <T> 相关泛型
	 * @param function BeanLambdaFunc表达式
	 * @return fieldName
	 */
	@SneakyThrows
	static <T> String fieldName(BeanLambdaFunc<T> function) {
		return BeanLambdaDescriptor.create(function).getFieldName();
	}

	/**
	 * 获取字段
	 * @param <T> 相关泛型
	 * @param function BeanLambdaFunc表达式
	 * @return field
	 */
	@SneakyThrows
	static <T> Field field(BeanLambdaFunc<T> function) {
		return BeanLambdaDescriptor.create(function).getField();
	}

}
