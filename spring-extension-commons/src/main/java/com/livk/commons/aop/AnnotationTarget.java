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

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.aop.Pointcut;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author livk
 */
final class AnnotationTarget<A extends Annotation> {

	public static final AnnotationPointcutFactory TARGET_POINTCUT = new AnnotationTargetPointcut();

	private static final ConcurrentMap<Class<? extends Annotation>, AnnotationTarget<?>> CACHE = new ConcurrentHashMap<>();

	@SuppressWarnings("unchecked")
	public static <A extends Annotation> AnnotationTarget<A> of(Class<A> annotationType) {
		return (AnnotationTarget<A>) CACHE.computeIfAbsent(annotationType, AnnotationTarget::new);
	}

	private final Class<A> annotationType;

	private final EnumSet<ElementType> elementTypes;

	private AnnotationTarget(Class<A> annotationType) {
		this.annotationType = annotationType;
		Target target = annotationType.getAnnotation(Target.class);
		this.elementTypes = (target == null) ? EnumSet.allOf(ElementType.class)
				: EnumSet.copyOf(Arrays.asList(target.value()));
	}

	public A getAnnotation(Method method) {
		return supports(ElementType.METHOD) ? AnnotationUtils.getAnnotation(method, annotationType) : null;
	}

	public A getAnnotation(Class<?> clazz) {
		return supports(ElementType.TYPE) ? AnnotationUtils.getAnnotation(clazz, annotationType) : null;
	}

	/**
	 * 是否支持指定的 ElementType。
	 */
	boolean supports(ElementType elementType) {
		return elementTypes.contains(elementType);
	}

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	static final class AnnotationTargetPointcut implements AnnotationPointcutFactory {

		@Override
		public Pointcut create(Class<? extends Annotation> annotationType) {
			AnnotationTarget<?> target = AnnotationTarget.of(annotationType);

			if (target.supports(ElementType.TYPE) && target.supports(ElementType.METHOD)) {
				return AnnotationPointcutFactory.forTypeOrMethod().create(annotationType);
			}

			if (target.supports(ElementType.TYPE)) {
				return AnnotationPointcutFactory.forType().create(annotationType);
			}

			if (target.supports(ElementType.METHOD)) {
				return AnnotationPointcutFactory.forMethod().create(annotationType);
			}

			throw new IllegalArgumentException("Annotation '" + annotationType.getName()
					+ "' must support ElementType.TYPE and/or ElementType.METHOD.");
		}

	}

}
