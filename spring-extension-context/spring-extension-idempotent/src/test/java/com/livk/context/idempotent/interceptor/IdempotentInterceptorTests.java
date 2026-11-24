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

package com.livk.context.idempotent.interceptor;

import com.livk.context.idempotent.IdempotentExecutor;
import com.livk.context.idempotent.annotation.Idempotent;
import com.livk.context.idempotent.exception.IdempotentException;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.ObjectProvider;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @author livk
 */
class IdempotentInterceptorTests {

	@SuppressWarnings("unchecked")
	ObjectProvider<IdempotentExecutor> executors = mock(ObjectProvider.class);

	IdempotentInterceptor interceptor = new IdempotentInterceptor(executors);

	@Test
	void doInvokeProceedsWhenTokenIsAcquired() throws Throwable {
		IdempotentExecutor executor = mock(IdempotentExecutor.class);
		given(executor.tryAcquire(eq("order:1"), eq(Duration.ofSeconds(30)))).willReturn(true);
		given(executors.orderedStream()).willReturn(Stream.of(executor));

		MethodInvocation invocation = mockInvocation("submit", new Object[] { "1" });
		given(invocation.proceed()).willReturn("ok");

		Object result = interceptor.doInvoke(invocation, idempotent("'order:' + #orderId", 30, true));

		assertThat(result).isEqualTo("ok");
		verify(invocation).proceed();
		verify(executor, never()).release(any());
	}

	@Test
	void doInvokeThrowsWhenTokenIsOccupied() throws Throwable {
		IdempotentExecutor executor = mock(IdempotentExecutor.class);
		given(executor.tryAcquire(eq("order:1"), any())).willReturn(false);
		given(executors.orderedStream()).willReturn(Stream.of(executor));

		MethodInvocation invocation = mockInvocation("submit", new Object[] { "1" });

		assertThatThrownBy(() -> interceptor.doInvoke(invocation, idempotent("'order:' + #orderId", 30, true)))
			.isInstanceOf(IdempotentException.class)
			.hasMessage("Duplicate request: order:1");
		verify(invocation, never()).proceed();
	}

	@Test
	void doInvokeReleasesTokenWhenMethodThrows() throws Throwable {
		IdempotentExecutor executor = mock(IdempotentExecutor.class);
		given(executor.tryAcquire(eq("order:1"), any())).willReturn(true);
		given(executors.orderedStream()).willReturn(Stream.of(executor));

		MethodInvocation invocation = mockInvocation("submit", new Object[] { "1" });
		given(invocation.proceed()).willThrow(new IllegalStateException("boom"));

		assertThatThrownBy(() -> interceptor.doInvoke(invocation, idempotent("'order:' + #orderId", 30, true)))
			.isInstanceOf(IllegalStateException.class)
			.hasMessage("boom");
		verify(executor).release("order:1");
	}

	@Test
	void doInvokeKeepsTokenWhenReleaseOnErrorIsDisabled() throws Throwable {
		IdempotentExecutor executor = mock(IdempotentExecutor.class);
		given(executor.tryAcquire(eq("order:1"), any())).willReturn(true);
		given(executors.orderedStream()).willReturn(Stream.of(executor));

		MethodInvocation invocation = mockInvocation("submit", new Object[] { "1" });
		given(invocation.proceed()).willThrow(new IllegalStateException("boom"));

		assertThatThrownBy(() -> interceptor.doInvoke(invocation, idempotent("'order:' + #orderId", 30, false)))
			.isInstanceOf(IllegalStateException.class);
		verify(executor, never()).release(any());
	}

	@Test
	void doInvokeThrowsWhenKeyIsBlank() throws Throwable {
		IdempotentExecutor executor = mock(IdempotentExecutor.class);
		given(executors.orderedStream()).willReturn(Stream.of(executor));
		MethodInvocation invocation = mockInvocation("submit", new Object[] { "1" });

		assertThatThrownBy(() -> interceptor.doInvoke(invocation, idempotent(" ", 30, true)))
			.isInstanceOf(IdempotentException.class);
		verify(executor, never()).tryAcquire(any(), any());
	}

	@Test
	void doInvokeThrowsWhenNoExecutor() throws Throwable {
		given(executors.orderedStream()).willReturn(Stream.empty());
		MethodInvocation invocation = mockInvocation("submit", new Object[] { "1" });

		assertThatThrownBy(() -> interceptor.doInvoke(invocation, idempotent("'order:' + #orderId", 30, true)))
			.isInstanceOf(NoSuchBeanDefinitionException.class);
	}

	@Test
	void doInvokeReusesCachedExecutor() throws Throwable {
		IdempotentExecutor executor = mock(IdempotentExecutor.class);
		given(executor.tryAcquire(any(), any())).willReturn(true);
		given(executors.orderedStream()).willReturn(Stream.of(executor));

		MethodInvocation invocation = mockInvocation("submit", new Object[] { "1" });
		given(invocation.proceed()).willReturn("ok");
		Idempotent idempotent = idempotent("'order:' + #orderId", 30, true);

		interceptor.doInvoke(invocation, idempotent);
		interceptor.doInvoke(invocation, idempotent);

		verify(executors, times(1)).orderedStream();
	}

	private Idempotent idempotent(String key, long ttl, boolean releaseOnError) {
		Idempotent idempotent = mock(Idempotent.class);
		given(idempotent.key()).willReturn(key);
		given(idempotent.ttl()).willReturn(ttl);
		given(idempotent.ttlUnit()).willReturn(TimeUnit.SECONDS);
		given(idempotent.releaseOnError()).willReturn(releaseOnError);
		return idempotent;
	}

	private MethodInvocation mockInvocation(String methodName, Object[] arguments) throws NoSuchMethodException {
		MethodInvocation invocation = mock(MethodInvocation.class);
		Method method = IdempotentInterceptorTests.class.getDeclaredMethod(methodName, String.class);
		given(invocation.getMethod()).willReturn(method);
		given(invocation.getArguments()).willReturn(arguments);
		return invocation;
	}

	@SuppressWarnings("unused")
	void submit(String orderId) {
	}

}
