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

package com.livk.context.mapstruct;

import com.livk.context.mapstruct.converter.Converter;
import com.livk.context.mapstruct.converter.ConverterPair;
import com.livk.context.mapstruct.converter.MapstructRegistry;
import com.livk.context.mapstruct.converter.MapstructService;
import com.livk.context.mapstruct.exception.ConverterNotFoundException;
import com.livk.context.mapstruct.repository.ConverterRepository;
import com.livk.context.mapstruct.repository.MapstructLocator;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * @author livk
 */
abstract class AbstractMapstructService implements MapstructService, MapstructRegistry, ApplicationContextAware {

	private static final Predicate<MapstructLocator> MATCH = ConverterRepository.class::isInstance;

	/**
	 * The Application context.
	 */
	private final PrioritizedMapstructLocator mapstructLocator = new PrioritizedMapstructLocator();

	@Override
	public <S, T> T convert(S source, Class<T> targetType) {
		@SuppressWarnings("unchecked")
		Class<S> sourceType = (Class<S>) source.getClass();
		Converter<S, T> sourceConverter = this.getConverter(sourceType, targetType);
		if (sourceConverter != null) {
			return sourceConverter.getTarget(source);
		}

		Converter<T, S> targetConverter = this.getConverter(targetType, sourceType);
		if (targetConverter != null) {
			return targetConverter.getSource(source);
		}
		throw new ConverterNotFoundException(source + " to class " + targetType + " not found converter");
	}

	protected <S, T> Converter<S, T> getConverter(Class<S> sourceType, Class<T> targetType) {
		ConverterPair converterPair = ConverterPair.of(sourceType, targetType);
		Converter<S, T> converter = mapstructLocator.get(converterPair);
		if (converter != null) {
			return this.addConverter(converter);
		}
		return null;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		ObjectProvider<MapstructLocator> mapstructLocators = applicationContext.getBeanProvider(MapstructLocator.class);
		mapstructLocators.orderedStream().filter(MATCH.negate()).forEach(mapstructLocator::add);
	}

	private static final class PrioritizedMapstructLocator implements MapstructLocator {

		private final List<MapstructLocator> mapstructLocators = new ArrayList<>();

		@Override
		public <S, T> Converter<S, T> get(ConverterPair converterPair) {
			for (MapstructLocator mapstructLocator : mapstructLocators) {
				Converter<S, T> converter = mapstructLocator.get(converterPair);
				if (converter != null) {
					return converter;
				}
			}
			return null;
		}

		public void add(MapstructLocator mapstructLocator) {
			this.mapstructLocators.add(mapstructLocator);
		}

	}

}
