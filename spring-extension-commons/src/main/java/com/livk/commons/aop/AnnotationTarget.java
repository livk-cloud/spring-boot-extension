/*
 * Copyright 2021-2024 spring-boot-extension the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *       https://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.livk.commons.aop;

import com.google.common.collect.Sets;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author livk
 */
final class AnnotationTarget<A extends Annotation> {

	private static final ConcurrentMap<Class<? extends Annotation>, AnnotationTarget<?>> CACHE = new ConcurrentHashMap<>();

	@SuppressWarnings("unchecked")
	public static <A extends Annotation> AnnotationTarget<A> of(Class<A> annotationType) {
		return (AnnotationTarget<A>) CACHE.computeIfAbsent(annotationType, AnnotationTarget::new);
	}

	private final Set<ElementType> elementTypes;

	private AnnotationTarget(Class<A> annotationType) {
		Target target = annotationType.getAnnotation(Target.class);
		this.elementTypes = Sets.newHashSet(target.value());
	}

	public boolean supportMethod() {
		return elementTypes.contains(ElementType.METHOD);
	}

	public boolean supportType() {
		return elementTypes.contains(ElementType.TYPE);
	}

}
