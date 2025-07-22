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
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author livk
 */
class MultiValueMapSplitterTests {

	@Test
	void split() {
		{
			String param = "names=admin&names=root&password=123456";
			MultiValueMap<String, String> valueMap = MultiValueMapSplitter.of("&", "=").split(param);

			Map<String, List<String>> map = Map.of("names", List.of("admin", "root"), "password", List.of("123456"));

			assertEquals(CollectionUtils.toMultiValueMap(map), valueMap);
		}

		{
			String str = "root=1,2,3&root=4&a=b&a=c";
			Map<String, List<String>> map = Map.of("root", List.of("1", "2", "3", "4"), "a", List.of("b", "c"));
			MultiValueMap<String, String> multiValueMap = MultiValueMapSplitter.of("&", "=").split(str, ",");
			assertEquals(CollectionUtils.toMultiValueMap(map), multiValueMap);
		}
	}

}
