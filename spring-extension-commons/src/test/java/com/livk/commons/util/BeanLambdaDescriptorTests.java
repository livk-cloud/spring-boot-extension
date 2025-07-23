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
class BeanLambdaDescriptorTests {

	@Test
	void create() {
		assertThat(BeanLambdaDescriptor.create(Maker::getNo)).isNotNull();
	}

	@Test
	void getFieldName() {
		assertThat(BeanLambdaDescriptor.create(Maker::getNo).getFieldName()).isEqualTo("no");
	}

	@Test
	void getField() throws NoSuchFieldException {
		Field field = Maker.class.getDeclaredField("no");
		assertThat(BeanLambdaDescriptor.create(Maker::getNo).getField()).isEqualTo(field);
	}

	@Test
	void getMethodName() {
		assertThat(BeanLambdaDescriptor.create(Maker::getNo).getMethodName()).isEqualTo("getNo");
	}

	@Test
	void getMethod() throws NoSuchMethodException {
		Method method = Maker.class.getMethod("getNo");
		assertThat(BeanLambdaDescriptor.create(Maker::getNo).getMethod()).isEqualTo(method);
	}

	@Data
	static class Maker {

		private Integer no;

	}

}
