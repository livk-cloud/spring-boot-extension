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

package com.livk.context.mapstruct.repository;

import com.livk.context.mapstruct.IntConverter;
import com.livk.context.mapstruct.LongConverter;
import com.livk.context.mapstruct.converter.Converter;
import com.livk.context.mapstruct.converter.ConverterPair;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
class InMemoryConverterRepositoryTests {

	@Test
	void test() {
		InMemoryConverterRepository repository = new InMemoryConverterRepository();
		IntConverter intConverter = new IntConverter();
		LongConverter longConverter = new LongConverter();
		repository.put(ConverterPair.of(Integer.class, String.class), intConverter);
		Converter<Long, String> result = repository.computeIfAbsent(ConverterPair.of(Long.class, String.class),
				longConverter);

		assertThat(result).isNotNull();

		assertThat(repository.contains(ConverterPair.of(Integer.class, String.class))).isTrue();
		assertThat(repository.contains(Integer.class, String.class)).isTrue();
		assertThat(repository.contains(ConverterPair.of(String.class, Integer.class))).isFalse();
		assertThat(repository.contains(String.class, Integer.class)).isFalse();

		assertThat(repository.contains(ConverterPair.of(Long.class, String.class))).isTrue();
		assertThat(repository.contains(Long.class, String.class)).isTrue();
		assertThat(repository.contains(ConverterPair.of(String.class, Long.class))).isFalse();
		assertThat(repository.contains(String.class, Long.class)).isFalse();

		assertThat(repository.<Integer, String>get(ConverterPair.of(Integer.class, String.class)))
			.isEqualTo(intConverter);
		assertThat(repository.<Long, String>get(ConverterPair.of(Long.class, String.class))).isEqualTo(longConverter);
	}

}
