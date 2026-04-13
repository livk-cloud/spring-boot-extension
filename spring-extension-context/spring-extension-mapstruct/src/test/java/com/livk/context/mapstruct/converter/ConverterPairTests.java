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

import com.livk.context.mapstruct.IntConverter;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
class ConverterPairTests {

	@Test
	void ofWithClasses() {
		ConverterPair pair = ConverterPair.of(String.class, Integer.class);
		assertThat(pair.getSourceType()).isEqualTo(String.class);
		assertThat(pair.getTargetType()).isEqualTo(Integer.class);
	}

	@Test
	void ofWithConverter() {
		ConverterPair pair = ConverterPair.of(new IntConverter());
		assertThat(pair).isNotNull();
		assertThat(pair.getSourceType()).isEqualTo(Integer.class);
		assertThat(pair.getTargetType()).isEqualTo(String.class);
	}

	@Test
	void equalsAndHashCode() {
		ConverterPair a = ConverterPair.of(String.class, Integer.class);
		ConverterPair b = ConverterPair.of(String.class, Integer.class);
		assertThat(a).isEqualTo(b);
		assertThat(a.hashCode()).isEqualTo(b.hashCode());
	}

	@Test
	void notEqualWhenDifferent() {
		ConverterPair a = ConverterPair.of(String.class, Integer.class);
		ConverterPair b = ConverterPair.of(String.class, Long.class);
		assertThat(a).isNotEqualTo(b);
	}

}
