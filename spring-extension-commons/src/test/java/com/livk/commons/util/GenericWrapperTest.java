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

package com.livk.commons.util;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author livk
 */
class GenericWrapperTest {

	@Test
	void test() {
		String value = "livk";
		StringBuilder builder = new StringBuilder(value).reverse();
		GenericWrapper<String> wrapper = GenericWrapper.of(value);
		assertEquals(value, wrapper.unwrap(String.class));
		assertEquals(value, wrapper.unwrap());
		assertTrue(wrapper.isWrapperFor(String.class));

		GenericWrapper<String> map = wrapper.map(StringUtils::reverse);
		assertEquals(builder.toString(), map.unwrap());
		assertNotEquals(value, map.unwrap());

		GenericWrapper<String> flatmap = wrapper.flatmap(GenericWrapper::of);
		assertEquals(value, flatmap.unwrap());
	}

}
