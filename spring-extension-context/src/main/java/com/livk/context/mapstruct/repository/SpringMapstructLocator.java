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

package com.livk.context.mapstruct.repository;

import com.livk.context.mapstruct.converter.Converter;
import com.livk.context.mapstruct.converter.ConverterPair;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.ResolvableType;
import org.jspecify.annotations.Nullable;

/**
 * The type Spring mapstruct locator.
 *
 * @author livk
 */
public class SpringMapstructLocator implements MapstructLocator, ApplicationContextAware {

	/**
	 * The Application context.
	 */
	private ApplicationContext applicationContext;

	@Override
	public <S, T> Converter<S, T> get(ConverterPair converterPair) {
		Class<?> sourceType = converterPair.getSourceType();
		Class<?> targetType = converterPair.getTargetType();
		ResolvableType resolvableType = ResolvableType.forClassWithGenerics(Converter.class, sourceType, targetType);
		return applicationContext.<Converter<S, T>>getBeanProvider(resolvableType).getIfUnique();
	}

	@Override
	public void setApplicationContext(@Nullable ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

}
