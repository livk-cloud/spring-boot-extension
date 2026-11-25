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

package com.livk.commons.aop;

import org.aopalliance.intercept.MethodInvocation;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Test;
import org.springframework.aop.IntroductionInterceptor;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.ComposablePointcut;
import org.springframework.core.Ordered;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author livk
 */
class AbstractAnnotationAdvisorTests {

	final MyAbstractAnnotationAdvisor advisor = new MyAbstractAnnotationAdvisor();

	@Test
	void resolvesAnnotationTypeFromGenericArgument() {
		assertThat(advisor.annotationType).isEqualTo(MyAnnotation.class);
	}

	@Test
	void implementsIntroductionInterceptor() {
		assertThat(advisor.implementsInterface(IntroductionInterceptor.class)).isTrue();
	}

	@Test
	void pointcutIsComposablePointcut() {
		assertThat(advisor.getPointcut()).isInstanceOf(ComposablePointcut.class);
	}

	@Test
	void classFilterMatchesAnnotatedClass() {
		assertThat(advisor.getPointcut().getClassFilter().matches(AopProxyClass.class)).isTrue();
	}

	@Test
	void methodMatcherMatchesAnnotatedMethod() throws NoSuchMethodException {
		assertThat(advisor.getPointcut()
			.getMethodMatcher()
			.matches(AopProxyClass.class.getDeclaredMethod("testAop"), AopProxyClass.class)).isTrue();
	}

	@Test
	void methodMatcherDoesNotMatchUnannotatedMethod() throws NoSuchMethodException {
		assertThat(advisor.getPointcut()
			.getMethodMatcher()
			.matches(UnannotatedClass.class.getDeclaredMethod("noAnnotation"), UnannotatedClass.class)).isFalse();
	}

	@Test
	void invokeUsesMethodLevelAnnotation() throws Throwable {
		AtomicReference<MyAnnotation> seen = new AtomicReference<>();
		MyAbstractAnnotationAdvisor capturing = new MyAbstractAnnotationAdvisor() {
			@Override
			protected Object doInvoke(MethodInvocation invocation, MyAnnotation annotation) {
				seen.set(annotation);
				return "ok";
			}
		};
		MethodInvocation invocation = mock(MethodInvocation.class);
		when(invocation.getMethod()).thenReturn(AopProxyClass.class.getDeclaredMethod("testAop"));
		when(invocation.getThis()).thenReturn(new AopProxyClass());

		assertThat(capturing.invoke(invocation)).isEqualTo("ok");
		assertThat(seen.get()).isNotNull();
	}

	@Test
	void invokeFallsBackToClassLevelAnnotation() throws Throwable {
		AtomicReference<MyAnnotation> seen = new AtomicReference<>();
		MyAbstractAnnotationAdvisor capturing = new MyAbstractAnnotationAdvisor() {
			@Override
			protected Object doInvoke(MethodInvocation invocation, MyAnnotation annotation) {
				seen.set(annotation);
				return "ok";
			}
		};
		MethodInvocation invocation = mock(MethodInvocation.class);
		when(invocation.getMethod()).thenReturn(AopProxyClass.class.getDeclaredMethod("unannotatedMethod"));
		when(invocation.getThis()).thenReturn(new AopProxyClass());

		assertThat(capturing.invoke(invocation)).isEqualTo("ok");
		assertThat(seen.get()).isNotNull();
	}

	@Test
	void invokeResolvesAnnotationFromMostSpecificTargetMethod() throws Throwable {
		AtomicReference<MyAnnotation> seen = new AtomicReference<>();
		MyAbstractAnnotationAdvisor capturing = new MyAbstractAnnotationAdvisor() {
			@Override
			protected Object doInvoke(MethodInvocation invocation, MyAnnotation annotation) {
				seen.set(annotation);
				return "ok";
			}
		};
		MethodInvocation invocation = mock(MethodInvocation.class);
		when(invocation.getMethod()).thenReturn(SampleService.class.getDeclaredMethod("annotated"));
		when(invocation.getThis()).thenReturn(new SampleServiceImpl());

		assertThat(capturing.invoke(invocation)).isEqualTo("ok");
		assertThat(seen.get()).isNotNull();
	}

	@Test
	void invokeSkipsClassLookupWhenThisIsNull() throws Throwable {
		AtomicReference<MyAnnotation> seen = new AtomicReference<>();
		MyAbstractAnnotationAdvisor capturing = new MyAbstractAnnotationAdvisor() {
			@Override
			protected Object doInvoke(MethodInvocation invocation, MyAnnotation annotation) {
				seen.set(annotation);
				return "ok";
			}
		};
		MethodInvocation invocation = mock(MethodInvocation.class);
		when(invocation.getMethod()).thenReturn(UnannotatedClass.class.getDeclaredMethod("noAnnotation"));
		when(invocation.getThis()).thenReturn(null);

		assertThat(capturing.invoke(invocation)).isEqualTo("ok");
		assertThat(seen.get()).isNull();
	}

	@Test
	void getOrderDefaultsToLowestPrecedenceMinusOne() {
		assertThat(advisor.getOrder()).isEqualTo(Ordered.LOWEST_PRECEDENCE - 1);
	}

	@Test
	void getOrderKeepsExplicitOrder() {
		MyAbstractAnnotationAdvisor ordered = new MyAbstractAnnotationAdvisor();
		ordered.setOrder(42);
		assertThat(ordered.getOrder()).isEqualTo(42);
	}

	@Test
	void getAdviceReturnsSelf() {
		assertThat(advisor.getAdvice()).isSameAs(advisor);
	}

	@Test
	void implementsInterfaceReturnsFalseForUnrelatedType() {
		assertThat(advisor.implementsInterface(Runnable.class)).isFalse();
	}

	@MyAnnotation
	static class AopProxyClass {

		@MyAnnotation
		void testAop() {
		}

		void unannotatedMethod() {
		}

	}

	static class UnannotatedClass {

		void noAnnotation() {
		}

	}

	interface SampleService {

		void annotated();

	}

	static class SampleServiceImpl implements SampleService {

		@Override
		@MyAnnotation
		public void annotated() {
			// Annotation carrier for most-specific method resolution tests.
		}

	}

	@Target({ ElementType.TYPE, ElementType.METHOD })
	@Retention(RetentionPolicy.RUNTIME)
	@interface MyAnnotation {

	}

	static class MyAbstractAnnotationAdvisor extends AbstractAnnotationAdvisor<MyAnnotation> {

		@Override
		protected Object doInvoke(MethodInvocation invocation, MyAnnotation annotation) throws Throwable {
			return invocation.proceed();
		}

		@NonNull
		@Override
		public Pointcut getPointcut() {
			return AnnotationPointcutFactory.forTypeOrMethod().create(annotationType);
		}

	}

}
