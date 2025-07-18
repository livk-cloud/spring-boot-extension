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

import lombok.Data;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertLinesMatch;

/**
 * <p>
 * ReflectionUtilsTest
 * </p>
 *
 * @author livk
 */
class ReflectionUtilsTest {

	final Field fieldNo = Maker.class.getDeclaredField("no");

	ReflectionUtilsTest() throws NoSuchFieldException {
	}

	@Test
	void setFieldAndAccessibleTest() {
		Maker maker = new Maker();
		ReflectionUtils.setFieldAndAccessible(fieldNo, maker, 2);
		assertEquals(2, maker.getNo());
	}

	@Test
	void getReadMethod() throws InvocationTargetException, IllegalAccessException {
		Maker maker = new Maker();
		maker.setNo(10);
		maker.setUsername("root");
		Method readMethod = ReflectionUtils.getReadMethod(Maker.class, fieldNo);
		assertEquals("getNo", readMethod.getName());
		assertEquals(10, readMethod.invoke(maker));

		Set<Method> readMethods = ReflectionUtils.getReadMethods(Maker.class);
		Set<String> set = readMethods.stream().map(Method::getName).collect(Collectors.toSet());
		assertEquals(Set.of("getNo", "getUsername"), set);
	}

	@Test
	void getWriteMethod() throws InvocationTargetException, IllegalAccessException {
		Maker maker = new Maker();
		maker.setNo(10);
		maker.setUsername("root");
		Method writeMethod = ReflectionUtils.getWriteMethod(Maker.class, fieldNo);
		assertEquals("setNo", writeMethod.getName());
		writeMethod.invoke(maker, 20);
		assertEquals(20, maker.getNo());

		Set<Method> writeMethods = ReflectionUtils.getWriteMethods(Maker.class);
		Set<String> set = writeMethods.stream().map(Method::getName).collect(Collectors.toSet());
		assertEquals(Set.of("setNo", "setUsername"), set);
	}

	@Test
	void getAllFields() {
		List<Field> fieldList = ReflectionUtils.getAllFields(Maker.class);
		List<String> list = fieldList.stream().map(Field::getName).collect(Collectors.toList());
		assertLinesMatch(List.of("no", "username"), list);
	}

	@Data
	static class Maker {

		private Integer no;

		private String username;

	}

}
