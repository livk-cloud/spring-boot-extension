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

import org.springframework.aop.Pointcut;
import org.springframework.lang.NonNull;

import java.lang.annotation.Annotation;

/**
 * 使用{@see AnnotationPointcutType}的注解型切点处理器
 *
 * @param <A> the type parameter
 * @author livk
 * @see AnnotationPointcutType
 * @see AnnotationAbstractPointcutAdvisor
 */
public abstract class AnnotationAbstractPointcutTypeAdvisor<A extends Annotation>
		extends AnnotationAbstractPointcutAdvisor<A> {

	@NonNull
	@Override
	public Pointcut getPointcut() {
		return pointcutType().getPointcut(annotationType);
	}

	/**
	 * <p>
	 * 用于指定不同的切点类型，默认为{@link AnnotationPointcutType#AUTO}
	 * </p>
	 * @return the annotation pointcut type
	 */
	protected AnnotationPointcutType pointcutType() {
		return AnnotationPointcutType.AUTO;
	}

}
