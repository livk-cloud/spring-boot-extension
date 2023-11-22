/*
 * Copyright 2021 spring-boot-extension the original author or authors.
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
 *
 */

package com.livk.commons.beans;

import lombok.Data;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author livk
 */
class BeanLambdaFuncTest {

	final Field field1 = Maker.class.getDeclaredField("no");

	final Field field2 = Maker.class.getDeclaredField("username");

	final Method method1 = Maker.class.getMethod("getNo");

	final Method method2 = Maker.class.getMethod("getUsername");

	BeanLambdaFuncTest() throws NoSuchFieldException, NoSuchMethodException {
	}

	@Test
	void method() {
		assertEquals(method1.getName(), BeanLambdaFunc.methodName(Maker::getNo));
		assertEquals(method2.getName(), BeanLambdaFunc.methodName(Maker::getUsername));

		assertEquals(method1, BeanLambdaFunc.method(Maker::getNo));
		assertEquals(method2, BeanLambdaFunc.method(Maker::getUsername));
	}

	@Test
	void field() {
		assertEquals(field1.getName(), BeanLambdaFunc.fieldName(Maker::getNo));
		assertEquals(field2.getName(), BeanLambdaFunc.fieldName(Maker::getUsername));

		assertEquals(field1, BeanLambdaFunc.field(Maker::getNo));
		assertEquals(field2, BeanLambdaFunc.field(Maker::getUsername));
	}

	@Data
	static class Maker {

		private Integer no;

		private String username;

	}

}
