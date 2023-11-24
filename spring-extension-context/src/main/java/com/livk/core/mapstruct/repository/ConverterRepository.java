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

package com.livk.core.mapstruct.repository;

import com.livk.core.mapstruct.converter.Converter;
import com.livk.core.mapstruct.converter.ConverterPair;

/**
 * <p>
 * AbstractFactory
 * </p>
 *
 * @author livk
 */
public interface ConverterRepository extends MapstructLocator {

	/**
	 * Contains boolean.
	 * @param sourceType the source type
	 * @param targetType the target type
	 * @return the boolean
	 */
	default boolean contains(Class<?> sourceType, Class<?> targetType) {
		return this.contains(ConverterPair.of(sourceType, targetType));
	}

	/**
	 * Contains boolean.
	 * @param converterPair the converter pair
	 * @return the boolean
	 */
	boolean contains(ConverterPair converterPair);

	/**
	 * Put.
	 * @param converterPair the converter pair
	 * @param converter the converter
	 */
	void put(ConverterPair converterPair, Converter<?, ?> converter);

	/**
	 * Compute if absent.
	 * @param converterPair the converter pair
	 * @param converter the converter
	 * @return the converter
	 */
	Converter<?, ?> computeIfAbsent(ConverterPair converterPair, Converter<?, ?> converter);

}
