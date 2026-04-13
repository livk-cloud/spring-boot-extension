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
import com.livk.context.mapstruct.converter.Converter;
import com.livk.context.mapstruct.converter.ConverterPair;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
class InMemoryConverterRepositoryTests {

	final InMemoryConverterRepository repository = new InMemoryConverterRepository();

	final ConverterPair pair = ConverterPair.of(Integer.class, String.class);

	final IntConverter converter = new IntConverter();

	@Test
	void putAndGet() {
		repository.put(pair, converter);
		Converter<Integer, String> result = repository.get(pair);
		assertThat(result).isSameAs(converter);
	}

	@Test
	void containsReturnsTrueAfterPut() {
		repository.put(pair, converter);
		assertThat(repository.contains(pair)).isTrue();
	}

	@Test
	void containsReturnsFalseWhenEmpty() {
		assertThat(repository.contains(pair)).isFalse();
	}

	@Test
	void containsWithClassesDelegate() {
		repository.put(pair, converter);
		assertThat(repository.contains(Integer.class, String.class)).isTrue();
	}

	@Test
	void getReturnsNullWhenNotFound() {
		assertThat(repository.get(pair)).isNull();
	}

	@Test
	void computeIfAbsentAddsWhenMissing() {
		Converter<Integer, String> result = repository.computeIfAbsent(pair, converter);
		assertThat(result).isSameAs(converter);
		assertThat(repository.contains(pair)).isTrue();
	}

	@Test
	void computeIfAbsentReturnsExistingWhenPresent() {
		repository.put(pair, converter);
		IntConverter another = new IntConverter();
		Converter<Integer, String> result = repository.computeIfAbsent(pair, another);
		assertThat(result).isSameAs(converter);
	}

}
