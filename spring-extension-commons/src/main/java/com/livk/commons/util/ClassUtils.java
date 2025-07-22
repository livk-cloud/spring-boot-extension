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

package com.livk.commons.util;

import lombok.experimental.UtilityClass;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

/**
 * <p>
 * class相关工具类
 * </p>
 *
 * @author livk
 */
@UtilityClass
public class ClassUtils extends org.springframework.util.ClassUtils {

	/**
	 * 将Type安全的转成Class
	 * @param <T> type parameter
	 * @param type type
	 * @return class
	 */
	@SuppressWarnings("unchecked")
	public <T> Class<T> toClass(Type type) {
		return switch (type) {
			case null -> throw new IllegalArgumentException("Type cannot be null");
			case ParameterizedType parameterizedType -> toClass(parameterizedType.getRawType());
			case TypeVariable<?> typeVariable -> handleTypeVariable(typeVariable);
			case GenericArrayType genericArrayType -> handleGenericArrayType(genericArrayType);
			case WildcardType wildcardType -> handleWildcardType(wildcardType);
			case Class<?> ignored -> (Class<T>) type;
			default -> throw new IllegalArgumentException(
					"Unsupported Type: " + type.getClass().getName() + ", type: " + type);
		};
	}

	@SuppressWarnings("unchecked")
	private <T> Class<T> handleTypeVariable(TypeVariable<?> typeVariable) {
		for (Type bound : typeVariable.getBounds()) {
			if (bound != Object.class) {
				return toClass(bound);
			}
		}
		return (Class<T>) Object.class;
	}

	@SuppressWarnings("unchecked")
	private <T> Class<T> handleGenericArrayType(GenericArrayType genericArrayType) {
		Type componentType = genericArrayType.getGenericComponentType();
		Class<?> componentClass = toClass(componentType);
		return (Class<T>) Array.newInstance(componentClass, 0).getClass();
	}

	@SuppressWarnings("unchecked")
	private <T> Class<T> handleWildcardType(WildcardType wildcardType) {
		Type[] upperBounds = wildcardType.getUpperBounds();
		if (upperBounds.length > 0 && upperBounds[0] != Object.class) {
			return toClass(upperBounds[0]);
		}
		Type[] lowerBounds = wildcardType.getLowerBounds();
		if (lowerBounds.length > 0) {
			return toClass(lowerBounds[0]);
		}
		return (Class<T>) Object.class;
	}

	/**
	 * 将class全类名转成class
	 * @param className class name
	 * @return class
	 */
	public static Class<?> resolveClassName(String className) {
		return resolveClassName(className, getDefaultClassLoader());
	}

	/**
	 * 判定当前类是否被加载
	 * @param className class name
	 * @return boolean
	 */
	public static boolean isPresent(String className) {
		return isPresent(className, getDefaultClassLoader());
	}

}
