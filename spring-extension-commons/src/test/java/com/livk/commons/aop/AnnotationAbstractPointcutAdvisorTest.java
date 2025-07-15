/*
 * Copyright 2021-2024 spring-boot-extension the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.livk.commons.aop;

import org.aopalliance.intercept.MethodInvocation;
import org.junit.jupiter.api.Test;
import org.springframework.aop.IntroductionInterceptor;
import org.springframework.aop.Pointcut;
import org.jspecify.annotations.NonNull;
import org.springframework.aop.support.ComposablePointcut;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * <p>
 * AnnotationAbstractPointcutAdvisorTest
 * </p>
 *
 * @author livk
 */
class AnnotationAbstractPointcutAdvisorTest {

	@Test
	void test() throws NoSuchMethodException {
		MyAnnotationAbstractPointcutAdvisor advisor = new MyAnnotationAbstractPointcutAdvisor();

		assertEquals(MyAnnotation.class, advisor.annotationType);
		assertTrue(advisor.implementsInterface(IntroductionInterceptor.class));
		assertEquals(ComposablePointcut.class, advisor.getPointcut().getClass());
		assertEquals(AnnotationPointcut.forTypeOrMethod().getPointcut(MyAnnotation.class), advisor.getPointcut());

		assertTrue(advisor.getPointcut().getClassFilter().matches(AopProxyClass.class));
		assertTrue(advisor.getPointcut()
			.getMethodMatcher()
			.matches(AopProxyClass.class.getDeclaredMethod("testAop"), AopProxyClass.class));
	}

	@MyAnnotation
	static class AopProxyClass {

		@MyAnnotation
		void testAop() {
		}

	}

	@Target({ ElementType.TYPE, ElementType.METHOD })
	@Retention(RetentionPolicy.RUNTIME)
	@interface MyAnnotation {

	}

	static class MyAnnotationAbstractPointcutAdvisor extends AnnotationAbstractPointcutAdvisor<MyAnnotation> {

		@Override
		protected Object invoke(MethodInvocation invocation, MyAnnotation annotation) throws Throwable {
			return invocation.proceed();
		}

		@NonNull
		@Override
		public Pointcut getPointcut() {
			return AnnotationPointcut.forTypeOrMethod().getPointcut(annotationType);
		}

	}

}
