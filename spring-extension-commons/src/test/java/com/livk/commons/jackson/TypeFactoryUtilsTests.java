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

import org.junit.jupiter.api.Test;
import org.springframework.core.ResolvableType;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.type.CollectionType;
import tools.jackson.databind.type.MapType;
import tools.jackson.databind.type.SimpleType;
import tools.jackson.databind.type.TypeBindings;
import tools.jackson.databind.type.TypeFactory;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
class TypeFactoryUtilsTests {

	static SimpleType strType = SimpleType.constructUnsafe(String.class);

	@Test
	void instance() {
		assertThat(TypeFactoryUtils.instance()).isNotNull();
	}

	@Test
	void testJavaTypeForSimpleType() {
		assertThat(TypeFactoryUtils.javaType(String.class)).isEqualTo(strType);
		assertThat(TypeFactoryUtils.javaType(new TypeReference<String>() {
		})).isEqualTo(strType);
		assertThat(TypeFactoryUtils.javaType(ResolvableType.forClass(String.class))).isEqualTo(strType);
	}

	@Test
	void testJavaTypeForListType() {
		TypeBindings bindings = TypeBindings.createIfNeeded(List.class, strType);
		CollectionType collectionType = CollectionType.construct(List.class, bindings,
				SimpleType.constructUnsafe(Collection.class), null, strType);

		assertThat(TypeFactoryUtils.javaType(List.class, String.class)).isEqualTo(collectionType);
		assertThat(TypeFactoryUtils.javaType(List.class, strType)).isEqualTo(collectionType);
		assertThat(TypeFactoryUtils.javaType(new TypeReference<List<String>>() {
		})).isEqualTo(collectionType);
		assertThat(TypeFactoryUtils.javaType(ResolvableType.forClassWithGenerics(List.class, String.class)))
			.isEqualTo(collectionType);
	}

	@Test
	void testJavaTypeForMapType() {
		TypeBindings bindings = TypeBindings.createIfNeeded(Map.class, new JavaType[] { strType, strType });
		MapType mapType = MapType.construct(Map.class, bindings, TypeFactory.unknownType(), null, strType, strType);

		assertThat(TypeFactoryUtils.javaType(Map.class, String.class, String.class)).isEqualTo(mapType);
		assertThat(TypeFactoryUtils.javaType(Map.class, strType, strType)).isEqualTo(mapType);
		assertThat(TypeFactoryUtils.javaType(new TypeReference<Map<String, String>>() {
		})).isEqualTo(mapType);
		assertThat(
				TypeFactoryUtils.javaType(ResolvableType.forClassWithGenerics(Map.class, String.class, String.class)))
			.isEqualTo(mapType);
	}

	@Test
	void listType() {
		TypeBindings bindings = TypeBindings.createIfNeeded(List.class, strType);
		CollectionType listType = CollectionType.construct(List.class, bindings,
				SimpleType.constructUnsafe(Iterable.class), null, strType);
		assertThat(TypeFactoryUtils.listType(String.class)).isEqualTo(listType);
		assertThat(TypeFactoryUtils.listType(strType)).isEqualTo(listType);
	}

	@Test
	void setType() {
		TypeBindings bindings = TypeBindings.createIfNeeded(Set.class, strType);
		CollectionType setType = CollectionType.construct(Set.class, bindings,
				SimpleType.constructUnsafe(Iterable.class), null, strType);
		assertThat(TypeFactoryUtils.setType(String.class)).isEqualTo(setType);
		assertThat(TypeFactoryUtils.setType(strType)).isEqualTo(setType);
	}

	@Test
	void mapType() {
		TypeBindings bindings = TypeBindings.createIfNeeded(Map.class, new JavaType[] { strType, strType });
		MapType mapType = MapType.construct(Map.class, bindings, TypeFactory.unknownType(), null, strType, strType);
		assertThat(TypeFactoryUtils.mapType(String.class, String.class)).isEqualTo(mapType);
		assertThat(TypeFactoryUtils.mapType(strType, strType)).isEqualTo(mapType);
	}

}
