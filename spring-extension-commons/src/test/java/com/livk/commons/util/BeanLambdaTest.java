/*
 * Copyright 2021-2024 spring-boot-extension the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *       https://www.apache.org/licenses/LICENSE-2.0
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

/**
 * @author livk
 */
class BeanLambdaTest {

	final Field field1 = Maker.class.getDeclaredField("no");

	final Field field2 = Maker.class.getDeclaredField("username");

	final Method method1 = Maker.class.getMethod("getNo");

	final Method method2 = Maker.class.getMethod("getUsername");

	BeanLambdaTest() throws NoSuchFieldException, NoSuchMethodException {
	}

	@Test
	void method() {
		assertEquals(method1.getName(), BeanLambda.methodName(Maker::getNo));
		assertEquals(method2.getName(), BeanLambda.methodName(Maker::getUsername));

		assertEquals(method1, BeanLambda.method(Maker::getNo));
		assertEquals(method2, BeanLambda.method(Maker::getUsername));
	}

	@Test
	void field() {
		assertEquals(field1.getName(), BeanLambda.fieldName(Maker::getNo));
		assertEquals(field2.getName(), BeanLambda.fieldName(Maker::getUsername));

		assertEquals(field1, BeanLambda.field(Maker::getNo));
		assertEquals(field2, BeanLambda.field(Maker::getUsername));
	}

	@Data
	static class Maker {

		private Integer no;

		private String username;

	}

}
