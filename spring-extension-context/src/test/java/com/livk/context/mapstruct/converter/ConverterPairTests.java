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

package com.livk.context.mapstruct.converter;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
class ConverterPairTests {

	@Test
	void of() {
		ConverterPair pair = ConverterPair.of(String.class, Object.class);

		assertThat(pair.getSourceType()).isEqualTo(String.class);
		assertThat(pair.getTargetType()).isEqualTo(Object.class);

		ConverterPair converterPair = ConverterPair.of(new TestConverter());

		assertThat(converterPair).isNotNull();
		assertThat(converterPair.getSourceType()).isEqualTo(String.class);
		assertThat(converterPair.getTargetType()).isEqualTo(Object.class);
	}

	static class TestConverter implements Converter<String, Object> {

		@Override
		public String getSource(Object o) {
			return o.toString();
		}

		@Override
		public Object getTarget(String s) {
			return s;
		}

	}

}
