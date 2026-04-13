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

	// --- basic accessors ---

	@Test
	void keyAndValueReturnConstructorArgs() {
		assertThat(pair.key()).isEqualTo("livk");
		assertThat(pair.value()).isEqualTo(123456);
	}

	@Test
	void ofFromMapEntry() {
		Map.Entry<String, Integer> entry = Map.entry("livk", 123456);
		Pair<String, Integer> result = Pair.of(entry);
		assertThat(result.key()).isEqualTo("livk");
		assertThat(result.value()).isEqualTo(123456);
		assertThat(result).isEqualTo(pair);
	}

	// --- equals / hashCode / toString / clone ---

	@Test
	void equalsAndHashCode() {
		Pair<String, Integer> same = Pair.of("livk", 123456);
		Pair<String, Integer> different = Pair.of("other", 0);
		assertThat(pair).isEqualTo(same);
		assertThat(pair.hashCode()).isEqualTo(same.hashCode());
		assertThat(pair).isNotEqualTo(different);
	}

	@Test
	void toStringFormat() {
		assertThat(pair.toString()).isEqualTo("{livk:123456}");
	}

	@Test
	void cloneReturnsCopy() {
		Pair<String, Integer> cloned = pair.clone();
		assertThat(cloned).isEqualTo(pair).isNotSameAs(pair);
	}

	// --- serialization ---

	@Test
	void serializesToJson() {
		assertThat(JsonMapperUtils.writeValueAsString(pair)).isEqualTo("{\"livk\":123456}");
	}

	// --- deserialization ---

	@Test
	void deserializesEmptyObjectToEmptyPair() {
		Pair<String, Integer> empty = JsonMapperUtils.readValue("{}", new TypeReference<>() {
		});
		assertThat(empty.key()).isNull();
		assertThat(empty.value()).isNull();
		assertThat(empty).isEqualTo(Pair.EMPTY);
	}

	@Test
	void deserializesSimpleKeyValue() {
		@Language("JSON")
		String json = "{\"livk\":123456}";
		Pair<String, Integer> result = JsonMapperUtils.readValue(json, new TypeReference<>() {
		});
		assertThat(result).isEqualTo(pair);
	}

	@Test
	void deserializesNestedPairValue() {
		@Language("JSON")
		String json = """
				{"livk": {"root": "username"}}""";
		Pair<String, Pair<String, String>> result = JsonMapperUtils.readValue(json, new TypeReference<>() {
		});
		assertThat(result.key()).isEqualTo("livk");
		assertThat(result.value().key()).isEqualTo("root");
		assertThat(result.value().value()).isEqualTo("username");
	}

	@Test
	void deserializesListValue() {
		@Language("JSON")
		String json = """
				{"livk": [1,2,3]}""";
		Pair<String, List<Integer>> result = JsonMapperUtils.readValue(json, new TypeReference<>() {
		});
		assertThat(result.key()).isEqualTo("livk");
		assertThat(result.value()).containsExactly(1, 2, 3);
	}

	@Test
	void deserializesMapValue() {
		@Language("JSON")
		String json = """
				{
				  "livk": {
				    "username": "root",
				    "password": "root"
				  }
				}""";
		Pair<String, Map<String, String>> result = JsonMapperUtils.readValue(json, new TypeReference<>() {
		});
		assertThat(result.key()).isEqualTo("livk");
		assertThat(result.value()).containsAllEntriesOf(Map.of("username", "root", "password", "root"));
	}

}
