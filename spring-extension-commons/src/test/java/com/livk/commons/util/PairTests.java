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

import com.livk.commons.jackson.JsonMapperUtils;
import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.Test;
import tools.jackson.core.type.TypeReference;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
class PairTests {

	static final Pair<String, Integer> pair = Pair.of("livk", 123456);

	@Test
	void pairJsonSerializerTest() {
		String json = "{\"livk\":123456}";
		String result = JsonMapperUtils.writeValueAsString(pair);
		assertThat(result).isEqualTo(json);
	}

	@Test
	void pairJsonDeserializerTest() {
		Pair<String, Integer> empty = JsonMapperUtils.readValue("{}", new TypeReference<>() {
		});
		assertThat(empty.key()).isNull();
		assertThat(empty.value()).isNull();
		assertThat(empty).isEqualTo(Pair.EMPTY);

		@Language("JSON")
		String json = "{\"livk\":123456}";
		Pair<String, Integer> result = JsonMapperUtils.readValue(json, new TypeReference<>() {
		});
		assertThat(result.key()).isEqualTo("livk");
		assertThat(result.value()).isEqualTo(123456);
		assertThat(result).isEqualTo(pair);

		@Language("JSON")
		String json2 = """
				{"livk": {"root": "username"}}""";
		Pair<String, Pair<String, String>> result2 = JsonMapperUtils.readValue(json2, new TypeReference<>() {
		});
		assertThat(result2.key()).isEqualTo("livk");
		assertThat(result2.value().key()).isEqualTo("root");
		assertThat(result2.value().value()).isEqualTo("username");

		@Language("JSON")
		String json3 = """
				{"livk":  [1,2,3]}""";
		Pair<String, List<Integer>> result3 = JsonMapperUtils.readValue(json3, new TypeReference<>() {
		});
		assertThat(result3.key()).isEqualTo("livk");
		assertThat(result3.value()).containsExactly(1, 2, 3);

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
		assertThat(result4.key()).isEqualTo("livk");
		assertThat(result4.value()).containsAllEntriesOf(Map.of("username", "root", "password", "root"));
	}

}
