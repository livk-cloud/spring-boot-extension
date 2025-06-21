/*
 * Copyright 2021-2024 spring-boot-extension the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
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
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author livk
 */
class BeanLambdaDescriptorTest {

	@Test
	void create() {
		assertNotNull(BeanLambdaDescriptor.create(Maker::getNo));
	}

	@Test
	void getFieldName() {
		assertEquals("no", BeanLambdaDescriptor.create(Maker::getNo).getFieldName());
	}

	@Test
	void getField() throws NoSuchFieldException {
		Field field = Maker.class.getDeclaredField("no");
		assertEquals(field, BeanLambdaDescriptor.create(Maker::getNo).getField());
	}

	@Test
	void getMethodName() {
		assertEquals("getNo", BeanLambdaDescriptor.create(Maker::getNo).getMethodName());
	}

	@Test
	void getMethod() throws NoSuchMethodException {
		Method method = Maker.class.getMethod("getNo");
		assertEquals(method, BeanLambdaDescriptor.create(Maker::getNo).getMethod());
	}

	@Data
	static class Maker {

		private Integer no;

	}

}
