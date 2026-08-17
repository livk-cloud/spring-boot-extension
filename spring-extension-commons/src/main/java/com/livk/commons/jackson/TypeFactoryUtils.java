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

import lombok.experimental.UtilityClass;
import org.springframework.core.ResolvableType;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.type.CollectionType;
import tools.jackson.databind.type.MapType;
import tools.jackson.databind.type.TypeFactory;

import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Utility class for Jackson TypeFactory operations.
 *
 * @author livk
 */
@UtilityClass
public class TypeFactoryUtils {

	/**
	 * Returns the default TypeFactory instance.
	 * @return typeFactory
	 */
	public static TypeFactory instance() {
		return TypeFactory.createDefaultInstance();
	}

	/**
	 * Constructs a JavaType from the given class.
	 * @param targetClass the target class
	 * @return javaType
	 */
	public static JavaType javaType(Class<?> targetClass) {
		return instance().constructType(targetClass);
	}

	/**
	 * Constructs a parametric JavaType with generics.
	 * @param targetClass the target class
	 * @param generics the generics
	 * @return javaType
	 */
	public static JavaType javaType(Class<?> targetClass, Class<?>... generics) {
		return instance().constructParametricType(targetClass, generics);
	}

	/**
	 * Constructs a parametric JavaType with JavaType generics.
	 * @param targetClass the target class
	 * @param generics the generics
	 * @return javaType
	 */
	public static JavaType javaType(Class<?> targetClass, JavaType... generics) {
		return instance().constructParametricType(targetClass, generics);
	}

	/**
	 * Converts TypeReference to JavaType.
	 * @param typeReference the type reference
	 * @return javaType
	 */
	public static JavaType javaType(TypeReference<?> typeReference) {
		return instance().constructType(typeReference);
	}

	/**
	 * Converts ResolvableType to Jackson JavaType.
	 * @param resolvableType the resolvable type
	 * @return javaType
	 * @see ResolvableType
	 */
	public static JavaType javaType(ResolvableType resolvableType) {
		Class<?> rawClass = resolvableType.getRawClass();
		if (resolvableType.getType() instanceof ParameterizedType parameterizedType) {
			JavaType[] javaTypes = Arrays.stream(parameterizedType.getActualTypeArguments())
				.map(type -> javaType(ResolvableType.forType(type)))
				.toArray(JavaType[]::new);
			return instance().constructParametricType(rawClass, javaTypes);
		}
		return javaType(rawClass);
	}

	/**
	 * Constructs a Set CollectionType from JavaType.
	 * @param javaType the java type
	 * @return setType
	 */
	public static CollectionType setType(JavaType javaType) {
		return instance().constructCollectionType(Set.class, javaType);
	}

	/**
	 * Constructs a Set CollectionType from Class.
	 * @param <T> the type parameter
	 * @param type the target class
	 * @return setType
	 */
	public static <T> CollectionType setType(Class<T> type) {
		return instance().constructCollectionType(Set.class, type);
	}

	/**
	 * Constructs a List CollectionType from Class.
	 * @param <T> the type parameter
	 * @param type the target class
	 * @return listType
	 */
	public static <T> CollectionType listType(Class<T> type) {
		return instance().constructCollectionType(List.class, type);
	}

	/**
	 * Constructs a List CollectionType from JavaType.
	 * @param javaType the java type
	 * @return listType
	 */
	public static CollectionType listType(JavaType javaType) {
		return instance().constructCollectionType(List.class, javaType);
	}

	/**
	 * Constructs a MapType from key and value classes.
	 * @param <K> the type parameter
	 * @param <V> the type parameter
	 * @param keyClass the key class
	 * @param valueClass the value class
	 * @return mapType
	 */
	public static <K, V> MapType mapType(Class<K> keyClass, Class<V> valueClass) {
		return instance().constructMapType(Map.class, keyClass, valueClass);
	}

	/**
	 * Constructs a MapType from key and value JavaTypes.
	 * @param kType the k type
	 * @param vType the v type
	 * @return mapType
	 */
	public static MapType mapType(JavaType kType, JavaType vType) {
		return instance().constructMapType(Map.class, kType, vType);
	}

}
