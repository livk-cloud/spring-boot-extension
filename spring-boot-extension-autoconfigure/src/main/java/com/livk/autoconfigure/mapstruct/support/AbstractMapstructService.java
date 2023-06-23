/*
 * Copyright 2021 spring-boot-extension the original author or authors.
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
 *
 */

package com.livk.autoconfigure.mapstruct.support;

import com.livk.autoconfigure.mapstruct.converter.Converter;
import com.livk.autoconfigure.mapstruct.converter.ConverterPair;
import com.livk.autoconfigure.mapstruct.converter.MapstructRegistry;
import com.livk.autoconfigure.mapstruct.converter.MapstructService;
import com.livk.autoconfigure.mapstruct.exception.ConverterNotFoundException;
import com.livk.autoconfigure.mapstruct.repository.MapstructLocator;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * <p>
 * AbstractMapstructService
 * </p>
 *
 * @author livk
 */
public abstract class AbstractMapstructService implements MapstructService, MapstructRegistry, ApplicationContextAware {

	/**
	 * The Application context.
	 */
	private MapstructLocator mapstructLocator;

	@SuppressWarnings("unchecked")
	@Override
	public <S, T> T convert(S source, Class<T> targetType) {
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

	private <S, T> Converter<S, T> getConverter(Class<S> sourceType, Class<T> targetType) {
		ConverterPair converterPair = ConverterPair.of(sourceType, targetType);
		return mapstructLocator.get(converterPair);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		ObjectProvider<MapstructLocator> mapstructLocators = applicationContext.getBeanProvider(MapstructLocator.class);
		this.mapstructLocator = new PrioritizedMapstructLocator(this, mapstructLocators);
	}
}
