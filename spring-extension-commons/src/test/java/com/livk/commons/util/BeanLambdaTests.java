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

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
class BeanLambdaTests {

	@Test
	void methodNameReturnsGetterName() {
		assertThat(BeanLambda.methodName(Maker::getNo)).isEqualTo("getNo");
		assertThat(BeanLambda.methodName(Maker::getUsername)).isEqualTo("getUsername");
	}

	@Test
	void methodReturnsGetterMethod() throws NoSuchMethodException {
		assertThat(BeanLambda.method(Maker::getNo)).isEqualTo(Maker.class.getMethod("getNo"));
		assertThat(BeanLambda.method(Maker::getUsername)).isEqualTo(Maker.class.getMethod("getUsername"));
	}

	@Test
	void fieldNameReturnsPropertyName() {
		assertThat(BeanLambda.fieldName(Maker::getNo)).isEqualTo("no");
		assertThat(BeanLambda.fieldName(Maker::getUsername)).isEqualTo("username");
	}

	@Test
	void fieldReturnsMatchingDeclaredField() throws NoSuchFieldException {
		assertThat(BeanLambda.field(Maker::getNo)).isEqualTo(Maker.class.getDeclaredField("no"));
		assertThat(BeanLambda.field(Maker::getUsername)).isEqualTo(Maker.class.getDeclaredField("username"));
	}

	@Test
	void fieldTypeMatchesPropertyType() {
		assertThat(BeanLambda.field(Maker::getNo).getType()).isEqualTo(Integer.class);
		assertThat(BeanLambda.field(Maker::getUsername).getType()).isEqualTo(String.class);
	}

	@Data
	static class Maker {

		private Integer no;

		private String username;

	}

}
