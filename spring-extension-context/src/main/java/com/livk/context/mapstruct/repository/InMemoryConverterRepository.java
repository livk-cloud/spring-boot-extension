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

package com.livk.context.mapstruct.repository;

import com.livk.context.mapstruct.converter.Converter;
import com.livk.context.mapstruct.converter.ConverterPair;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 * InMemoryConverterRepository
 * </p>
 *
 * @author livk
 */
public class InMemoryConverterRepository implements ConverterRepository {

	private final Map<ConverterPair, Converter<?, ?>> converterMap = new ConcurrentHashMap<>();

	@Override
	public boolean contains(ConverterPair converterPair) {
		return converterMap.containsKey(converterPair);
	}

	@Override
	public <S, T> void put(ConverterPair converterPair, Converter<S, T> converter) {
		converterMap.put(converterPair, converter);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <S, T> Converter<S, T> get(ConverterPair converterPair) {
		return (Converter<S, T>) converterMap.get(converterPair);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <S, T> Converter<S, T> computeIfAbsent(ConverterPair converterPair, Converter<S, T> converter) {
		return (Converter<S, T>) converterMap.computeIfAbsent(converterPair, pair -> converter);
	}

}
