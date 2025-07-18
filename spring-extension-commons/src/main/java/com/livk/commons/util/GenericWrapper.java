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

import java.util.Optional;
import java.util.function.Function;

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
	 * 根据类型获取相关实例
	 * <p>
	 * 如果无法进行转换则抛出异常{@link ClassCastException}
	 * @param <T> 相关泛型
	 * @param type 类信息
	 * @return 相关实例
	 */
	@Deprecated(since = "1.4.0", forRemoval = true)
	default <T> T unwrap(Class<T> type) {
		if (isWrapperFor(type)) {
			return type.cast(unwrap());
		}
		throw new ClassCastException("cannot be converted to " + type);
	}

	/**
	 * 判断是否可以进行转换
	 * @param type 类信息
	 * @return bool
	 */
	@Deprecated(since = "1.4.3", forRemoval = true)
	default boolean isWrapperFor(Class<?> type) {
		return type.isInstance(unwrap());
	}

	/**
	 * 进行map转换
	 * @param function fun
	 * @param <R> 转换后泛型
	 * @return wrapper
	 */
	@Deprecated(since = "1.4.0", forRemoval = true)
	default <R> GenericWrapper<R> map(Function<V, R> function) {
		V unwrap = this.unwrap();
		return of(function.apply(unwrap));
	}

	/**
	 * 进行flatmap转换
	 * @param function fun
	 * @return wrapper
	 */
	@Deprecated(since = "1.4.0", forRemoval = true)
	default <G extends GenericWrapper<?>> G flatmap(Function<V, G> function) {
		return function.apply(this.unwrap());
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
