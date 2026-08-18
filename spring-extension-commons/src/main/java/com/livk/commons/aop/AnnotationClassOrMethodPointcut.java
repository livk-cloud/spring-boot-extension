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

import org.springframework.util.ObjectUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.aop.support.annotation.AnnotationClassFilter;
import org.springframework.aop.support.annotation.AnnotationMethodMatcher;
import org.springframework.core.annotation.AnnotationUtils;
import org.jspecify.annotations.NonNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Pointcut matching class-level or method-level annotations.
 * <p>
 * 参考
 * {@see org.springframework.retry.annotation.RetryConfiguration.AnnotationClassOrMethodPointcut}
 * </p>
 *
 * @author livk
 * @deprecated use
 * {@link org.springframework.aop.support.ComposablePointcut#union(Pointcut)}
 */
@Deprecated(since = "1.5.2")
final class AnnotationClassOrMethodPointcut extends StaticMethodMatcherPointcut {

	private final MethodMatcher methodResolver;

	/**
	 * Constructor for class or method pointcut.
	 * @param annotationType 注解类型
	 */
	AnnotationClassOrMethodPointcut(Class<? extends Annotation> annotationType) {
		this.methodResolver = new AnnotationMethodMatcher(annotationType);
		setClassFilter(new AnnotationClassOrMethodFilter(annotationType));
	}

	@Override
	public boolean matches(@NonNull Method method, @NonNull Class<?> targetClass) {
		return getClassFilter().matches(targetClass) || this.methodResolver.matches(method, targetClass);
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof final AnnotationClassOrMethodPointcut otherAdvisor)) {
			return false;
		}
		return ObjectUtils.nullSafeEquals(this.methodResolver, otherAdvisor.methodResolver);
	}

	@Override
	public int hashCode() {
		return this.methodResolver.hashCode();
	}

	private static final class AnnotationClassOrMethodFilter extends AnnotationClassFilter {

		private final AnnotationMethodsResolver methodResolver;

		/**
		 * Constructor for annotation class or method filter.
		 * @param annotationType 注解类型
		 */
		AnnotationClassOrMethodFilter(Class<? extends Annotation> annotationType) {
			super(annotationType, true);
			this.methodResolver = new AnnotationMethodsResolver(annotationType);
		}

		@Override
		public boolean matches(@NonNull Class<?> clazz) {
			return super.matches(clazz) || this.methodResolver.hasAnnotatedMethods(clazz);
		}

	}

	private static class AnnotationMethodsResolver {

		private final Class<? extends Annotation> annotationType;

		/**
		 * Constructor for annotation methods resolver.
		 * @param annotationType 注解类型
		 */
		AnnotationMethodsResolver(Class<? extends Annotation> annotationType) {
			this.annotationType = annotationType;
		}

		/**
		 * Checks whether the class has annotated methods.
		 * @param clazz 类信息
		 * @return boolean
		 */
		public boolean hasAnnotatedMethods(Class<?> clazz) {
			final AtomicBoolean found = new AtomicBoolean(false);
			ReflectionUtils.doWithMethods(clazz, (method) -> {
				if (found.get()) {
					return;
				}
				Annotation annotation = AnnotationUtils.findAnnotation(method, this.annotationType);
				if (annotation != null) {
					found.set(true);
				}
			});
			return found.get();
		}

	}

}
