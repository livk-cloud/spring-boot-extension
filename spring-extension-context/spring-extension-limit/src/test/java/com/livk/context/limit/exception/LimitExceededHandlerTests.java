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

package com.livk.context.limit.exception;

import com.livk.context.limit.annotation.Limit;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

/**
 * @author livk
 */
class LimitExceededHandlerTests {

	@Test
	void defaultHandlerBuildsLimitException() {
		Limit limit = mock(Limit.class);
		given(limit.key()).willReturn("testKey");
		given(limit.rate()).willReturn(10);
		given(limit.rateInterval()).willReturn(60);
		given(limit.rateIntervalUnit()).willReturn(TimeUnit.SECONDS);

		RuntimeException ex = LimitExceededHandler.DEFAULT.buildException(limit);

		assertThat(ex).isInstanceOf(LimitException.class);
		assertThat(ex.getMessage()).contains("testKey").contains("10").contains("60").contains("SECONDS");
	}

	@Test
	void customHandlerCanBeImplemented() {
		LimitExceededHandler custom = limit -> new IllegalStateException("custom: " + limit.key());

		Limit limit = mock(Limit.class);
		given(limit.key()).willReturn("myKey");

		RuntimeException ex = custom.buildException(limit);
		assertThat(ex).isInstanceOf(IllegalStateException.class);
		assertThat(ex.getMessage()).isEqualTo("custom: myKey");
	}

}
