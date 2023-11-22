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

/**
 * 通用包装器
 */
public interface Wrapper {

	/**
	 * 根据类型获取相关实例
	 * <p>
	 * 如果无法进行转换则抛出异常{@link ClassCastException}
	 *
	 * @param <T>  相关泛型
	 * @param type 类信息
	 * @return 相关实例
	 */
	default <T> T unwrap(Class<T> type) {
		if (isWrapperFor(type)) {
			return type.cast(unwrap());
		}
		throw new ClassCastException("cannot be converted to " + type);
	}

	/**
	 * 判断是否可以进行转换
	 *
	 * @param type 类信息
	 * @return bool
	 */
	boolean isWrapperFor(Class<?> type);

	/**
	 * 解析获取相关实例
	 *
	 * @return 相关实例
	 */
	Object unwrap();
}
