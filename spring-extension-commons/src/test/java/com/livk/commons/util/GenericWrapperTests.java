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

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
class GenericWrapperTests {

	@Test
	void test() {
		String value = "livk";
		StringBuilder builder = new StringBuilder(value).reverse();

		GenericWrapper<String> wrapper = GenericWrapper.of(value);
		assertThat(wrapper.unwrap()).isEqualTo(value);

		GenericWrapper<String> map = GenericWrapper.of(reverse(wrapper.unwrap()));
		assertThat(map.unwrap()).isEqualTo(builder.toString());
		assertThat(map.unwrap()).isNotEqualTo(value);

		GenericWrapper<String> flatmap = GenericWrapper.of(wrapper.unwrap());
		assertThat(flatmap.unwrap()).isEqualTo(value);
	}

	String reverse(String str) {
		List<String> list = Arrays.asList(str.split(""));
		Collections.reverse(list);
		return String.join("", list);
	}

}
