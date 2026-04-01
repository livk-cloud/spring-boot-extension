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

package com.livk.commons.util;

import lombok.Getter;
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

	private static final Map<String, BeanLambdaDescriptor> cache = new ConcurrentHashMap<>(128);

	private final Method method;

	private BeanLambdaDescriptor(Method method) {
		this.method = method;
	}

	private PropertyDescriptor getPropertyDescriptor() {
		Class<?> type = method.getDeclaringClass();
		PropertyDescriptor propertyDescriptor = BeanUtils.findPropertyForMethod(method, type);
		if (propertyDescriptor == null) {
			throw new IllegalStateException("No PropertyDescriptor found for method: " + method);
		}
		return propertyDescriptor;
	}

	/**
	 * 静态构建根据{@link BeanLambdaDescriptor}
	 * <p>
	 * 使用缓存避免无效加载
	 * @param <T> 相关泛型
	 * @param function beanLambdaFunc表达式
	 * @return beanLambdaDescriptor
	 */
	public static <T> BeanLambdaDescriptor create(BeanLambda<T> function) {
		SerializedLambda serializedLambda = resolveSerializedLambda(function);
		String key = serializedLambda.getImplClass() + "#" + serializedLambda.getImplMethodName();
		return cache.computeIfAbsent(key, k -> doCreate(serializedLambda));
	}

	private static SerializedLambda resolveSerializedLambda(BeanLambda<?> function) {
		try {
			Method writeReplace = function.getClass().getDeclaredMethod("writeReplace");
			writeReplace.setAccessible(true);
			return (SerializedLambda) writeReplace.invoke(function);
		}
		catch (Exception ex) {
			throw new IllegalStateException("Failed to resolve lambda: " + function, ex);
		}
	}

	private static BeanLambdaDescriptor doCreate(SerializedLambda serializedLambda) {
		String className = ClassUtils.convertResourcePathToClassName(serializedLambda.getImplClass());
		Class<?> type = ClassUtils.resolveClassName(className, ClassUtils.getDefaultClassLoader());
		Method method = ReflectionUtils.findMethod(type, serializedLambda.getImplMethodName());
		Assert.notNull(method, "Cannot find method: " + serializedLambda.getImplMethodName() + " on " + className);
		return new BeanLambdaDescriptor(method);
	}

	public String getFieldName() {
		return getPropertyDescriptor().getName();
	}

	public Field getField() {
		PropertyDescriptor propertyDescriptor = getPropertyDescriptor();
		String fieldName = propertyDescriptor.getName();
		Class<?> fieldType = propertyDescriptor.getPropertyType();
		Class<?> type = method.getDeclaringClass();
		Field field = ReflectionUtils.findField(type, fieldName, fieldType);
		Assert.notNull(field, "Field '" + fieldName + "' of type '" + fieldType.getName() + "' not found on class: "
				+ type.getName());
		return field;
	}

	public String getMethodName() {
		return method.getName();
	}

}
