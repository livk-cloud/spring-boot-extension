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

import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Test;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author livk
 */
class ClassUtilsTests {

	@Test
	void toClassTest() {
		assertThat(ClassUtils.toClass(String.class)).isEqualTo(String.class);

		Type listType = new ListType();
		assertThat(ClassUtils.toClass(listType)).isEqualTo(List.class);

		TypeVariable<?> typeVar = MyGeneric.class.getTypeParameters()[0];
		assertThat(ClassUtils.toClass(typeVar)).isEqualTo(Number.class);

		Type arrayType = new StringGenericArrayType();
		assertThat(ClassUtils.toClass(arrayType)).isEqualTo(String[].class);

		WildcardType wildcardType = new NumberWildcardType();
		assertThat(ClassUtils.toClass(wildcardType)).isEqualTo(Number.class);

		assertThatThrownBy(() -> ClassUtils.toClass(null)).isInstanceOf(IllegalArgumentException.class)
			.hasMessage("Type cannot be null");
	}

	@Test
	void resolveTypeArgument() {
		assertThatThrownBy(() -> ClassUtils.resolveTypeArgument(NumberWildcardType.class, WildcardType.class))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("No type arguments found on generic interface [" + WildcardType.class.getName() + "]");

		assertThat(ClassUtils.resolveTypeArgument(IntGeneric.class, MyGeneric.class)).isEqualTo(Integer.class);

		assertThatThrownBy(() -> ClassUtils.resolveTypeArgument(TypeMap.class, HashMap.class))
			.isInstanceOf(IllegalArgumentException.class);
	}

	static class TypeMap extends HashMap<String, Type> {

	}

	static class IntGeneric extends MyGeneric<Integer> {

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
