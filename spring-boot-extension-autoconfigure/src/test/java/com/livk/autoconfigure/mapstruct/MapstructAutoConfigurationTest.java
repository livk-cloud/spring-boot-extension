/*
 * Copyright 2021-2024 spring-boot-extension the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *       https://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.livk.autoconfigure.mapstruct;

import com.livk.context.mapstruct.GenericMapstructService;
import com.livk.context.mapstruct.repository.ConverterRepository;
import com.livk.context.mapstruct.repository.SpringMapstructLocator;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
class MapstructAutoConfigurationTest {

	final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
		.withConfiguration(AutoConfigurations.of(MapstructAutoConfiguration.class));

	@Test
	void genericMapstructService() {
		this.contextRunner.run((context) -> {
			assertThat(context).hasSingleBean(GenericMapstructService.class);
		});
	}

	@Test
	void converterRepository() {
		this.contextRunner.run((context) -> {
			assertThat(context).hasSingleBean(ConverterRepository.class);
		});
	}

	@Test
	void springMapstructLocator() {
		this.contextRunner.run((context) -> {
			assertThat(context).hasSingleBean(SpringMapstructLocator.class);
		});
	}

}
