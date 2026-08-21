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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 反射相关工具类.
 * </p>
 *
 * @author livk
 * @deprecated use {@link FieldUtils}
 */
@Slf4j
@UtilityClass
@Deprecated(since = "2.1.1")
public class ReflectionUtils extends org.springframework.util.ReflectionUtils {

	/**
	 * 给field设置accessible为true,并且设置一个值.
	 * @param field field
	 * @param parameter parameter
	 * @param value value
	 */
	public void setFieldAndAccessible(Field field, Object parameter, Object value) {
		FieldUtils.setFieldAndAccessible(field, parameter, value);
	}

	/**
	 * 获取一个类的所有的Get方法.
	 * @param targetClass class
	 * @return read methods
	 */
	public Set<Method> getReadMethods(Class<?> targetClass) {
		return FieldUtils.getReadMethods(targetClass);
	}

	/**
	 * 获取一个类的Field Get方法.
	 * @param targetClass class
	 * @param field field
	 * @return read method
	 */
	public Method getReadMethod(Class<?> targetClass, Field field) {
		return FieldUtils.getReadMethod(targetClass, field);
	}

	/**
	 * 获取一个类的所有的Set方法.
	 * @param targetClass target class
	 * @return write methods
	 */
	public Set<Method> getWriteMethods(Class<?> targetClass) {
		return FieldUtils.getWriteMethods(targetClass);
	}

	/**
	 * 获取一个类的Field Set方法.
	 * @param targetClass class
	 * @param field field
	 * @return method
	 */
	public Method getWriteMethod(Class<?> targetClass, Field field) {
		return FieldUtils.getWriteMethod(targetClass, field);
	}

	/**
	 * 获取一个类的所有Field,包括所有的父类.
	 * @param targetClass class
	 * @return fields
	 */
	public List<Field> getAllFields(Class<?> targetClass) {
		return FieldUtils.getAllFields(targetClass);
	}

	/**
	 * 获取一个私有属性的值.
	 * @param field field
	 * @param target target
	 * @return declared field value
	 */
	public static Object getDeclaredFieldValue(Field field, Object target) {
		return FieldUtils.getDeclaredFieldValue(field, target);
	}

}
