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

package com.livk.autoconfigure.mapstruct;

import com.livk.auto.service.annotation.SpringAutoService;
import com.livk.context.mapstruct.GenericMapstructService;
import com.livk.context.mapstruct.repository.ConverterRepository;
import com.livk.context.mapstruct.repository.InMemoryConverterRepository;
import com.livk.context.mapstruct.repository.MapstructLocator;
import com.livk.context.mapstruct.repository.SpringMapstructLocator;
import org.mapstruct.factory.Mappers;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * @author livk
 */
@AutoConfiguration
@SpringAutoService
@ConditionalOnClass(Mappers.class)
public class MapstructAutoConfiguration {

	/**
	 * Generic mapstruct service generic mapstruct service.
	 * @param repository the repository
	 * @return the generic mapstruct service
	 */
	@Bean
	public GenericMapstructService genericMapstructService(ConverterRepository repository) {
		return new GenericMapstructService(repository);
	}

	/**
	 * Converter repository converter repository.
	 * @return the converter repository
	 */
	@Bean
	@ConditionalOnMissingBean
	public ConverterRepository converterRepository() {
		return new InMemoryConverterRepository();
	}

	/**
	 * Spring mapstruct locator mapstruct locator.
	 * @return the mapstruct locator
	 */
	@Bean
	public MapstructLocator springMapstructLocator() {
		return new SpringMapstructLocator();
	}

}
