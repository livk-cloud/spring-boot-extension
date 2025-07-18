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

import com.google.common.collect.Maps;
import lombok.experimental.UtilityClass;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * <p>
 * Bean相关操作工具类
 * </p>
 *
 * @author livk
 */
@UtilityClass
public class BeanUtils extends org.springframework.beans.BeanUtils {

	/**
	 * 基于BeanUtils的复制
	 * @param <T> 类型
	 * @param source 目标源
	 * @param targetClass 需复制的结果类型
	 * @return result t
	 */
	public <T> T copy(Object source, Class<T> targetClass) {
		return copy(source, () -> instantiateClass(targetClass));
	}

	/**
	 * 基于BeanUtils的复制
	 * @param <T> 类型
	 * @param source 目标源
	 * @param supplier 供应商
	 * @return result t
	 */
	public <T> T copy(Object source, Supplier<T> supplier) {
		if (supplier == null) {
			return null;
		}
		T t = supplier.get();
		if (source != null) {
			copyProperties(source, t);
		}
		return t;
	}

	/**
	 * list类型复制
	 * @param <T> 类型
	 * @param sourceList 目标list
	 * @param targetClass class类型
	 * @return result list
	 */
	public <T> List<T> copyList(Collection<?> sourceList, Class<T> targetClass) {
		return sourceList.stream().map(source -> copy(source, targetClass)).toList();
	}

	/**
	 * 使用BeanWrapper将Bean转成Map
	 * @param source bean
	 * @return Map
	 * @see BeanWrapper
	 */
	public static Map<String, Object> convert(Object source) {
		BeanWrapper beanWrapper = new BeanWrapperImpl(source);
		Map<String, Object> map = new HashMap<>();
		for (PropertyDescriptor descriptor : beanWrapper.getPropertyDescriptors()) {
			String name = descriptor.getName();
			Object propertyValue = beanWrapper.getPropertyValue(name);
			map.put(name, propertyValue);
		}
		return Collections.unmodifiableMap(map);
	}

	@SuppressWarnings("unchecked")
	public static <T> T convert(Map<String, Object> map) {
		if (!map.containsKey("class")) {
			throw new IllegalArgumentException("class must not be null");
		}
		HashMap<String, Object> target = Maps.newHashMap(map);
		Class<T> targetClass = (Class<T>) target.remove("class");
		if (BeanUtils.getResolvableConstructor(targetClass).getParameterCount() != 0) {
			throw new IllegalArgumentException("Missing no-argument constructor");
		}
		BeanWrapper beanWrapper = new BeanWrapperImpl(targetClass);
		beanWrapper.setPropertyValues(target);
		return (T) beanWrapper.getWrappedInstance();
	}

}
