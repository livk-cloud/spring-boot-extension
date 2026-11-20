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

import org.junit.jupiter.api.Test;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

/**
 * @author livk
 */
class AnnotationTargetTests {

	@Test
	void ofReturnsCachedInstance() {
		assertThat(AnnotationTarget.of(TypeAndMethod.class)).isSameAs(AnnotationTarget.of(TypeAndMethod.class));
	}

	@Test
	void getAnnotationRespectsSupportedElementTypes() throws Exception {
		AnnotationTarget<TypeOnly> typeOnly = AnnotationTarget.of(TypeOnly.class);
		assertThat(typeOnly.getAnnotation(TypeOnlyClass.class)).isNotNull();
		assertThat(typeOnly.getAnnotation(TypeOnlyClass.class.getDeclaredMethod("method"))).isNull();

		AnnotationTarget<MethodOnly> methodOnly = AnnotationTarget.of(MethodOnly.class);
		assertThat(methodOnly.getAnnotation(MethodOnlyClass.class.getDeclaredMethod("method"))).isNotNull();
		assertThat(methodOnly.getAnnotation(MethodOnlyClass.class)).isNull();
	}

	@Test
	void targetPointcutCreatesTypeOrMethodPointcut() {
		Pointcut pointcut = AnnotationTarget.TARGET_POINTCUT.create(TypeAndMethod.class);
		ClassFilter classFilter = pointcut.getClassFilter();
		MethodMatcher methodMatcher = pointcut.getMethodMatcher();

		assertThat(classFilter.matches(TypeAndMethodClass.class)).isTrue();
		assertThat(methodMatcher.matches(TypeAndMethodClass.class.getDeclaredMethods()[0], TypeAndMethodClass.class))
			.isTrue();
	}

	@Test
	void targetPointcutCreatesTypeOnlyPointcut() {
		Pointcut pointcut = AnnotationTarget.TARGET_POINTCUT.create(TypeOnly.class);
		assertThat(pointcut.getClassFilter().matches(TypeOnlyClass.class)).isTrue();
	}

	@Test
	void targetPointcutCreatesMethodOnlyPointcut() throws Exception {
		Pointcut pointcut = AnnotationTarget.TARGET_POINTCUT.create(MethodOnly.class);
		assertThat(pointcut.getMethodMatcher()
			.matches(MethodOnlyClass.class.getDeclaredMethod("method"), MethodOnlyClass.class)).isTrue();
	}

	@Test
	void targetPointcutRejectsUnsupportedElementTypes() {
		assertThatIllegalArgumentException().isThrownBy(() -> AnnotationTarget.TARGET_POINTCUT.create(FieldOnly.class))
			.withMessageContaining("must support ElementType.TYPE and/or ElementType.METHOD");
	}

	@Test
	void supportsAllElementTypesWhenAnnotationHasNoTargetMeta() {
		AnnotationTarget<NoTargetMeta> target = AnnotationTarget.of(NoTargetMeta.class);
		assertThat(target.supports(ElementType.TYPE)).isTrue();
		assertThat(target.supports(ElementType.METHOD)).isTrue();
		assertThat(target.supports(ElementType.FIELD)).isTrue();
	}

	@Target({ ElementType.TYPE, ElementType.METHOD })
	@Retention(RetentionPolicy.RUNTIME)
	@interface TypeAndMethod {

	}

	@Target(ElementType.TYPE)
	@Retention(RetentionPolicy.RUNTIME)
	@interface TypeOnly {

	}

	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	@interface MethodOnly {

	}

	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.RUNTIME)
	@interface FieldOnly {

	}

	@Retention(RetentionPolicy.RUNTIME)
	@interface NoTargetMeta {

	}

	@TypeAndMethod
	static class TypeAndMethodClass {

		@TypeAndMethod
		void method() {
		}

	}

	@TypeOnly
	static class TypeOnlyClass {

		void method() {
		}

	}

	static class MethodOnlyClass {

		@MethodOnly
		void method() {
		}

	}

}
