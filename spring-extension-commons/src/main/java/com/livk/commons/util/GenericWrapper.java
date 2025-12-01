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

import java.util.Optional;

/**
 * 带有泛型的包装器
 *
 * @param <V> the type parameter
 * @author livk
 */
public interface GenericWrapper<V> {

	/**
	 * 构建一个GenericWrapper包装器
	 * @param <T> 相关泛型
	 * @param delegate the value
	 * @return the delegating wrapper
	 */
	static <T> GenericWrapper<T> of(T delegate) {
		return new RecordWrapper<>(delegate);
	}

	/**
	 * 转成optional
	 * @return optional
	 */
	default Optional<V> optional() {
		return Optional.of(this).map(GenericWrapper::unwrap);
	}

	/**
	 * 解析成泛型相关实例
	 * @return 带有泛型的相关实例
	 */
	V unwrap();

}
