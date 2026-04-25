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

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
class ReflectionUtilsTests {

	final Field fieldNo = Maker.class.getDeclaredField("no");

	ReflectionUtilsTests() throws NoSuchFieldException {
	}

	// --- setFieldAndAccessible ---

	@Test
	void setFieldAndAccessible() {
		Maker maker = new Maker();
		ReflectionUtils.setFieldAndAccessible(fieldNo, maker, 2);
		assertThat(maker.getNo()).isEqualTo(2);
	}

	// --- getReadMethod / getReadMethods ---

	@Test
	void getReadMethodReturnsGetter() {
		Method readMethod = ReflectionUtils.getReadMethod(Maker.class, fieldNo);
		assertThat(readMethod).isNotNull();
		assertThat(readMethod.getName()).isEqualTo("getNo");
	}

	@Test
	void getReadMethodInvokesCorrectly() throws Exception {
		Maker maker = new Maker();
		maker.setNo(10);
		Method readMethod = ReflectionUtils.getReadMethod(Maker.class, fieldNo);
		assertThat(readMethod.invoke(maker)).isEqualTo(10);
	}

	@Test
	void getReadMethodsReturnsAllGetters() {
		Set<Method> readMethods = ReflectionUtils.getReadMethods(Maker.class);
		Set<String> names = readMethods.stream().map(Method::getName).collect(Collectors.toSet());
		assertThat(names).containsExactlyInAnyOrder("getNo", "getUsername");
	}

	// --- getWriteMethod / getWriteMethods ---

	@Test
	void getWriteMethodReturnsSetter() {
		Method writeMethod = ReflectionUtils.getWriteMethod(Maker.class, fieldNo);
		assertThat(writeMethod).isNotNull();
		assertThat(writeMethod.getName()).isEqualTo("setNo");
	}

	@Test
	void getWriteMethodInvokesCorrectly() throws Exception {
		Maker maker = new Maker();
		Method writeMethod = ReflectionUtils.getWriteMethod(Maker.class, fieldNo);
		writeMethod.invoke(maker, 20);
		assertThat(maker.getNo()).isEqualTo(20);
	}

	@Test
	void getWriteMethodsReturnsAllSetters() {
		Set<Method> writeMethods = ReflectionUtils.getWriteMethods(Maker.class);
		Set<String> names = writeMethods.stream().map(Method::getName).collect(Collectors.toSet());
		assertThat(names).containsExactlyInAnyOrder("setNo", "setUsername");
	}

	// --- getAllFields ---

	@Test
	void getAllFieldsReturnsDeclaredFields() {
		List<String> fieldNames = ReflectionUtils.getAllFields(Maker.class)
			.stream()
			.map(Field::getName)
			.collect(Collectors.toList());
		assertThat(fieldNames).containsExactly("no", "username");
	}

	@Test
	void getAllFieldsIncludesParentFields() {
		List<String> fieldNames = ReflectionUtils.getAllFields(SubMaker.class)
			.stream()
			.map(Field::getName)
			.collect(Collectors.toList());
		assertThat(fieldNames).containsExactly("extra", "no", "username");
	}

	// --- getDeclaredFieldValue ---

	@Test
	void getDeclaredFieldValueReadsPrivateField() {
		Maker maker = new Maker();
		maker.setNo(42);
		Object value = ReflectionUtils.getDeclaredFieldValue(fieldNo, maker);
		assertThat(value).isEqualTo(42);
	}

	@Test
	void getDeclaredFieldValueReturnsNullForUnsetField() {
		Maker maker = new Maker();
		Object value = ReflectionUtils.getDeclaredFieldValue(fieldNo, maker);
		assertThat(value).isNull();
	}

	// --- test helper types ---

	@Data
	static class Maker {

		private Integer no;

		private String username;

	}

	@Data
	@EqualsAndHashCode(callSuper = true)
	static class SubMaker extends Maker {

		private String extra;

	}

}
