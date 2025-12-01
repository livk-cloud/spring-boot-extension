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
import com.livk.context.mapstruct.repository.InMemoryConverterRepository;
import com.livk.context.mapstruct.repository.SpringMapstructLocator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
@SpringJUnitConfig(classes = GenericMapstructServiceTests.Config.class)
class GenericMapstructServiceTests {

	@Autowired
	private GenericMapstructService service;

	@Test
	void test() {
		DoubleConverter converter = new DoubleConverter();

		assertThat(service.addConverter(converter)).isEqualTo(converter);

		assertThat(service.convert("123", Integer.class)).isEqualTo(123);
		assertThat(service.convert("123", Long.class)).isEqualTo(123L);
		assertThat(service.convert("123.1", Double.class)).isEqualTo(123.1);

		assertThat(service.convert(123, String.class)).isEqualTo("123");
		assertThat(service.convert(123L, String.class)).isEqualTo("123");
		assertThat(service.convert(123.1, String.class)).isEqualTo("123.1");
	}

	@TestConfiguration
	static class Config {

		@Bean
		public IntConverter intConverter() {
			return new IntConverter();
		}

		@Bean
		public LongConverter longConverter() {
			return new LongConverter();
		}

		@Bean
		public SpringMapstructLocator springMapstructLocator() {
			return new SpringMapstructLocator();
		}

		@Bean
		public GenericMapstructService mapstructService() {
			return new GenericMapstructService(new InMemoryConverterRepository());
		}

	}

	static class DoubleConverter implements Converter<Double, String> {

		@Override
		public Double getSource(String s) {
			return Double.parseDouble(s);
		}

		@Override
		public String getTarget(Double d) {
			return d.toString();
		}

	}

}
