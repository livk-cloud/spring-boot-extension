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

package com.livk.context.mapstruct.converter;

/**
 * <p>
 * MapstructFactory
 * </p>
 *
 * @author livk
 */
public interface MapstructRegistry {

	/**
	 * Add converter.
	 * @param converterPair the converter pair
	 * @param converter the converter
	 * @return the converter
	 */
	<S, T> Converter<S, T> addConverter(ConverterPair converterPair, Converter<S, T> converter);

	/**
	 * Add converter.
	 * @param converter the converter
	 * @return the converter
	 */
	default <S, T> Converter<S, T> addConverter(Converter<S, T> converter) {
		ConverterPair converterPair = ConverterPair.of(converter);
		if (converterPair != null) {
			return this.addConverter(converterPair, converter);
		}
		return converter;
	}

	/**
	 * Add converter.
	 * @param <S> the type parameter
	 * @param <T> the type parameter
	 * @param sourceType the source type
	 * @param targetType the target type
	 * @param converter the converter
	 * @return the converter
	 */
	default <S, T> Converter<S, T> addConverter(Class<S> sourceType, Class<T> targetType, Converter<S, T> converter) {
		ConverterPair converterPair = ConverterPair.of(sourceType, targetType);
		return this.addConverter(converterPair, converter);
	}

}
