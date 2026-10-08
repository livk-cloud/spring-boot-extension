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

package com.livk.commons.wrapper;

import org.jspecify.annotations.NonNull;

/**
 * 值包装器，用于封装并解析内部值.
 *
 * @param <T> the type parameter
 * @author livk
 */
public interface ValueWrapper<T> {

	/**
	 * 解析并返回被包装的值.
	 * @return 被包装的值
	 */
	T unwrap();

	/**
	 * 构建一个不可变的值包装器.
	 * @param <T> 相关泛型
	 * @param value the value
	 * @return the immutable wrapper
	 */
	static <T> ValueWrapper<T> immutable(@NonNull T value) {
		return new ImmutableWrapperImpl<>(value);
	}

}
