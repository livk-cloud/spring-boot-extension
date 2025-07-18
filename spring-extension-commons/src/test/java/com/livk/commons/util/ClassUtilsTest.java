/*
 * Copyright 2021-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * you may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.livk.commons.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.jspecify.annotations.NonNull;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * <p>
 * ClassUtilsTest
 * </p>
 *
 * @author livk
 */
class ClassUtilsTest {

	@Test
	void toClassTest() {
		assertEquals(String.class, ClassUtils.toClass(String.class));

		Type listType = new ListType();

		assertEquals(List.class, ClassUtils.toClass(listType));

		TypeVariable<?> typeVar = MyGeneric.class.getTypeParameters()[0];
		assertEquals(Number.class, ClassUtils.toClass(typeVar));

		Type arrayType = new StringGenericArrayType();
		assertEquals(String[].class, ClassUtils.toClass(arrayType));

		WildcardType wildcardType = new NumberWildcardType();
		assertEquals(Number.class, ClassUtils.toClass(wildcardType));

		Assertions.assertThrows(IllegalArgumentException.class, () -> ClassUtils.toClass(null), "Type cannot be null");
	}

	static class NumberWildcardType implements WildcardType {

		@NonNull
		@Override
		public Type[] getUpperBounds() {
			return new Type[] { Number.class };
		}

		@NonNull
		@Override
		public Type[] getLowerBounds() {
			return new Type[0];
		}

	}

	static class StringGenericArrayType implements GenericArrayType {

		@NonNull
		@Override
		public Type getGenericComponentType() {
			return String.class;
		}

	}

	static class ListType implements ParameterizedType {

		@NonNull
		@Override
		public Type[] getActualTypeArguments() {
			return new Type[] { String.class };
		}

		@NonNull
		@Override
		public Type getRawType() {
			return List.class;
		}

		@Override
		public Type getOwnerType() {
			return null;
		}

	}

	static class MyGeneric<T extends Number> {

	}

}
