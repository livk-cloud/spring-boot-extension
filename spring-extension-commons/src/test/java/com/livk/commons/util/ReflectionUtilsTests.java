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
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
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

	@Test
	void setFieldAndAccessibleTest() {
		Maker maker = new Maker();
		ReflectionUtils.setFieldAndAccessible(fieldNo, maker, 2);
		assertThat(maker.getNo()).isEqualTo(2);
	}

	@Test
	void getReadMethod() throws InvocationTargetException, IllegalAccessException {
		Maker maker = new Maker();
		maker.setNo(10);
		maker.setUsername("root");
		Method readMethod = ReflectionUtils.getReadMethod(Maker.class, fieldNo);
		assertThat(readMethod.getName()).isEqualTo("getNo");
		assertThat(readMethod.invoke(maker)).isEqualTo(10);

		Set<Method> readMethods = ReflectionUtils.getReadMethods(Maker.class);
		Set<String> methodNames = readMethods.stream().map(Method::getName).collect(Collectors.toSet());
		assertThat(methodNames).containsExactlyInAnyOrder("getNo", "getUsername");
	}

	@Test
	void getWriteMethod() throws InvocationTargetException, IllegalAccessException {
		Maker maker = new Maker();
		maker.setNo(10);
		maker.setUsername("root");
		Method writeMethod = ReflectionUtils.getWriteMethod(Maker.class, fieldNo);
		assertThat(writeMethod.getName()).isEqualTo("setNo");

		writeMethod.invoke(maker, 20);
		assertThat(maker.getNo()).isEqualTo(20);

		Set<Method> writeMethods = ReflectionUtils.getWriteMethods(Maker.class);
		Set<String> methodNames = writeMethods.stream().map(Method::getName).collect(Collectors.toSet());
		assertThat(methodNames).containsExactlyInAnyOrder("setNo", "setUsername");
	}

	@Test
	void getAllFields() {
		List<Field> fields = ReflectionUtils.getAllFields(Maker.class);
		List<String> fieldNames = fields.stream().map(Field::getName).collect(Collectors.toList());
		assertThat(fieldNames).containsExactly("no", "username");
	}

	@Data
	static class Maker {

		private Integer no;

		private String username;

	}

}
