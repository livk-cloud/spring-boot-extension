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

import com.google.common.collect.Sets;
import lombok.RequiredArgsConstructor;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.util.HashSet;

/**
 * <p>
 * {@link AnnotationAutoPointcut} 的泛型实现
 * </p>
 * <p>
 * 用于便捷的获取各种切点
 * </p>
 *
 * @author livk
 * @see AnnotationAutoPointcut
 */
@RequiredArgsConstructor
public enum AnnotationPointcutType implements AnnotationAutoPointcut {

	/**
	 * 用于指定类级别的切点
	 */
	TYPE(AnnotationMatchingPointcut::forClassAnnotation),

	/**
	 * 用于指定方法级别的切点
	 */
	METHOD(AnnotationMatchingPointcut::forMethodAnnotation),

	/**
	 * 用于指定类级别或方法级别的切点
	 */
	TYPE_OR_METHOD(AnnotationClassOrMethodPointcut::new),

	/**
	 * 自动识别，根据注解上的元注解信息
	 *
	 * @see Target#value()
	 */
	AUTO(annotationType -> {
		Target target = annotationType.getAnnotation(Target.class);
		HashSet<ElementType> elementTypeHashSet = Sets.newHashSet(target.value());
		if (elementTypeHashSet.contains(ElementType.TYPE) && elementTypeHashSet.contains(ElementType.METHOD)) {
			return TYPE_OR_METHOD.getPointcut(annotationType);
		}
		else if (elementTypeHashSet.contains(ElementType.TYPE)) {
			return TYPE.getPointcut(annotationType);
		}
		else if (elementTypeHashSet.contains(ElementType.METHOD)) {
			return METHOD.getPointcut(annotationType);
		}
		else {
			throw new IllegalArgumentException(
					"annotation:" + annotationType + " Missing " + Target.class + " TYPE or METHOD information");
		}
	});

	private final AnnotationAutoPointcut annotationAutoPointcut;

	@Override
	public Pointcut getPointcut(Class<? extends Annotation> annotationType) {
		return annotationAutoPointcut.getPointcut(annotationType);
	}

}
