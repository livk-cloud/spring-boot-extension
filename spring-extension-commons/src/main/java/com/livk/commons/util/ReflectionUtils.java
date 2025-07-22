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

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 反射相关工具类
 * </p>
 *
 * @author livk
 */
@Slf4j
@UtilityClass
public class ReflectionUtils extends org.springframework.util.ReflectionUtils {

	/**
	 * 给field设置accessible为true,并且设置一个值
	 * @param field field
	 * @param parameter parameter
	 * @param value value
	 */
	public void setFieldAndAccessible(Field field, Object parameter, Object value) {
		field.setAccessible(true);
		setField(field, parameter, value);
	}

	/**
	 * 获取一个类的所有的Get方法
	 * @param targetClass class
	 * @return read methods
	 */
	public Set<Method> getReadMethods(Class<?> targetClass) {
		return Arrays.stream(BeanUtils.getPropertyDescriptors(targetClass))
			.map(PropertyDescriptor::getReadMethod)
			.filter(method -> !method.getName().equals("getClass"))
			.collect(Collectors.toSet());
	}

	/**
	 * 获取一个类的Field Get方法
	 * @param targetClass class
	 * @param field field
	 * @return read method
	 */
	public Method getReadMethod(Class<?> targetClass, Field field) {
		try {
			PropertyDescriptor descriptor = new PropertyDescriptor(field.getName(), targetClass);
			return descriptor.getReadMethod();
		}
		catch (Exception ex) {
			log.error("Failed to get the get field method message: {}", ex.getMessage(), ex);
			return null;
		}
	}

	/**
	 * 获取一个类的所有的Set方法
	 * @param targetClass target class
	 * @return write methods
	 */
	public Set<Method> getWriteMethods(Class<?> targetClass) {
		return Arrays.stream(BeanUtils.getPropertyDescriptors(targetClass))
			.map(PropertyDescriptor::getWriteMethod)
			.filter(Objects::nonNull)
			.collect(Collectors.toSet());
	}

	/**
	 * 获取一个类的Field Set方法
	 * @param targetClass class
	 * @param field field
	 * @return method
	 */
	public Method getWriteMethod(Class<?> targetClass, Field field) {
		try {
			PropertyDescriptor descriptor = new PropertyDescriptor(field.getName(), targetClass);
			return descriptor.getWriteMethod();
		}
		catch (Exception ex) {
			log.error("Failed to set the get field method message: {}", ex.getMessage(), ex);
			return null;
		}
	}

	/**
	 * 获取一个类的所有Field,包括所有的父类
	 * @param targetClass class
	 * @return fields
	 */
	public List<Field> getAllFields(Class<?> targetClass) {
		List<Field> allFields = new ArrayList<>();
		Class<?> currentClass = targetClass;
		while (currentClass != null) {
			Field[] declaredFields = currentClass.getDeclaredFields();
			Collections.addAll(allFields, declaredFields);
			currentClass = currentClass.getSuperclass();
		}
		return allFields;
	}

	/**
	 * 获取一个私有属性的值
	 * @param field field
	 * @param target target
	 * @return declared field value
	 */
	public static Object getDeclaredFieldValue(Field field, Object target) {
		field.setAccessible(true);
		return ReflectionUtils.getField(field, target);
	}

}
