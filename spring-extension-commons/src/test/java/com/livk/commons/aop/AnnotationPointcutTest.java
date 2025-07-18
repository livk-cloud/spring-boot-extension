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

import org.junit.jupiter.api.Test;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * <p>
 * AnnotationAutoPointcutTest
 * </p>
 *
 * @author livk
 */
class AnnotationPointcutTest {

	@Test
	void test() throws NoSuchMethodException {
		Class<? extends Annotation> annotationType = MyAnnotation.class;
		Class<?> type = AopProxyClass.class;
		Method method = AopProxyClass.class.getDeclaredMethod("testAop");
		{
			AnnotationPointcut pointcut = AnnotationPointcut.forTarget();
			assertEquals(AnnotationPointcut.forTypeOrMethod().getPointcut(annotationType),
					pointcut.getPointcut(annotationType));
			assertTrue(pointcut.getPointcut(annotationType).getClassFilter().matches(type));
			assertTrue(pointcut.getPointcut(annotationType).getMethodMatcher().matches(method, type));
		}

		{
			AnnotationPointcut pointcut = AnnotationPointcut.forType();
			assertEquals(AnnotationMatchingPointcut.forClassAnnotation(annotationType),
					pointcut.getPointcut(annotationType));
			assertTrue(pointcut.getPointcut(annotationType).getClassFilter().matches(type));
			assertTrue(pointcut.getPointcut(annotationType).getMethodMatcher().matches(method, type));
		}

		{
			AnnotationPointcut pointcut = AnnotationPointcut.forMethod();
			assertEquals(AnnotationMatchingPointcut.forMethodAnnotation(annotationType),
					pointcut.getPointcut(annotationType));
			assertTrue(pointcut.getPointcut(annotationType).getClassFilter().matches(type));
			assertTrue(pointcut.getPointcut(annotationType).getMethodMatcher().matches(method, type));
		}

		{
			AnnotationPointcut pointcut = AnnotationPointcut.forTypeOrMethod();
			assertEquals(AnnotationPointcut.forTypeOrMethod().getPointcut(annotationType),
					pointcut.getPointcut(annotationType));
			assertTrue(pointcut.getPointcut(annotationType).getClassFilter().matches(type));
			assertTrue(pointcut.getPointcut(annotationType).getMethodMatcher().matches(method, type));
		}

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

}
