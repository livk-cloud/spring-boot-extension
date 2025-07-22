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
import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
class BeanLambdaTests {

	final Field field1 = Maker.class.getDeclaredField("no");

	final Field field2 = Maker.class.getDeclaredField("username");

	final Method method1 = Maker.class.getMethod("getNo");

	final Method method2 = Maker.class.getMethod("getUsername");

	BeanLambdaTests() throws NoSuchFieldException, NoSuchMethodException {
	}

	@Test
	void method() {
		assertThat(BeanLambda.methodName(Maker::getNo)).isEqualTo(method1.getName());
		assertThat(BeanLambda.methodName(Maker::getUsername)).isEqualTo(method2.getName());

		assertThat(BeanLambda.method(Maker::getNo)).isEqualTo(method1);
		assertThat(BeanLambda.method(Maker::getUsername)).isEqualTo(method2);
	}

	@Test
	void field() {
		assertThat(BeanLambda.fieldName(Maker::getNo)).isEqualTo(field1.getName());
		assertThat(BeanLambda.fieldName(Maker::getUsername)).isEqualTo(field2.getName());

		assertThat(BeanLambda.field(Maker::getNo)).isEqualTo(field1);
		assertThat(BeanLambda.field(Maker::getUsername)).isEqualTo(field2);
	}

	@Data
	static class Maker {

		private Integer no;

		private String username;

	}

}
