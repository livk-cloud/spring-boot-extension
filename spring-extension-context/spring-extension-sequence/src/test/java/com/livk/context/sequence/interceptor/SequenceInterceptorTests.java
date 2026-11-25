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

package com.livk.context.sequence.interceptor;

import com.livk.context.sequence.Sequence;
import com.livk.context.sequence.SequenceProvider;
import com.livk.context.sequence.annotation.Seq;
import com.livk.context.sequence.exception.SequenceException;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.ObjectProvider;

import java.lang.reflect.Method;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

/**
 * @author livk
 */
class SequenceInterceptorTests {

	@SuppressWarnings("unchecked")
	ObjectProvider<SequenceProvider> providers = mock(ObjectProvider.class);

	SequenceInterceptor interceptor = new SequenceInterceptor(providers);

	@Test
	void fillsAbsentReturnValue() throws Throwable {
		stubSequence("order", 11L);
		MethodInvocation invocation = invocation("nextId", new Object[0]);
		given(invocation.proceed()).willReturn(0L);

		assertThat(interceptor.doInvoke(invocation, seq("order", ""))).isEqualTo(11L);
	}

	@Test
	void keepsExistingReturnValue() throws Throwable {
		Sequence sequence = stubSequence("order", 11L);
		MethodInvocation invocation = invocation("nextId", new Object[0]);
		given(invocation.proceed()).willReturn(8L);

		assertThat(interceptor.doInvoke(invocation, seq("order", ""))).isEqualTo(8L);
		verify(sequence, never()).nextValue();
	}

	@Test
	void fillsAbsentParameter() throws Throwable {
		stubSequence("order", 21L);
		Object[] arguments = new Object[] { null };
		MethodInvocation invocation = invocation("create", arguments);
		given(invocation.proceed()).willReturn("saved");

		assertThat(interceptor.doInvoke(invocation, seq("order", "id"))).isEqualTo("saved");
		assertThat(arguments[0]).isEqualTo(21L);
	}

	@Test
	void keepsExistingParameter() throws Throwable {
		Sequence sequence = stubSequence("order", 21L);
		Object[] arguments = new Object[] { 9L };
		MethodInvocation invocation = invocation("create", arguments);
		given(invocation.proceed()).willReturn("saved");

		interceptor.doInvoke(invocation, seq("order", "id"));

		assertThat(arguments[0]).isEqualTo(9L);
		verify(sequence, never()).nextValue();
	}

	@Test
	void rejectsReturnTypeThatCannotHoldAnId() throws Throwable {
		stubSequence("order", 1L);
		MethodInvocation invocation = invocation("name", new Object[0]);

		assertThatThrownBy(() -> interceptor.doInvoke(invocation, seq("order", "")))
			.isInstanceOf(SequenceException.class);
		verify(invocation, never()).proceed();
	}

	@Test
	void rejectsUnknownParameter() throws Throwable {
		stubSequence("order", 1L);
		MethodInvocation invocation = invocation("create", new Object[] { null });

		assertThatThrownBy(() -> interceptor.doInvoke(invocation, seq("order", "missing")))
			.isInstanceOf(SequenceException.class);
	}

	@Test
	void fillsAbsentParameterByIndex() throws Throwable {
		stubSequence("order", 31L);
		Object[] arguments = new Object[] { null };
		MethodInvocation invocation = invocation("create", arguments);
		given(invocation.proceed()).willReturn("saved");

		assertThat(interceptor.doInvoke(invocation, seqIndex("order", 0))).isEqualTo("saved");
		assertThat(arguments[0]).isEqualTo(31L);
	}

	@Test
	void rejectsOutOfRangeParameterIndex() throws Throwable {
		stubSequence("order", 1L);
		MethodInvocation invocation = invocation("create", new Object[] { null });

		assertThatThrownBy(() -> interceptor.doInvoke(invocation, seqIndex("order", 3)))
			.isInstanceOf(SequenceException.class);
	}

	@Test
	void rejectsNonIdParameterIndex() throws Throwable {
		stubSequence("order", 1L);
		MethodInvocation invocation = invocation("nameOnly", new Object[] { "x" });

		assertThatThrownBy(() -> interceptor.doInvoke(invocation, seqIndex("order", 0)))
			.isInstanceOf(SequenceException.class);
	}

	@Test
	void keepsExistingParameterByIndex() throws Throwable {
		Sequence sequence = stubSequence("order", 31L);
		Object[] arguments = new Object[] { 8L };
		MethodInvocation invocation = invocation("create", arguments);
		given(invocation.proceed()).willReturn("saved");

		interceptor.doInvoke(invocation, seqIndex("order", 0));

		assertThat(arguments[0]).isEqualTo(8L);
		verify(sequence, never()).nextValue();
	}

	@Test
	void rejectsBlankBizName() throws Throwable {
		MethodInvocation invocation = invocation("nextId", new Object[0]);
		assertThatThrownBy(() -> interceptor.doInvoke(invocation, seq(" ", ""))).isInstanceOf(SequenceException.class);
	}

	@Test
	void throwsWhenNoProvider() throws Throwable {
		given(providers.orderedStream()).willReturn(Stream.empty());
		MethodInvocation invocation = invocation("nextId", new Object[0]);

		assertThatThrownBy(() -> interceptor.doInvoke(invocation, seq("order", "")))
			.isInstanceOf(NoSuchBeanDefinitionException.class);
	}

	private Sequence stubSequence(String bizName, long id) {
		Sequence sequence = mock(Sequence.class);
		given(sequence.nextValue()).willReturn(id);
		SequenceProvider provider = mock(SequenceProvider.class);
		given(provider.get(bizName)).willReturn(sequence);
		given(providers.orderedStream()).willReturn(Stream.of(provider));
		return sequence;
	}

	private Seq seq(String bizName, String param) {
		Seq seq = mock(Seq.class);
		given(seq.value()).willReturn(bizName);
		given(seq.param()).willReturn(param);
		given(seq.paramIndex()).willReturn(-1);
		return seq;
	}

	private Seq seqIndex(String bizName, int paramIndex) {
		Seq seq = mock(Seq.class);
		given(seq.value()).willReturn(bizName);
		given(seq.param()).willReturn("");
		given(seq.paramIndex()).willReturn(paramIndex);
		return seq;
	}

	private MethodInvocation invocation(String methodName, Object[] arguments) throws NoSuchMethodException {
		MethodInvocation invocation = mock(MethodInvocation.class);
		Method method = method(methodName);
		given(invocation.getMethod()).willReturn(method);
		given(invocation.getArguments()).willReturn(arguments);
		return invocation;
	}

	private Method method(String name) throws NoSuchMethodException {
		for (Method method : SequenceInterceptorTests.class.getDeclaredMethods()) {
			if (method.getName().equals(name)) {
				return method;
			}
		}
		throw new NoSuchMethodException(name);
	}

	@SuppressWarnings("unused")
	long nextId() {
		return 0;
	}

	@SuppressWarnings("unused")
	String create(Long id) {
		return "saved";
	}

	@SuppressWarnings("unused")
	String name() {
		return "order";
	}

	@SuppressWarnings("unused")
	String nameOnly(String value) {
		return value;
	}

}
