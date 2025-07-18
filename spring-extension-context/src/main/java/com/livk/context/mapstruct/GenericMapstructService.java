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

package com.livk.context.mapstruct;

import com.livk.context.mapstruct.converter.Converter;
import com.livk.context.mapstruct.converter.ConverterPair;
import com.livk.context.mapstruct.converter.MapstructRegistry;
import com.livk.context.mapstruct.repository.ConverterRepository;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 * Generic
 * </p>
 *
 * @author livk
 */
@RequiredArgsConstructor
public class GenericMapstructService extends AbstractMapstructService implements MapstructRegistry {

	private final ConverterRepository converterRepository;

	@Override
	public <S, T> Converter<S, T> addConverter(ConverterPair converterPair, Converter<S, T> converter) {
		return converterRepository.computeIfAbsent(converterPair, converter);
	}

	@Override
	protected <S, T> Converter<S, T> getConverter(Class<S> sourceType, Class<T> targetType) {
		Converter<S, T> converter = super.getConverter(sourceType, targetType);
		if (converter == null) {
			converter = converterRepository.get(ConverterPair.of(sourceType, targetType));
		}
		if (converter != null) {
			this.addConverter(converter);
		}
		return converter;
	}

}
