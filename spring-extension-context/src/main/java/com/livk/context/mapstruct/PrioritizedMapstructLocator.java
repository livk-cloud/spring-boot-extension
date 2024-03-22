/*
 * Copyright 2021-2024 spring-boot-extension the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.livk.context.mapstruct;

import com.livk.context.mapstruct.converter.Converter;
import com.livk.context.mapstruct.converter.ConverterPair;
import com.livk.context.mapstruct.converter.MapstructRegistry;
import com.livk.context.mapstruct.repository.ConverterRepository;
import com.livk.context.mapstruct.repository.MapstructLocator;
import org.springframework.beans.factory.ObjectProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * @author livk
 */
class PrioritizedMapstructLocator implements MapstructLocator {

	private final MapstructRegistry mapstructRegistry;

	private final List<MapstructLocator> mapstructLocators = new ArrayList<>();

	private ConverterRepository converterRepository;

	public PrioritizedMapstructLocator(MapstructRegistry mapstructRegistry,
			ObjectProvider<MapstructLocator> mapstructLocatorObjectProvider) {
		this.mapstructRegistry = mapstructRegistry;
		for (MapstructLocator mapstructLocator : mapstructLocatorObjectProvider.orderedStream().toList()) {
			if (mapstructLocator instanceof ConverterRepository repository) {
				this.converterRepository = repository;
			}
			else {
				mapstructLocators.add(mapstructLocator);
			}
		}
	}

	@Override
	public <S, T> Converter<S, T> get(ConverterPair converterPair) {
		Converter<S, T> converter = converterRepository.get(converterPair);
		if (converter == null) {
			for (MapstructLocator mapstructLocator : mapstructLocators) {
				converter = mapstructLocator.get(converterPair);
				if (converter != null) {
					return mapstructRegistry.addConverter(converterPair, converter);
				}
			}
		}
		return converter;
	}

}
