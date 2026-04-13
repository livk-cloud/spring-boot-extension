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

package com.livk.context.dynamic.intercept;

import com.livk.context.dynamic.DataSourceContextHolder;
import com.livk.context.dynamic.annotation.DynamicSource;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * @author livk
 */
class DataSourceInterceptorTests {

	final DataSourceInterceptor interceptor = new DataSourceInterceptor();

	@AfterEach
	void tearDown() {
		DataSourceContextHolder.clear();
	}

	@Test
	void invokeSwitchesDataSourceAndClearsAfter() throws Throwable {
		DynamicSource annotation = mock(DynamicSource.class);
		given(annotation.value()).willReturn("slave1");

		MethodInvocation invocation = mock(MethodInvocation.class);
		given(invocation.proceed()).willAnswer(inv -> {
			// during invocation, datasource should be set
			assertThat(DataSourceContextHolder.getDataSource()).isEqualTo("slave1");
			return "result";
		});

		Object result = interceptor.invoke(invocation, annotation);

		assertThat(result).isEqualTo("result");
		assertThat(DataSourceContextHolder.getDataSource()).isNull();
		verify(invocation).proceed();
	}

	@Test
	void invokeWithNullAnnotationDoesNotSwitchDataSource() throws Throwable {
		MethodInvocation invocation = mock(MethodInvocation.class);
		given(invocation.proceed()).willReturn("result");

		Object result = interceptor.invoke(invocation, null);

		assertThat(result).isEqualTo("result");
		assertThat(DataSourceContextHolder.getDataSource()).isNull();
	}

	@Test
	void invokeClearsDataSourceEvenWhenAnnotationPresent() throws Throwable {
		DataSourceContextHolder.switchDataSource("existing");

		DynamicSource annotation = mock(DynamicSource.class);
		given(annotation.value()).willReturn("slave2");

		MethodInvocation invocation = mock(MethodInvocation.class);
		given(invocation.proceed()).willReturn(null);

		interceptor.invoke(invocation, annotation);

		assertThat(DataSourceContextHolder.getDataSource()).isNull();
	}

}
