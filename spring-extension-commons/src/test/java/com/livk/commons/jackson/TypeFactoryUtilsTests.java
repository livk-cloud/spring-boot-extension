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
import tools.jackson.databind.type.TypeFactory;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
class TypeFactoryUtilsTests {

	@Test
	void instanceReturnsTypeFactory() {
		assertThat(TypeFactoryUtils.instance()).isNotNull().isInstanceOf(TypeFactory.class);
	}

	// --- javaType from Class ---

	@Test
	void javaTypeFromClass() {
		JavaType type = TypeFactoryUtils.javaType(String.class);
		assertThat(type.getRawClass()).isEqualTo(String.class);
		assertThat(type.hasGenericTypes()).isFalse();
	}

	@Test
	void javaTypeFromClassWithClassGenerics() {
		JavaType type = TypeFactoryUtils.javaType(List.class, String.class);
		assertThat(type.getRawClass()).isEqualTo(List.class);
		assertThat(type.getBindings().getBoundType(0).getRawClass()).isEqualTo(String.class);
	}

	@Test
	void javaTypeFromClassWithJavaTypeGenerics() {
		JavaType strType = TypeFactoryUtils.javaType(String.class);
		JavaType type = TypeFactoryUtils.javaType(List.class, strType);
		assertThat(type.getRawClass()).isEqualTo(List.class);
		assertThat(type.getBindings().getBoundType(0)).isEqualTo(strType);
	}

	// --- javaType from TypeReference ---

	@Test
	void javaTypeFromTypeReferenceSimple() {
		JavaType type = TypeFactoryUtils.javaType(new TypeReference<String>() {
		});
		assertThat(type.getRawClass()).isEqualTo(String.class);
	}

	@Test
	void javaTypeFromTypeReferenceParameterized() {
		JavaType type = TypeFactoryUtils.javaType(new TypeReference<List<String>>() {
		});
		assertThat(type.getRawClass()).isEqualTo(List.class);
		assertThat(type.getBindings().getBoundType(0).getRawClass()).isEqualTo(String.class);
	}

	@Test
	void javaTypeFromTypeReferenceMap() {
		JavaType type = TypeFactoryUtils.javaType(new TypeReference<Map<String, Integer>>() {
		});
		assertThat(type.getRawClass()).isEqualTo(Map.class);
		assertThat(type.getBindings().getBoundType(0).getRawClass()).isEqualTo(String.class);
		assertThat(type.getBindings().getBoundType(1).getRawClass()).isEqualTo(Integer.class);
	}

	// --- javaType from ResolvableType ---

	@Test
	void javaTypeFromResolvableTypeSimple() {
		JavaType type = TypeFactoryUtils.javaType(ResolvableType.forClass(String.class));
		assertThat(type.getRawClass()).isEqualTo(String.class);
	}

	@Test
	void javaTypeFromResolvableTypeParameterized() {
		JavaType type = TypeFactoryUtils.javaType(ResolvableType.forClassWithGenerics(List.class, String.class));
		assertThat(type.getRawClass()).isEqualTo(List.class);
		assertThat(type.getBindings().getBoundType(0).getRawClass()).isEqualTo(String.class);
	}

	@Test
	void javaTypeFromResolvableTypeMap() {
		JavaType type = TypeFactoryUtils
			.javaType(ResolvableType.forClassWithGenerics(Map.class, String.class, Integer.class));
		assertThat(type.getRawClass()).isEqualTo(Map.class);
		assertThat(type.getBindings().getBoundType(0).getRawClass()).isEqualTo(String.class);
		assertThat(type.getBindings().getBoundType(1).getRawClass()).isEqualTo(Integer.class);
	}

	// --- listType ---

	@Test
	void listTypeFromClass() {
		CollectionType type = TypeFactoryUtils.listType(String.class);
		assertThat(type.getRawClass()).isEqualTo(List.class);
		assertThat(type.getBindings().getBoundType(0).getRawClass()).isEqualTo(String.class);
	}

	@Test
	void listTypeFromJavaType() {
		JavaType strType = TypeFactoryUtils.javaType(String.class);
		CollectionType type = TypeFactoryUtils.listType(strType);
		assertThat(type.getRawClass()).isEqualTo(List.class);
		assertThat(type.getBindings().getBoundType(0)).isEqualTo(strType);
	}

	// --- setType ---

	@Test
	void setTypeFromClass() {
		CollectionType type = TypeFactoryUtils.setType(String.class);
		assertThat(type.getRawClass()).isEqualTo(Set.class);
		assertThat(type.getBindings().getBoundType(0).getRawClass()).isEqualTo(String.class);
	}

	@Test
	void setTypeFromJavaType() {
		JavaType strType = TypeFactoryUtils.javaType(String.class);
		CollectionType type = TypeFactoryUtils.setType(strType);
		assertThat(type.getRawClass()).isEqualTo(Set.class);
		assertThat(type.getBindings().getBoundType(0)).isEqualTo(strType);
	}

	// --- mapType ---

	@Test
	void mapTypeFromClasses() {
		MapType type = TypeFactoryUtils.mapType(String.class, Integer.class);
		assertThat(type.getRawClass()).isEqualTo(Map.class);
		assertThat(type.getBindings().getBoundType(0).getRawClass()).isEqualTo(String.class);
		assertThat(type.getBindings().getBoundType(1).getRawClass()).isEqualTo(Integer.class);
	}

	@Test
	void mapTypeFromJavaTypes() {
		JavaType strType = TypeFactoryUtils.javaType(String.class);
		JavaType intType = TypeFactoryUtils.javaType(Integer.class);
		MapType type = TypeFactoryUtils.mapType(strType, intType);
		assertThat(type.getRawClass()).isEqualTo(Map.class);
		assertThat(type.getBindings().getBoundType(0)).isEqualTo(strType);
		assertThat(type.getBindings().getBoundType(1)).isEqualTo(intType);
	}

	// --- cross-method consistency ---

	@Test
	void allJavaTypeOverloadsProduceConsistentResults() {
		JavaType fromClass = TypeFactoryUtils.javaType(List.class, String.class);
		JavaType fromTypeRef = TypeFactoryUtils.javaType(new TypeReference<List<String>>() {
		});
		JavaType fromResolvable = TypeFactoryUtils
			.javaType(ResolvableType.forClassWithGenerics(List.class, String.class));

		assertThat(fromClass).isEqualTo(fromTypeRef);
		assertThat(fromTypeRef).isEqualTo(fromResolvable);
	}

}
