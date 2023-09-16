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
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * <p>
 * BeanUtils
 * </p>
 *
 * @author livk
 */
@UtilityClass
public class BeanUtils extends org.springframework.beans.BeanUtils {

	/**
	 * 基于BeanUtils的复制
	 *
	 * @param <T>         类型
	 * @param source      目标源
	 * @param targetClass 需复制的结果类型
	 * @return result t
	 */
	public <T> T copy(Object source, Class<T> targetClass) {
		return copy(source, () -> instantiateClass(targetClass));
	}

	/**
	 * 基于BeanUtils的复制
	 *
	 * @param <T>      类型
	 * @param source   目标源
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
	 *
	 * @param <T>         类型
	 * @param sourceList  目标list
	 * @param targetClass class类型
	 * @return result list
	 */
	public <T> List<T> copyList(Collection<?> sourceList, Class<T> targetClass) {
		return sourceList.stream().map(source -> copy(source, targetClass)).toList();
	}

	/**
	 * Is field null boolean.
	 *
	 * @param source the source
	 * @return the boolean
	 */
	public static boolean isFieldNull(Object source) {
		if (source == null) {
			return true;
		}
		return Predicates.create(source.getClass().getDeclaredFields())
			.anyChecked(field -> ReflectionUtils.getDeclaredFieldValue(field, source) == null);
	}

	/**
	 * 使用BeanWrapper将Bean转成Map
	 *
	 * @param source bean
	 * @return Map
	 * @see BeanWrapper
	 */
	public static Map<String, Object> convert(Object source) {
		BeanWrapper beanWrapper = new BeanWrapperImpl(source);
		PropertyDescriptor[] descriptors = beanWrapper.getPropertyDescriptors();
		Map<String, Object> map = new HashMap<>();
		for (PropertyDescriptor descriptor : descriptors) {
			if ("class".equals(descriptor.getName())) {
				continue;
			}
			Object propertyValue = beanWrapper.getPropertyValue(descriptor.getName());
			map.put(descriptor.getName(), propertyValue);
		}
		return map;
	}
}
