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

import com.google.common.collect.Maps;
import lombok.experimental.UtilityClass;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Bean与Map之间的转换工具类
 * </p>
 *
 * @author livk
 */
@UtilityClass
public class BeanConverter {

	/**
	 * 使用BeanWrapper将Bean转成Map
	 * @param bean bean对象
	 * @return map
	 * @see BeanWrapper
	 */
	public static Map<String, Object> toMap(Object bean) {
		BeanWrapper beanWrapper = new BeanWrapperImpl(bean);
		Map<String, Object> map = new HashMap<>();
		for (PropertyDescriptor descriptor : beanWrapper.getPropertyDescriptors()) {
			String name = descriptor.getName();
			Object propertyValue = beanWrapper.getPropertyValue(name);
			map.put(name, propertyValue);
		}
		return Collections.unmodifiableMap(map);
	}

	/**
	 * 使用BeanWrapper将Map转成Bean
	 * @param <T> 类型
	 * @param map map数据
	 * @param clazz 目标类型
	 * @return bean实例
	 */
	public static <T> T fromMap(Map<String, Object> map, Class<T> clazz) {
		if (org.springframework.beans.BeanUtils.getResolvableConstructor(clazz).getParameterCount() != 0) {
			throw new IllegalArgumentException("Missing no-argument constructor");
		}
		BeanWrapper beanWrapper = new BeanWrapperImpl(clazz);
		beanWrapper.setPropertyValues(map);
		@SuppressWarnings("unchecked")
		T result = (T) beanWrapper.getWrappedInstance();
		return result;
	}

	@SuppressWarnings("unchecked")
	public static <T> T fromMap(Map<String, Object> map) {
		if (!map.containsKey("class")) {
			throw new IllegalArgumentException("class must not be null");
		}
		HashMap<String, Object> target = Maps.newHashMap(map);
		Class<T> targetClass = (Class<T>) target.remove("class");
		return BeanConverter.fromMap(target, targetClass);
	}

}
