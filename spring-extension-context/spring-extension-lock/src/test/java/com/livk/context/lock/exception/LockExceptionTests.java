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

package com.livk.context.lock.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
class LockExceptionTests {

	@Test
	void constructDefault() {
		LockException ex = new LockException();
		assertThat(ex.getMessage()).isNull();
		assertThat(ex.getCause()).isNull();
	}

	@Test
	void constructWithMessage() {
		LockException ex = new LockException("lock failed");
		assertThat(ex.getMessage()).isEqualTo("lock failed");
	}

	@Test
	void constructWithMessageAndCause() {
		RuntimeException cause = new RuntimeException("root");
		LockException ex = new LockException("lock failed", cause);
		assertThat(ex.getMessage()).isEqualTo("lock failed");
		assertThat(ex.getCause()).isSameAs(cause);
	}

	@Test
	void constructWithCause() {
		RuntimeException cause = new RuntimeException("root");
		LockException ex = new LockException(cause);
		assertThat(ex.getCause()).isSameAs(cause);
	}

	@Test
	void isRuntimeException() {
		assertThat(new LockException()).isInstanceOf(RuntimeException.class);
	}

}
