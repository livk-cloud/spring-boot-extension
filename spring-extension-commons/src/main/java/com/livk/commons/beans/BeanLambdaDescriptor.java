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

package com.livk.commons.beans;

import com.livk.commons.util.BeanUtils;
import com.livk.commons.util.ClassUtils;
import com.livk.commons.util.Pair;
import com.livk.commons.util.ReflectionUtils;
import lombok.Getter;
import lombok.SneakyThrows;

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
class BeanLambdaDescriptor {

	private static final Map<Pair<Class<?>, String>, BeanLambdaDescriptor> cache = new ConcurrentHashMap<>(128);

	private final Class<?> type;

	private final String methodName;

	private PropertyDescriptor propertyDescriptor;

	private BeanLambdaDescriptor(Class<?> type, String methodName) {
		this.type = type;
		this.methodName = methodName;
		initField();
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
	public static <T> BeanLambdaDescriptor create(BeanLambdaFunc<T> function) {
		Method writeReplace = function.getClass().getDeclaredMethod("writeReplace");
		writeReplace.setAccessible(true);
		SerializedLambda serializedLambda = (SerializedLambda) writeReplace.invoke(function);
		String className = ClassUtils.convertResourcePathToClassName(serializedLambda.getImplClass());
		Class<?> type = ClassUtils.resolveClassName(className);
		return cache.computeIfAbsent(Pair.of(type, serializedLambda.getImplMethodName()),
				pair -> new BeanLambdaDescriptor(pair.key(), pair.value()));
	}

	private void initField() {
		PropertyDescriptor[] propertyDescriptors = BeanUtils.getPropertyDescriptors(type);
		for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
			if (propertyDescriptor.getReadMethod().getName().equals(methodName)) {
				this.propertyDescriptor = propertyDescriptor;
			}
		}
	}

	public String getFieldName() {
		return propertyDescriptor != null ? propertyDescriptor.getName() : null;
	}

	public Field getField() {
		if (propertyDescriptor != null) {
			String fieldName = getFieldName();
			Class<?> fieldType = propertyDescriptor.getPropertyType();
			return ReflectionUtils.findField(type, fieldName, fieldType);
		}
		return null;
	}

	@SneakyThrows
	public Method getMethod() {
		return ReflectionUtils.findMethod(type, methodName);
	}

}
