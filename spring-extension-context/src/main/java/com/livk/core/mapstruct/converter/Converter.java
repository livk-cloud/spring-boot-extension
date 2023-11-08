/*
 * Copyright 2021 spring-boot-extension the original author or authors.
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
 *
 */

package com.livk.core.mapstruct.converter;

import com.livk.commons.util.ObjectUtils;
import org.springframework.core.GenericTypeResolver;

/**
 * <p>
 * Converter
 * </p>
 *
 * @param <S> the type parameter
 * @param <T> the type parameter
 * @author livk
 */
public interface Converter<S, T> {

	/**
	 * Gets source.
	 *
	 * @param t the t
	 * @return the source
	 */
	S getSource(T t);

	/**
	 * Gets target.
	 *
	 * @param s the s
	 * @return the target
	 */
	T getTarget(S s);

	/**
	 * Type converter pair.
	 *
	 * @return the converter pair
	 */
	default ConverterPair type() {
		Class<?>[] types = GenericTypeResolver.resolveTypeArguments(this.getClass(), Converter.class);
		if (ObjectUtils.isEmpty(types)) {
			return null;
		}
		return ConverterPair.of(types[0], types[1]);
	}

}
