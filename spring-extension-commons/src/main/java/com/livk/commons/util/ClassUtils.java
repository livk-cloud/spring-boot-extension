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

import lombok.experimental.UtilityClass;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

/**
 * <p>
 * ClassUtils
 * </p>
 *
 * @author livk
 */
@UtilityClass
public class ClassUtils extends org.springframework.util.ClassUtils {

	/**
	 * To class.
	 *
	 * @param <T>  the type parameter
	 * @param type the type
	 * @return the class
	 */
	@SuppressWarnings("unchecked")
	public <T> Class<T> toClass(Type type) {
		if (type instanceof ParameterizedType parameterizedType) {
			return toClass(parameterizedType.getRawType());
		} else if (type instanceof TypeVariable<?> typeVariable) {
			String className = typeVariable.getGenericDeclaration().toString();
			return toClass(resolveClassName(className));
		} else {
			return (Class<T>) type;
		}
	}


	/**
	 * Resolve class name class.
	 *
	 * @param className the class name
	 * @return the class
	 */
	public static Class<?> resolveClassName(String className) {
		return resolveClassName(className, getDefaultClassLoader());
	}

	/**
	 * Is present boolean.
	 *
	 * @param className the class name
	 * @return the boolean
	 */
	public static boolean isPresent(String className) {
		return isPresent(className, getDefaultClassLoader());
	}
}
