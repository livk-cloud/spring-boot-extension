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

	final BeanLambdaDescriptor noDescriptor = BeanLambdaDescriptor.create(Maker::getNo);

	final BeanLambdaDescriptor usernameDescriptor = BeanLambdaDescriptor.create(Maker::getUsername);

	@Test
	void createReturnsNonNull() {
		assertThat(noDescriptor).isNotNull();
		assertThat(usernameDescriptor).isNotNull();
	}

	@Test
	void createReturnsCachedInstance() {
		BeanLambdaDescriptor another = BeanLambdaDescriptor.create(Maker::getNo);
		assertThat(another).isSameAs(noDescriptor);
	}

	@Test
	void createReturnsDifferentInstancesForDifferentLambdas() {
		assertThat(noDescriptor).isNotSameAs(usernameDescriptor);
	}

	@Test
	void getFieldNameForNo() {
		assertThat(noDescriptor.getFieldName()).isEqualTo("no");
	}

	@Test
	void getFieldNameForUsername() {
		assertThat(usernameDescriptor.getFieldName()).isEqualTo("username");
	}

	@Test
	void getFieldReturnsCorrectField() throws NoSuchFieldException {
		Field expected = Maker.class.getDeclaredField("no");
		assertThat(noDescriptor.getField()).isEqualTo(expected);
		assertThat(noDescriptor.getField().getType()).isEqualTo(Integer.class);
	}

	@Test
	void getFieldForUsernameReturnsCorrectField() throws NoSuchFieldException {
		Field expected = Maker.class.getDeclaredField("username");
		assertThat(usernameDescriptor.getField()).isEqualTo(expected);
		assertThat(usernameDescriptor.getField().getType()).isEqualTo(String.class);
	}

	@Test
	void getMethodNameForNo() {
		assertThat(noDescriptor.getMethodName()).isEqualTo("getNo");
	}

	@Test
	void getMethodNameForUsername() {
		assertThat(usernameDescriptor.getMethodName()).isEqualTo("getUsername");
	}

	@Test
	void getMethodReturnsCorrectMethod() throws NoSuchMethodException {
		Method expected = Maker.class.getMethod("getNo");
		assertThat(noDescriptor.getMethod()).isEqualTo(expected);
	}

	@Test
	void getMethodForUsernameReturnsCorrectMethod() throws NoSuchMethodException {
		Method expected = Maker.class.getMethod("getUsername");
		assertThat(usernameDescriptor.getMethod()).isEqualTo(expected);
	}

	@Data
	static class Maker {

		private Integer no;

		private String username;

	}

}
