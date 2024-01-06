/*
 * Copyright 2021 spring-boot-extension the original author or authors.
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
 *
 */

package com.livk.commons.util;

import lombok.experimental.UtilityClass;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Method;

/**
 * <p>
 * Annotation工具类
 * </p>
 *
 * @author livk
 */
@UtilityClass
public class AnnotationUtils extends org.springframework.core.annotation.AnnotationUtils {

	/**
	 * 获取方法上或者类路径上的注解,方法级别优先,类路径允许复合注解
	 * @param <A> annotation泛型
	 * @param methodParameter parameter
	 * @param annotationClass annotation
	 * @return annotation annotation element
	 */
	public <A extends Annotation> A getAnnotationElement(MethodParameter methodParameter, Class<A> annotationClass) {
		A annotation = getAnnotation(methodParameter.getAnnotatedElement(), annotationClass);
		if (annotation == null) {
			Class<?> containingClass = methodParameter.getContainingClass();
			annotation = getAnnotation(containingClass, annotationClass);
		}
		return annotation;
	}

	/**
	 * 获取方法上或者类路径上的注解,方法级别优先,类路径允许复合注解
	 * @param <A> annotation泛型
	 * @param method method
	 * @param annotationClass annotation
	 * @return annotation annotation element
	 */
	public <A extends Annotation> A getAnnotationElement(Method method, Class<A> annotationClass) {
		A annotation = getAnnotation(method, annotationClass);
		if (annotation == null) {
			annotation = getAnnotation(method.getDeclaringClass(), annotationClass);
		}
		return annotation;
	}

	/**
	 * 判断方法上或者类路径上是否包含注解,类路径允许复合注解
	 * @param <A> annotation泛型
	 * @param methodParameter parameter
	 * @param annotationClass annotation
	 * @return bool boolean
	 */
	public <A extends Annotation> boolean hasAnnotationElement(MethodParameter methodParameter,
			Class<A> annotationClass) {
		Class<?> containingClass = methodParameter.getContainingClass();
		return (AnnotatedElementUtils.hasAnnotation(containingClass, annotationClass)
				|| AnnotatedElementUtils.hasAnnotation(methodParameter.getAnnotatedElement(), annotationClass));
	}

	/**
	 * 判断方法上或者类路径上是否包含注解,类路径允许复合注解
	 * @param <A> annotation泛型
	 * @param method method
	 * @param annotationClass annotation
	 * @return bool boolean
	 */
	public <A extends Annotation> boolean hasAnnotationElement(Method method, Class<A> annotationClass) {
		return AnnotatedElementUtils.hasAnnotation(method, annotationClass)
				|| AnnotatedElementUtils.hasAnnotation(method.getDeclaringClass(), annotationClass);
	}

	/**
	 * 构建AnnotationAttributes
	 * @param metadata the metadata
	 * @param annotationClassName the annotation class name
	 * @return the annotation attributes
	 */
	public AnnotationAttributes attributesFor(AnnotatedTypeMetadata metadata, String annotationClassName) {
		return AnnotationAttributes.fromMap(metadata.getAnnotationAttributes(annotationClassName));
	}

	/**
	 * 构建AnnotationAttributes
	 * @param <A> 注解类型
	 * @param metadata the metadata
	 * @param annotationClass the annotation class
	 * @return the annotation attributes
	 */
	public <A extends Annotation> AnnotationAttributes attributesFor(AnnotatedTypeMetadata metadata,
			Class<A> annotationClass) {
		return attributesFor(metadata, annotationClass.getName());
	}

	/**
	 * 根据key获取AnnotationAttributes数据，并转成枚举数组
	 * @param attributes AnnotationAttributes
	 * @param key key
	 * @param <E> 枚举类型
	 * @return E[]
	 */
	@SuppressWarnings("unchecked")
	public <E extends Enum<?>> E[] getValue(AnnotationAttributes attributes, String key) {
		Object value = attributes.get(key);
		if (!(value instanceof Enum<?>[]) && Enum[].class.getComponentType().isInstance(value)) {
			Object array = Array.newInstance(Enum[].class.getComponentType(), 1);
			Array.set(array, 0, value);
			value = array;
		}
		return (E[]) value;
	}

}
