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

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
class YamlUtilsTests {

	// --- convertMapToYaml ---

	@Test
	void convertMapToYamlWithSimpleKeys() {
		Map<String, Object> flat = Map.of("spring.redis.host", "livk.com", "spring.redis.port", 5672);
		Map<String, Object> result = YamlUtils.convertMapToYaml(flat);

		assertThat(result).containsKey("spring");
		@SuppressWarnings("unchecked")
		Map<String, Object> spring = (Map<String, Object>) result.get("spring");
		@SuppressWarnings("unchecked")
		Map<String, Object> redis = (Map<String, Object>) spring.get("redis");
		assertThat(redis).containsEntry("host", "livk.com").containsEntry("port", 5672);
	}

	@Test
	void convertMapToYamlWithArrayIndexKeys() {
		Map<String, Object> flat = Map.of("spring.env[0]", 1, "spring.env[1]", 2);
		Map<String, Object> result = YamlUtils.convertMapToYaml(flat);

		@SuppressWarnings("unchecked")
		Map<String, Object> spring = (Map<String, Object>) result.get("spring");
		assertThat(spring.get("env")).isEqualTo(List.of(1, 2));
	}

	// --- convertYamlToMap ---

	@Test
	void convertYamlToMapWithNestedMap() {
		Map<String, Object> yaml = Map.of("spring", Map.of("redis", Map.of("host", "livk.com", "port", 5672)));
		Properties result = YamlUtils.convertYamlToMap(yaml);
		assertThat(result).containsEntry("spring.redis.host", "livk.com").containsEntry("spring.redis.port", 5672);
	}

	@Test
	void convertYamlToMapWithList() {
		Map<String, Object> yaml = Map.of("spring", Map.of("env", List.of(1, 2)));
		Properties result = YamlUtils.convertYamlToMap(yaml);
		assertThat(result).containsEntry("spring.env[0]", 1).containsEntry("spring.env[1]", 2);
	}

	@Test
	void convertYamlToMapWithEmptyCollection() {
		Map<String, Object> yaml = Map.of("tags", Collections.emptyList());
		Properties result = YamlUtils.convertYamlToMap(yaml);
		assertThat(result).containsEntry("tags", "");
	}

	// --- toYml ---

	@Test
	void toYmlProducesValidYamlString() {
		Map<String, Object> flat = Map.of("a.b", "value");
		String yml = YamlUtils.toYml(flat);
		assertThat(yml).contains("a:").contains("b: value");
	}

	@Test
	void toYmlWithEmptyMapReturnsEmptyString() {
		assertThat(YamlUtils.toYml(Collections.emptyMap())).isEmpty();
	}

	@Test
	void toYmlWithNullMapReturnsEmptyString() {
		assertThat(YamlUtils.toYml(null)).isEmpty();
	}

	// --- round-trip ---

	@Test
	void roundTripConversion() {
		Map<String, Object> flat = Map.of("spring.redis.host", "livk.com", "spring.redis.port", 5672, "spring.env[0]",
				1, "spring.env[1]", 2);
		Map<String, Object> yamlMap = YamlUtils.convertMapToYaml(flat);
		Properties backToFlat = YamlUtils.convertYamlToMap(yamlMap);
		assertThat(backToFlat).containsAllEntriesOf(flat);
	}

}
