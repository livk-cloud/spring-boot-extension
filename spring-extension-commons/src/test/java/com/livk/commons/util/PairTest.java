/*
 * Copyright 2021-2024 spring-boot-extension the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.livk.commons.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.livk.commons.jackson.util.JsonMapperUtils;
import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * <p>
 * PairTest
 * </p>
 *
 * @author livk
 */
class PairTest {

	static Pair<String, Integer> pair = Pair.of("livk", 123456);

	@Test
	void toMap() {
		assertEquals(pair.toMap(), Map.of("livk", 123456));
		assertEquals(Map.entry("livk", 123456), pair.toEntry());
	}

	@Test
	void map() {
		Pair<String, String> map = pair.map(String::toUpperCase, String::valueOf);
		assertEquals("LIVK", map.key());
		assertEquals("123456", map.value());

		Pair<String, Integer> keyMap = pair.keyMap(String::toUpperCase);
		assertEquals("LIVK", keyMap.key());
		assertEquals(123456, keyMap.value());

		Pair<String, String> valueMap = pair.valueMap(String::valueOf);
		assertEquals("livk", valueMap.key());
		assertEquals("123456", valueMap.value());

		Pair<String, String> flatMap = pair.flatMap((key, value) -> Pair.of(key.toUpperCase(), String.valueOf(value)));
		assertEquals("LIVK", flatMap.key());
		assertEquals("123456", flatMap.value());
	}

	@Test
	void pairJsonSerializerTest() {
		String json = "{\"livk\":123456}";
		String result = JsonMapperUtils.writeValueAsString(pair);
		assertEquals(json, result);
	}

	@Test
	void pairJsonDeserializerTest() {

		Pair<String, Integer> empty = JsonMapperUtils.readValue("{}", new TypeReference<>() {
		});
		assertNull(empty.key());
		assertNull(empty.value());
		assertEquals(Pair.EMPTY, empty);

		@Language("JSON")
		String json = "{\"livk\":123456}";
		Pair<String, Integer> result = JsonMapperUtils.readValue(json, new TypeReference<>() {
		});
		assertEquals("livk", result.key());
		assertEquals(123456, result.value());
		assertEquals(pair, result);

		@Language("JSON")
		String json2 = """
				{"livk": {"root": "username"}}""";
		Pair<String, Pair<String, String>> result2 = JsonMapperUtils.readValue(json2, new TypeReference<>() {
		});
		assertEquals("livk", result2.key());
		assertEquals("root", result2.value().key());
		assertEquals("username", result2.value().value());

		@Language("JSON")
		String json3 = """
				{"livk":  [1,2,3]}""";
		Pair<String, List<Integer>> result3 = JsonMapperUtils.readValue(json3, new TypeReference<>() {
		});
		assertEquals("livk", result3.key());
		assertEquals(List.of(1, 2, 3), result3.value());

		@Language("JSON")
		String json4 = """
				{
				  "livk": {
				    "username": "root",
				    "password": "root"
				  }
				}""";
		Pair<String, Map<String, String>> result4 = JsonMapperUtils.readValue(json4, new TypeReference<>() {
		});
		assertEquals("livk", result4.key());
		assertEquals(Map.of("username", "root", "password", "root"), result4.value());
	}

}
