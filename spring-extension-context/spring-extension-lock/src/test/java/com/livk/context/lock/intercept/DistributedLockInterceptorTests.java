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

package com.livk.context.lock.intercept;

import com.livk.context.lock.DistributedLock;
import com.livk.context.lock.LockType;
import com.livk.context.lock.annotation.DistLock;
import com.livk.context.lock.exception.LockException;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectProvider;

import java.lang.reflect.Method;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * @author livk
 */
class DistributedLockInterceptorTests {

	@SuppressWarnings("unchecked")
	ObjectProvider<DistributedLock> provider = mock(ObjectProvider.class);

	DistributedLockInterceptor interceptor = new DistributedLockInterceptor(provider);

	@Test
	void invokeAllowsAccessWhenLockAcquired() throws Throwable {
		DistributedLock distributedLock = mock(DistributedLock.class);
		given(distributedLock.tryLock(any(), anyString(), anyLong(), anyLong(), anyBoolean())).willReturn(true);
		given(provider.orderedStream()).willReturn(Stream.of(distributedLock));

		DistLock distLock = createDistLock();
		MethodInvocation invocation = mockInvocation();
		given(invocation.proceed()).willReturn("result");

		Object result = interceptor.invoke(invocation, distLock);

		assertThat(result).isEqualTo("result");
		verify(invocation).proceed();
		verify(distributedLock).unlock();
	}

	@Test
	void invokeThrowsLockExceptionWhenLockNotAcquired() throws Throwable {
		DistributedLock distributedLock = mock(DistributedLock.class);
		given(distributedLock.tryLock(any(), anyString(), anyLong(), anyLong(), anyBoolean())).willReturn(false);
		given(provider.orderedStream()).willReturn(Stream.of(distributedLock));

		DistLock distLock = createDistLock();
		MethodInvocation invocation = mockInvocation();

		assertThatThrownBy(() -> interceptor.invoke(invocation, distLock)).isInstanceOf(LockException.class)
			.hasMessage("Failed to acquire locks!");
	}

	@Test
	void invokeUnlocksEvenWhenProceedThrows() throws Throwable {
		DistributedLock distributedLock = mock(DistributedLock.class);
		given(distributedLock.tryLock(any(), anyString(), anyLong(), anyLong(), anyBoolean())).willReturn(true);
		given(provider.orderedStream()).willReturn(Stream.of(distributedLock));

		DistLock distLock = createDistLock();
		MethodInvocation invocation = mockInvocation();
		given(invocation.proceed()).willThrow(new RuntimeException("business error"));

		assertThatThrownBy(() -> interceptor.invoke(invocation, distLock)).isInstanceOf(RuntimeException.class)
			.hasMessage("business error");
		verify(distributedLock).unlock();
	}

	private DistLock createDistLock() {
		DistLock distLock = mock(DistLock.class);
		given(distLock.key()).willReturn("testKey");
		given(distLock.type()).willReturn(LockType.LOCK);
		given(distLock.leaseTime()).willReturn(30L);
		given(distLock.waitTime()).willReturn(10L);
		given(distLock.async()).willReturn(false);
		return distLock;
	}

	private MethodInvocation mockInvocation() throws NoSuchMethodException {
		MethodInvocation invocation = mock(MethodInvocation.class);
		Method method = DistributedLockInterceptorTests.class.getDeclaredMethod("dummyMethod");
		given(invocation.getMethod()).willReturn(method);
		given(invocation.getArguments()).willReturn(new Object[0]);
		return invocation;
	}

	@SuppressWarnings("unused")
	void dummyMethod() {
	}

}
