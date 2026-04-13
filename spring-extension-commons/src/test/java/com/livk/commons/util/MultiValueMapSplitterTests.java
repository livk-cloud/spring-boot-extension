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

package com.livk.commons.util;

import com.google.common.base.Splitter;
import org.junit.jupiter.api.Test;
import org.springframework.util.MultiValueMap;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author livk
 */
class MultiValueMapSplitterTests {

	// --- split without value regex ---

	@Test
	void splitWithDuplicateKeys() {
		String param = "names=admin&names=root&password=123456";
		MultiValueMap<String, String> result = MultiValueMapSplitter.of("&", "=").split(param);
		assertThat(result.get("names")).containsExactly("admin", "root");
		assertThat(result.get("password")).containsExactly("123456");
	}

	@Test
	void splitWithoutRegexKeepsCommaInValue() {
		String str = "root=1,2,3&a=b";
		MultiValueMap<String, String> result = MultiValueMapSplitter.of("&", "=").split(str);
		assertThat(result.get("root")).containsExactly("1,2,3");
		assertThat(result.get("a")).containsExactly("b");
	}

	// --- split with value regex ---

	@Test
	void splitWithRegexSplitsValues() {
		String str = "root=1,2,3&root=4&a=b&a=c";
		MultiValueMap<String, String> result = MultiValueMapSplitter.of("&", "=").split(str, ",");
		assertThat(result.get("root")).containsExactly("1", "2", "3", "4");
		assertThat(result.get("a")).containsExactly("b", "c");
	}

	// --- factory method overloads ---

	@Test
	void ofWithSplitterAndSplitter() {
		MultiValueMapSplitter splitter = MultiValueMapSplitter.of(Splitter.on("&"), Splitter.on("="));
		MultiValueMap<String, String> result = splitter.split("a=1&b=2");
		assertThat(result.get("a")).containsExactly("1");
		assertThat(result.get("b")).containsExactly("2");
	}

	@Test
	void ofWithSplitterAndString() {
		MultiValueMapSplitter splitter = MultiValueMapSplitter.of(Splitter.on("&"), "=");
		MultiValueMap<String, String> result = splitter.split("a=1&b=2");
		assertThat(result.get("a")).containsExactly("1");
		assertThat(result.get("b")).containsExactly("2");
	}

	// --- immutability ---

	@Test
	void splitReturnsUnmodifiableMap() {
		MultiValueMap<String, String> result = MultiValueMapSplitter.of("&", "=").split("a=1");
		assertThatThrownBy(() -> result.put("b", List.of("2"))).isInstanceOf(UnsupportedOperationException.class);
	}

	// --- invalid input ---

	@Test
	void splitWithMissingValueThrows() {
		assertThatThrownBy(() -> MultiValueMapSplitter.of("&", "=").split("keyonly"))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void splitWithTooManyPartsThrows() {
		assertThatThrownBy(() -> MultiValueMapSplitter.of("&", "=").split("a=b=c"))
			.isInstanceOf(IllegalArgumentException.class);
	}

}
