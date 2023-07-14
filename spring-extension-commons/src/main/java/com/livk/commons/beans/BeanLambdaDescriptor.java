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
 * The type Bean lambda descriptor.
 *
 * @author livk
 */
@Getter
class BeanLambdaDescriptor {

	private static final Map<Pair<Class<?>, Method>, BeanLambdaDescriptor> cache = new ConcurrentHashMap<>(128);

	private final Class<?> type;

	private final Method method;

	private PropertyDescriptor propertyDescriptor;

	private Field field;


	/**
	 * Create bean lambda descriptor.
	 *
	 * @param <T>      the type parameter
	 * @param function the function
	 * @return the bean lambda descriptor
	 */
	@SneakyThrows
	public static <T> BeanLambdaDescriptor create(BeanLambdaFunc<T> function) {
		Method writeReplace = function.getClass().getDeclaredMethod("writeReplace");
		writeReplace.setAccessible(true);
		SerializedLambda serializedLambda = (SerializedLambda) writeReplace.invoke(function);
		String className = ClassUtils.convertResourcePathToClassName(serializedLambda.getImplClass());
		Class<?> type = ClassUtils.resolveClassName(className, ClassUtils.getDefaultClassLoader());
		Method method = type.getMethod(serializedLambda.getImplMethodName());
		return cache.computeIfAbsent(Pair.of(type, method), pair -> new BeanLambdaDescriptor(pair.key(), pair.value()));
	}

	private BeanLambdaDescriptor(Class<?> type, Method method) {
		this.type = type;
		this.method = method;
		initField();
	}

	private void initField() {
		PropertyDescriptor[] propertyDescriptors = BeanUtils.getPropertyDescriptors(type);
		for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
			if (propertyDescriptor.getReadMethod().equals(method)) {
				this.propertyDescriptor = propertyDescriptor;
				String fieldName = propertyDescriptor.getName();
				Class<?> fieldType = propertyDescriptor.getPropertyType();
				this.field = ReflectionUtils.findField(type, fieldName, fieldType);
			}
		}
	}
}
