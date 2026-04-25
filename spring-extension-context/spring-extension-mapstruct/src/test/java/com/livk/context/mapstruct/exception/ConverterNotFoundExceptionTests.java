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

package com.livk.context.mapstruct.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
class ConverterNotFoundExceptionTests {

	@Test
	void constructDefault() {
		ConverterNotFoundException ex = new ConverterNotFoundException();
		assertThat(ex.getMessage()).isNull();
	}

	@Test
	void constructWithMessage() {
		ConverterNotFoundException ex = new ConverterNotFoundException("not found");
		assertThat(ex.getMessage()).isEqualTo("not found");
	}

	@Test
	void constructWithMessageAndCause() {
		RuntimeException cause = new RuntimeException("root");
		ConverterNotFoundException ex = new ConverterNotFoundException("not found", cause);
		assertThat(ex.getMessage()).isEqualTo("not found");
		assertThat(ex.getCause()).isSameAs(cause);
	}

	@Test
	void constructWithCause() {
		RuntimeException cause = new RuntimeException("root");
		ConverterNotFoundException ex = new ConverterNotFoundException(cause);
		assertThat(ex.getCause()).isSameAs(cause);
	}

	@Test
	void isRuntimeException() {
		assertThat(new ConverterNotFoundException()).isInstanceOf(RuntimeException.class);
	}

}
