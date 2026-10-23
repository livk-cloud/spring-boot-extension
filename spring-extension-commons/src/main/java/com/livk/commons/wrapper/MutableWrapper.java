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
 * 可变的值包装器，支持设置和更新内部值.
 *
 * @param <T> the type parameter
 * @author livk
 */
public interface MutableWrapper<T> extends ValueWrapper<T> {

	/**
	 * 设置被包装的值.
	 * <p>
	 * 当模式为 {@link Mode#ONCE} 且已经初始化时，后续写入将被忽略.
	 * @param value the value
	 */
	void set(@NonNull T value);

	/**
	 * 判断包装器是否已经初始化（至少成功写入过一次）.
	 * @return {@code true} 表示已初始化
	 */
	boolean isInitialized();

	/**
	 * 按指定写入模式构建一个未初始化的可变包装器.
	 * @param <T> 相关泛型
	 * @param mode the write mode
	 * @return the mutable wrapper
	 */
	static <T> MutableWrapper<T> mutable(Mode mode) {
		return new MutableWrapperImpl<>(mode);
	}

	/**
	 * 使用给定初始值构建可变包装器，写入模式为 {@link Mode#MULTIPLE}.
	 * @param <T> 相关泛型
	 * @param value the value
	 * @return the mutable wrapper
	 */
	static <T> MutableWrapper<T> mutable(@NonNull T value) {
		return new MutableWrapperImpl<>(value);
	}

	/**
	 * 可变包装器的写入模式.
	 */
	enum Mode {

		/**
		 * 允许多次写入.
		 */
		MULTIPLE,

		/**
		 * 仅允许写入一次.
		 */
		ONCE

	}

}
