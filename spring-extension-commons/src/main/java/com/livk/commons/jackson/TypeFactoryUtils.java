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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import lombok.experimental.UtilityClass;
import org.springframework.core.ResolvableType;

import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * TypeFactory工具类
 *
 * @author livk
 */
@UtilityClass
public class TypeFactoryUtils {

	/**
	 * 获取TypeFactory
	 * @return TypeFactory
	 */
	public static TypeFactory instance() {
		return TypeFactory.defaultInstance();
	}

	/**
	 * 构建一个JavaType
	 * @param targetClass the target class
	 * @return JavaType
	 */
	public static JavaType javaType(Class<?> targetClass) {
		return instance().constructType(targetClass);
	}

	/**
	 * 构建一个含有泛型的JavaType
	 * @param targetClass the target class
	 * @param generics the generics
	 * @return JavaType
	 */
	public static JavaType javaType(Class<?> targetClass, Class<?>... generics) {
		return instance().constructParametricType(targetClass, generics);
	}

	/**
	 * 构建一个含有泛型的JavaType
	 * @param targetClass the target class
	 * @param generics the generics
	 * @return JavaType
	 */
	public static JavaType javaType(Class<?> targetClass, JavaType... generics) {
		return instance().constructParametricType(targetClass, generics);
	}

	/**
	 * TypeReference转JavaType
	 * @param typeReference the type reference
	 * @return JavaType
	 */
	public static JavaType javaType(TypeReference<?> typeReference) {
		return instance().constructType(typeReference);
	}

	/**
	 * ResolvableType转成Jackson JavaType
	 * @param resolvableType the resolvable type
	 * @return JavaType
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
	 * 构建一个CollectionType
	 * @param <T> the type parameter
	 * @param type the target class
	 * @return CollectionType
	 * @deprecated use {@link #listType(JavaType)} or {@link #setType(JavaType)}
	 */
	@Deprecated(since = "1.4.3")
	public static <T> CollectionType collectionType(Class<T> type) {
		return instance().constructCollectionType(Collection.class, type);
	}

	/**
	 * 构建一个CollectionType
	 * @param javaType the java type
	 * @return CollectionType
	 * @deprecated use {@link #listType(JavaType)} or {@link #setType(JavaType)}
	 */
	@Deprecated(since = "1.4.3")
	public static CollectionType collectionType(JavaType javaType) {
		return instance().constructCollectionType(Collection.class, javaType);
	}

	/**
	 * 构建一个SetType
	 * @param javaType the java type
	 * @return SetType
	 */
	public static CollectionType setType(JavaType javaType) {
		return instance().constructCollectionType(Set.class, javaType);
	}

	/**
	 * 构建一个SetType
	 * @param <T> the type parameter
	 * @param type the target class
	 * @return SetType
	 */
	public static <T> CollectionType setType(Class<T> type) {
		return instance().constructCollectionType(Set.class, type);
	}

	/**
	 * 构建一个ListType
	 * @param <T> the type parameter
	 * @param type the target class
	 * @return ListType
	 */
	public static <T> CollectionType listType(Class<T> type) {
		return instance().constructCollectionType(List.class, type);
	}

	/**
	 * 构建一个ListType
	 * @param javaType the java type
	 * @return ListType
	 */
	public static CollectionType listType(JavaType javaType) {
		return instance().constructCollectionType(List.class, javaType);
	}

	/**
	 * 构建一个MapType
	 * @param <K> the type parameter
	 * @param <V> the type parameter
	 * @param keyClass the key class
	 * @param valueClass the value class
	 * @return MapType
	 */
	public static <K, V> MapType mapType(Class<K> keyClass, Class<V> valueClass) {
		return instance().constructMapType(Map.class, keyClass, valueClass);
	}

	/**
	 * 构建一个MapType
	 * @param kType the k type
	 * @param vType the v type
	 * @return MapType
	 */
	public static MapType mapType(JavaType kType, JavaType vType) {
		return instance().constructMapType(Map.class, kType, vType);
	}

}
