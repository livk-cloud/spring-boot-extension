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

package com.livk.commons.util;

import lombok.Getter;
import lombok.SneakyThrows;
import org.springframework.util.Assert;

import java.beans.PropertyDescriptor;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * BeanLambda的相关方法或者字段处理
 *
 * @author livk
 */
@Getter
final class BeanLambdaDescriptor {

	private static final Map<Method, BeanLambdaDescriptor> cache = new ConcurrentHashMap<>(128);

	private final Method method;

	private BeanLambdaDescriptor(Method method) {
		this.method = method;
	}

	private PropertyDescriptor getPropertyDescriptor() {
		Class<?> type = method.getDeclaringClass();
		PropertyDescriptor propertyDescriptor = BeanUtils.findPropertyForMethod(method, type);
		if (propertyDescriptor == null) {
			throw new IllegalStateException("propertyDescriptor must not be null");
		}
		return propertyDescriptor;
	}

	/**
	 * 静态构建根据{@link BeanLambdaDescriptor}
	 * <p>
	 * 使用缓存避免无效加载
	 * @param <T> 相关泛型
	 * @param function BeanLambdaFunc表达式
	 * @return BeanLambdaDescriptor
	 */
	@SneakyThrows
	public static <T> BeanLambdaDescriptor create(BeanLambda<T> function) {
		Method writeReplace = function.getClass().getDeclaredMethod("writeReplace");
		writeReplace.setAccessible(true);
		SerializedLambda serializedLambda = (SerializedLambda) writeReplace.invoke(function);
		String className = ClassUtils.convertResourcePathToClassName(serializedLambda.getImplClass());
		Class<?> type = ClassUtils.resolveClassName(className, ClassUtils.getDefaultClassLoader());
		Method method = ReflectionUtils.findMethod(type, serializedLambda.getImplMethodName());
		Assert.notNull(method, "method must not be null");
		return cache.computeIfAbsent(method, BeanLambdaDescriptor::new);
	}

	public String getFieldName() {
		return getPropertyDescriptor().getName();
	}

	public Field getField() {
		PropertyDescriptor propertyDescriptor = getPropertyDescriptor();
		String fieldName = propertyDescriptor.getName();
		Class<?> fieldType = propertyDescriptor.getPropertyType();
		Class<?> type = method.getDeclaringClass();
		return ReflectionUtils.findField(type, fieldName, fieldType);
	}

	public String getMethodName() {
		return method.getName();
	}

}
