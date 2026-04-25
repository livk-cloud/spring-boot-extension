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

package com.livk.commons.web;

import com.livk.commons.util.HttpServletUtils;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author livk
 */
class HttpParametersTests {

	// --- constructors ---

	@Test
	void defaultConstructorCreatesEmptyParameters() {
		HttpParameters parameters = new HttpParameters();
		assertThat(parameters).isEmpty();
	}

	@Test
	void constructorWithMultiValueMap() {
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("key", "value");
		HttpParameters parameters = new HttpParameters(map);
		assertThat(parameters.getFirst("key")).isEqualTo("value");
	}

	@Test
	void constructorWithNullThrows() {
		assertThatThrownBy(() -> new HttpParameters((MultiValueMap<String, String>) null))
			.isInstanceOf(IllegalArgumentException.class);
	}

	// --- from request ---

	@Test
	void paramsFromRequestWithMultipleValues() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addParameter("username", "livk", "root", "admin");
		HttpParameters parameters = HttpServletUtils.params(request);
		assertThat(parameters.get("username")).containsExactly("livk", "root", "admin");
		assertThat(parameters.getFirst("username")).isEqualTo("livk");
	}

	@Test
	void paramsFromRequestWithSingleValue() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addParameter("password", "123456");
		HttpParameters parameters = HttpServletUtils.params(request);
		assertThat(parameters.getFirst("password")).isEqualTo("123456");
	}

	// --- getOrEmpty ---

	@Test
	void getOrEmptyReturnsValuesWhenPresent() {
		HttpParameters parameters = new HttpParameters();
		parameters.add("key", "value");
		assertThat(parameters.getOrEmpty("key")).containsExactly("value");
	}

	@Test
	void getOrEmptyReturnsEmptyListWhenAbsent() {
		HttpParameters parameters = new HttpParameters();
		assertThat(parameters.getOrEmpty("missing")).isEmpty();
	}

	// --- equals / hashCode ---

	@Test
	void equalsAndHashCode() {
		HttpParameters a = new HttpParameters();
		a.add("key", "value");
		HttpParameters b = new HttpParameters();
		b.add("key", "value");
		assertThat(a).isEqualTo(b);
		assertThat(a.hashCode()).isEqualTo(b.hashCode());
	}

	@Test
	void notEqualWhenDifferent() {
		HttpParameters a = new HttpParameters();
		a.add("key", "a");
		HttpParameters b = new HttpParameters();
		b.add("key", "b");
		assertThat(a).isNotEqualTo(b);
	}

	// --- toString / formatParameters ---

	@Test
	void toStringFormatsSingleValue() {
		HttpParameters parameters = new HttpParameters();
		parameters.add("name", "livk");
		assertThat(parameters.toString()).contains("name:\"livk\"");
	}

	@Test
	void toStringFormatsMultipleValues() {
		HttpParameters parameters = new HttpParameters();
		parameters.addAll("name", List.of("a", "b"));
		assertThat(parameters.toString()).contains("name:\"a\", \"b\"");
	}

	@Test
	void formatParametersProducesExpectedFormat() {
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("k", "v");
		assertThat(HttpParameters.formatParameters(map)).isEqualTo("[k:\"v\"]");
	}

}
