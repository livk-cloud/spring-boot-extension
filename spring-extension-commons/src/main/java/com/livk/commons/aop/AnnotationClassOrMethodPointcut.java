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

import com.livk.commons.util.ObjectUtils;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.aop.support.annotation.AnnotationClassFilter;
import org.springframework.aop.support.annotation.AnnotationMethodMatcher;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.lang.NonNull;
import org.springframework.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * <p>
 * 类注解或者方法注解的切点
 * </p>
 * <p>
 * 参考
 * {@see org.springframework.retry.annotation.RetryConfiguration.AnnotationClassOrMethodPointcut}
 * </p>
 *
 * @author livk
 */
final class AnnotationClassOrMethodPointcut extends StaticMethodMatcherPointcut {

	private final MethodMatcher methodResolver;

	/**
	 * 构造方法
	 * <p>
	 * 用于类或者方法切点
	 * @param annotationType 注解类型
	 */
	public AnnotationClassOrMethodPointcut(Class<? extends Annotation> annotationType) {
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

	private static final class AnnotationClassOrMethodFilter extends AnnotationClassFilter {

		private final AnnotationMethodsResolver methodResolver;

		/**
		 * 构造方法
		 * <p>
		 * 用于匹配的ClassFilter
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
		 * 构造方法
		 * @param annotationType 注解类型
		 */
		public AnnotationMethodsResolver(Class<? extends Annotation> annotationType) {
			this.annotationType = annotationType;
		}

		/**
		 * 判断类是否包含注解
		 * @param clazz 类信息
		 * @return boolean
		 */
		public boolean hasAnnotatedMethods(Class<?> clazz) {
			final AtomicBoolean found = new AtomicBoolean(false);
			ReflectionUtils.doWithMethods(clazz, method -> {
				if (found.get()) {
					return;
				}
				Annotation annotation = AnnotationUtils.findAnnotation(method, annotationType);
				if (annotation != null) {
					found.set(true);
				}
			});
			return found.get();
		}

	}

}
