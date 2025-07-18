/*
 * Copyright 2021-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * you may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.livk.commons.aop;

import org.aopalliance.intercept.MethodInvocation;
import org.junit.jupiter.api.Test;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * <p>
 * AnnotationAbstractPointcutTypeAdvisorTest
 * </p>
 *
 * @author livk
 */
class AnnotationAbstractPointcutTypeAdvisorTest {

	@Test
	void test() {
		MyAnnotationAbstractPointcutTypeAdvisor advisor = new MyAnnotationAbstractPointcutTypeAdvisor();

		assertEquals(AnnotationMatchingPointcut.class, advisor.getPointcut().getClass());
		assertEquals(AnnotationMatchingPointcut.forClassAnnotation(MyAnnotation.class), advisor.getPointcut());

		assertTrue(advisor.getPointcut().getClassFilter().matches(AopProxyClass.class));
		assertFalse(advisor.getPointcut()
			.getClassFilter()
			.matches(AnnotationAbstractPointcutAdvisorTest.AopProxyClass.class));
	}

	@MyAnnotation
	static class AopProxyClass {

		@SuppressWarnings("unused")
		void testAop() {
		}

	}

	@Target({ ElementType.TYPE })
	@Retention(RetentionPolicy.RUNTIME)
	@interface MyAnnotation {

	}

	static class MyAnnotationAbstractPointcutTypeAdvisor extends AnnotationAbstractPointcutTypeAdvisor<MyAnnotation> {

		@Override
		protected Object invoke(MethodInvocation invocation, MyAnnotation annotation) throws Throwable {
			return invocation.proceed();
		}

		@Override
		protected AnnotationPointcut annotationPointcut() {
			return AnnotationPointcut.forType();
		}

	}

}
