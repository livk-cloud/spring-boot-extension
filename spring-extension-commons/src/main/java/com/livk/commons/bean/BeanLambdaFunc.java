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

package com.livk.commons.bean;

import lombok.SneakyThrows;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.function.Function;

/**
 * The interface Field function.
 *
 * @param <T> the type parameter
 * @author livk
 */
@FunctionalInterface
public interface BeanLambdaFunc<T> extends Function<T, Object>, Serializable {

	/**
	 * Method name
	 *
	 * @param <T>      the type parameter
	 * @param function the function
	 * @return the string
	 */
	static <T> String methodName(BeanLambdaFunc<T> function) {
		return method(function).getName();
	}

	/**
	 * Method
	 *
	 * @param <T>      the type parameter
	 * @param function the function
	 * @return the method
	 */
	static <T> Method method(BeanLambdaFunc<T> function) {
		return BeanLambdaDescriptor.create(function).getMethod();
	}

	/**
	 * Gets field name.
	 *
	 * @param <T>      the type parameter
	 * @param function the function
	 * @return the field name
	 */
	@SneakyThrows
	static <T> String fieldName(BeanLambdaFunc<T> function) {
		Field field = field(function);
		return field == null ? null : field.getName();
	}

	/**
	 * Gets field.
	 *
	 * @param <T>      the type parameter
	 * @param function the function
	 * @return the field
	 */
	@SneakyThrows
	static <T> Field field(BeanLambdaFunc<T> function) {
		return BeanLambdaDescriptor.create(function).getField();
	}
}
