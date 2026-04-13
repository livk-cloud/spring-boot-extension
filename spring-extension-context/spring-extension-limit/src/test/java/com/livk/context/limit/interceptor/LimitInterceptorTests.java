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

package com.livk.context.limit.interceptor;

import com.livk.context.limit.LimitExecutor;
import com.livk.context.limit.annotation.Limit;
import com.livk.context.limit.exception.LimitException;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectProvider;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * @author livk
 */
class LimitInterceptorTests {

	@SuppressWarnings("unchecked")
	ObjectProvider<LimitExecutor> providers = mock(ObjectProvider.class);

	LimitInterceptor interceptor = new LimitInterceptor(providers);

	@Test
	void invokeAllowsAccessWhenExecutorReturnsTrue() throws Throwable {
		LimitExecutor executor = mock(LimitExecutor.class);
		given(executor.tryAccess(anyString(), anyInt(), any())).willReturn(true);
		given(providers.orderedStream()).willReturn(Stream.of(executor));

		Limit limit = createLimit("testKey", 10, 60);
		MethodInvocation invocation = mockInvocation();
		given(invocation.proceed()).willReturn("result");

		Object result = interceptor.invoke(invocation, limit);

		assertThat(result).isEqualTo("result");
		verify(invocation).proceed();
	}

	@Test
	void invokeThrowsLimitExceptionWhenExecutorReturnsFalse() throws Throwable {
		LimitExecutor executor = mock(LimitExecutor.class);
		given(executor.tryAccess(anyString(), anyInt(), any())).willReturn(false);
		given(providers.orderedStream()).willReturn(Stream.of(executor));

		Limit limit = createLimit("testKey", 10, 60);
		MethodInvocation invocation = mockInvocation();

		assertThatThrownBy(() -> interceptor.invoke(invocation, limit)).isInstanceOf(LimitException.class);
	}

	private Limit createLimit(String key, int rate, int rateInterval) {
		Limit limit = mock(Limit.class);
		given(limit.key()).willReturn(key);
		given(limit.rate()).willReturn(rate);
		given(limit.rateInterval()).willReturn(rateInterval);
		given(limit.rateIntervalUnit()).willReturn(TimeUnit.SECONDS);
		given(limit.restrictIp()).willReturn(false);
		given(limit.handler()).willAnswer(inv -> com.livk.context.limit.exception.LimitExceededHandler.class);
		return limit;
	}

	private MethodInvocation mockInvocation() throws NoSuchMethodException {
		MethodInvocation invocation = mock(MethodInvocation.class);
		Method method = LimitInterceptorTests.class.getDeclaredMethod("dummyMethod");
		given(invocation.getMethod()).willReturn(method);
		given(invocation.getArguments()).willReturn(new Object[0]);
		return invocation;
	}

	@SuppressWarnings("unused")
	void dummyMethod() {
	}

}
