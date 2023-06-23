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

import com.livk.commons.bean.util.BeanUtils;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * ReflectionUtils
 * </p>
 *
 * @author livk
 */
@Slf4j
@UtilityClass
public class ReflectionUtils extends org.springframework.util.ReflectionUtils {

	/**
	 * Sets field and accessible.
	 *
	 * @param field     the field
	 * @param parameter the parameter
	 * @param value     the value
	 */
	public void setFieldAndAccessible(Field field, Object parameter, Object value) {
		field.setAccessible(true);
		setField(field, parameter, value);
	}

	/**
	 * Gets read methods.
	 *
	 * @param targetClass the target class
	 * @return the read methods
	 */
	public Set<Method> getReadMethods(Class<?> targetClass) {
		return Arrays.stream(BeanUtils.getPropertyDescriptors(targetClass))
			.map(PropertyDescriptor::getReadMethod)
			.filter(method -> !method.getName().equals("getClass"))
			.collect(Collectors.toSet());
	}

	/**
	 * Gets read method.
	 *
	 * @param targetClass the target class
	 * @param field       the field
	 * @return the read method
	 */
	public Method getReadMethod(Class<?> targetClass, Field field) {
		try {
			PropertyDescriptor descriptor = new PropertyDescriptor(field.getName(), targetClass);
			return descriptor.getReadMethod();
		} catch (Exception e) {
			log.error("获取字段get方法失败 message: {}", e.getMessage(), e);
			return null;
		}
	}

	/**
	 * Gets write methods.
	 *
	 * @param targetClass the target class
	 * @return the write methods
	 */
	public Set<Method> getWriteMethods(Class<?> targetClass) {
		return Arrays.stream(BeanUtils.getPropertyDescriptors(targetClass))
			.map(PropertyDescriptor::getWriteMethod)
			.filter(Objects::nonNull)
			.collect(Collectors.toSet());
	}

	/**
	 * Get write method.
	 *
	 * @param targetClass the target class
	 * @param field       the field
	 * @return the method
	 */
	public Method getWriteMethod(Class<?> targetClass, Field field) {
		try {
			PropertyDescriptor descriptor = new PropertyDescriptor(field.getName(), targetClass);
			return descriptor.getWriteMethod();
		} catch (Exception e) {
			log.error("获取字段set方法失败 message: {}", e.getMessage(), e);
			return null;
		}
	}

	/**
	 * Gets all fields.
	 *
	 * @param targetClass the target class
	 * @return the all fields
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
	 * Gets declared field value.
	 *
	 * @param field  the field
	 * @param target the target
	 * @return the declared field value
	 */
	public static Object getDeclaredFieldValue(Field field, Object target) {
		field.setAccessible(true);
		return ReflectionUtils.getField(field, target);
	}
}
