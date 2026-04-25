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

package com.livk.context.limit.executor;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
class WebRequestReentrantLimitExecutorTests {

	MockHttpServletRequest request;

	TestReentrantLimitExecutor executor;

	@BeforeEach
	void setUp() {
		request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
		executor = new TestReentrantLimitExecutor();
	}

	@AfterEach
	void tearDown() {
		RequestContextHolder.resetRequestAttributes();
	}

	@Test
	void tryAccessDelegatesToReentrantTryAccess() {
		executor.allowAccess = true;
		assertThat(executor.tryAccess("key", 10, Duration.ofSeconds(60))).isTrue();
		assertThat(executor.callCount).isEqualTo(1);
	}

	@Test
	void tryAccessReturnsFalseWhenDenied() {
		executor.allowAccess = false;
		assertThat(executor.tryAccess("key", 10, Duration.ofSeconds(60))).isFalse();
		assertThat(executor.callCount).isEqualTo(1);
	}

	@Test
	void tryAccessReusesResultForSameRequestAndKey() {
		executor.allowAccess = true;
		assertThat(executor.tryAccess("key", 10, Duration.ofSeconds(60))).isTrue();
		assertThat(executor.tryAccess("key", 10, Duration.ofSeconds(60))).isTrue();
		// reentrantTryAccess should only be called once due to request attribute caching
		assertThat(executor.callCount).isEqualTo(1);
	}

	@Test
	void tryAccessCallsReentrantForDifferentKeys() {
		executor.allowAccess = true;
		executor.tryAccess("key1", 10, Duration.ofSeconds(60));
		executor.tryAccess("key2", 10, Duration.ofSeconds(60));
		assertThat(executor.callCount).isEqualTo(2);
	}

	@Test
	void tryAccessSetsRequestAttribute() {
		executor.allowAccess = true;
		executor.tryAccess("myKey", 10, Duration.ofSeconds(60));
		assertThat(request.getAttribute("limit:myKey")).isEqualTo(true);
	}

	static class TestReentrantLimitExecutor extends WebRequestReentrantLimitExecutor {

		boolean allowAccess = true;

		int callCount = 0;

		@Override
		protected boolean reentrantTryAccess(String compositeKey, int rate, Duration rateInterval) {
			callCount++;
			return allowAccess;
		}

	}

}
