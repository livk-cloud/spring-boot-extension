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
import org.springframework.util.Assert;

/**
 * {@link MutableWrapper} 的默认实现.
 *
 * @param <T> the type parameter
 * @author livk
 */
final class MutableWrapperImpl<T> implements MutableWrapper<T> {

	private T value;

	private boolean initialized = false;

	private final Mode mode;

	/**
	 * 按写入模式创建未初始化的包装器.
	 * @param mode the write mode
	 */
	MutableWrapperImpl(Mode mode) {
		this.mode = mode;
	}

	/**
	 * 使用初始值创建包装器，写入模式固定为 {@link Mode#MULTIPLE}.
	 * @param value the value
	 */
	MutableWrapperImpl(T value) {
		Assert.notNull(value, "MutableWrapper init value not be null");
		this.value = value;
		this.mode = Mode.MULTIPLE;
		this.initialized = true;
	}

	@Override
	public T unwrap() {
		return this.value;
	}

	@Override
	public void set(@NonNull T value) {
		Assert.notNull(value, "MutableWrapper init value not be null");
		if (this.isInitialized() && this.mode == Mode.ONCE) {
			return;
		}
		this.value = value;
		this.initialized = true;
	}

	@Override
	public boolean isInitialized() {
		return this.initialized;
	}

}
