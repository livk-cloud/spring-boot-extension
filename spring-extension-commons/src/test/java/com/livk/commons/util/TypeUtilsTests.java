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
import org.springframework.util.ClassUtils;

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
class TypeUtilsTests {

	// --- toClass ---

	@Test
	void toClassFromPlainClass() {
		assertThat(TypeUtils.toClass(String.class)).isEqualTo(String.class);
	}

	@Test
	void toClassFromParameterizedType() {
		assertThat(TypeUtils.toClass(new ListType())).isEqualTo(List.class);
	}

	@Test
	void toClassFromTypeVariable() {
		TypeVariable<?> typeVar = MyGeneric.class.getTypeParameters()[0];
		assertThat(TypeUtils.toClass(typeVar)).isEqualTo(Number.class);
	}

	@Test
	void toClassFromTypeVariableWithObjectBoundReturnsObject() {
		TypeVariable<?> typeVar = UnboundedGeneric.class.getTypeParameters()[0];
		assertThat(TypeUtils.toClass(typeVar)).isEqualTo(Object.class);
	}

	@Test
	void toClassFromGenericArrayType() {
		assertThat(TypeUtils.toClass(new StringGenericArrayType())).isEqualTo(String[].class);
	}

	@Test
	void toClassFromWildcardTypeWithUpperBound() {
		assertThat(TypeUtils.toClass(new NumberWildcardType())).isEqualTo(Number.class);
	}

	@Test
	void toClassFromWildcardTypeWithLowerBound() {
		WildcardType lowerBounded = new WildcardType() {
			@NonNull
			@Override
			public Type[] getUpperBounds() {
				return new Type[] { Object.class };
			}

			@NonNull
			@Override
			public Type[] getLowerBounds() {
				return new Type[] { String.class };
			}
		};
		assertThat(TypeUtils.toClass(lowerBounded)).isEqualTo(String.class);
	}

	@Test
	void toClassWithNullThrows() {
		assertThatThrownBy(() -> TypeUtils.toClass(null)).isInstanceOf(IllegalArgumentException.class)
			.hasMessage("Type cannot be null");
	}

	// --- resolveTypeArgument ---

	@Test
	void resolveTypeArgumentResolvesConcreteGeneric() {
		assertThat(TypeUtils.resolveTypeArgument(IntGeneric.class, MyGeneric.class)).isEqualTo(Integer.class);
	}

	@Test
	void resolveTypeArgumentThrowsWhenNoGenerics() {
		assertThatThrownBy(() -> TypeUtils.resolveTypeArgument(NumberWildcardType.class, WildcardType.class))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("No type arguments found");
	}

	@Test
	void resolveTypeArgumentThrowsWhenMultipleGenerics() {
		assertThatThrownBy(() -> TypeUtils.resolveTypeArgument(TypeMap.class, HashMap.class))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("Expected 1 type argument");
	}

	// --- resolveClassName / isPresent (Spring ClassUtils) ---

	@Test
	void resolveClassNameResolvesExistingClass() {
		assertThat(ClassUtils.resolveClassName("java.lang.String", ClassUtils.getDefaultClassLoader()))
			.isEqualTo(String.class);
	}

	@Test
	void isPresentReturnsTrueForExistingClass() {
		assertThat(ClassUtils.isPresent("java.lang.String", ClassUtils.getDefaultClassLoader())).isTrue();
	}

	@Test
	void isPresentReturnsFalseForNonexistentClass() {
		assertThat(ClassUtils.isPresent("com.livk.nonexistent.FakeClass", ClassUtils.getDefaultClassLoader()))
			.isFalse();
	}

	// --- test helper types ---

	static class TypeMap extends HashMap<String, Type> {

	}

	static class IntGeneric extends MyGeneric<Integer> {

	}

	static class MyGeneric<T extends Number> {

	}

	static class UnboundedGeneric<T> {

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

}
