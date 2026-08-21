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
class FieldUtilsTests {

	final Field fieldNo = Maker.class.getDeclaredField("no");

	FieldUtilsTests() throws NoSuchFieldException {
	}

	@Test
	void setFieldAndAccessible() {
		Maker maker = new Maker();
		FieldUtils.setFieldAndAccessible(fieldNo, maker, 2);
		assertThat(maker.getNo()).isEqualTo(2);
	}

	@Test
	void getReadMethodReturnsGetter() {
		Method readMethod = FieldUtils.getReadMethod(Maker.class, fieldNo);
		assertThat(readMethod).isNotNull();
		assertThat(readMethod.getName()).isEqualTo("getNo");
	}

	@Test
	void getReadMethodInvokesCorrectly() throws Exception {
		Maker maker = new Maker();
		maker.setNo(10);
		Method readMethod = FieldUtils.getReadMethod(Maker.class, fieldNo);
		assertThat(readMethod.invoke(maker)).isEqualTo(10);
	}

	@Test
	void getReadMethodsReturnsAllGetters() {
		Set<Method> readMethods = FieldUtils.getReadMethods(Maker.class);
		Set<String> names = readMethods.stream().map(Method::getName).collect(Collectors.toSet());
		assertThat(names).containsExactlyInAnyOrder("getNo", "getUsername");
	}

	@Test
	void getWriteMethodReturnsSetter() {
		Method writeMethod = FieldUtils.getWriteMethod(Maker.class, fieldNo);
		assertThat(writeMethod).isNotNull();
		assertThat(writeMethod.getName()).isEqualTo("setNo");
	}

	@Test
	void getWriteMethodInvokesCorrectly() throws Exception {
		Maker maker = new Maker();
		Method writeMethod = FieldUtils.getWriteMethod(Maker.class, fieldNo);
		writeMethod.invoke(maker, 20);
		assertThat(maker.getNo()).isEqualTo(20);
	}

	@Test
	void getWriteMethodsReturnsAllSetters() {
		Set<Method> writeMethods = FieldUtils.getWriteMethods(Maker.class);
		Set<String> names = writeMethods.stream().map(Method::getName).collect(Collectors.toSet());
		assertThat(names).containsExactlyInAnyOrder("setNo", "setUsername");
	}

	@Test
	void getAllFieldsReturnsDeclaredFields() {
		List<String> fieldNames = FieldUtils.getAllFields(Maker.class)
			.stream()
			.map(Field::getName)
			.collect(Collectors.toList());
		assertThat(fieldNames).containsExactly("no", "username");
	}

	@Test
	void getAllFieldsIncludesParentFields() {
		List<String> fieldNames = FieldUtils.getAllFields(SubMaker.class)
			.stream()
			.map(Field::getName)
			.collect(Collectors.toList());
		assertThat(fieldNames).containsExactly("extra", "no", "username");
	}

	@Test
	void getDeclaredFieldValueReadsPrivateField() {
		Maker maker = new Maker();
		maker.setNo(42);
		Object value = FieldUtils.getDeclaredFieldValue(fieldNo, maker);
		assertThat(value).isEqualTo(42);
	}

	@Test
	void getDeclaredFieldValueReturnsNullForUnsetField() {
		Maker maker = new Maker();
		Object value = FieldUtils.getDeclaredFieldValue(fieldNo, maker);
		assertThat(value).isNull();
	}

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
