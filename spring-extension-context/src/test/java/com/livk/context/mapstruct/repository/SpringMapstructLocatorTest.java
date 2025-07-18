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

import com.livk.context.mapstruct.IntConverter;
import com.livk.context.mapstruct.LongConverter;
import com.livk.context.mapstruct.converter.ConverterPair;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author livk
 */
@SpringJUnitConfig(classes = SpringMapstructLocatorTest.Config.class)
class SpringMapstructLocatorTest {

	@Autowired
	MapstructLocator mapstructLocator;

	@Test
	void test() {
		assertNotNull(mapstructLocator.get(ConverterPair.of(Integer.class, String.class)));
		assertNotNull(mapstructLocator.get(ConverterPair.of(Long.class, String.class)));
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

	}

}
