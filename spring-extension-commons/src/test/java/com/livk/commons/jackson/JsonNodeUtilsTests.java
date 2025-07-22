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

package com.livk.commons.jackson;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
class JsonNodeUtilsTests {

	static final String json = """
			{
			  "username": "root",
			  "password": "root",
			  "info": {
			    "name": "livk",
			    "email": "1375632510@qq.com"
			  }
			}""";

	static final JsonNode node = JsonMapperUtils.readTree(json);

	@Test
	void findStringValue() {
		String username = JsonNodeUtils.findStringValue(node, "username");
		assertThat(username).isEqualTo("root");
	}

	@Test
	void findValue() {
		Map<String, Object> map = JsonNodeUtils.findValue(node, "info", JsonNodeUtils.STRING_OBJECT_MAP,
				JsonMapper.builder().build());
		assertThat(map).containsExactlyInAnyOrderEntriesOf(Map.of("name", "livk", "email", "1375632510@qq.com"));
	}

	@Test
	void findObjectNode() {
		JsonNode jsonNode = JsonNodeUtils.findObjectNode(node, "info");
		assertThat(jsonNode.get("name").asText()).isEqualTo("livk");
		assertThat(jsonNode.get("email").asText()).isEqualTo("1375632510@qq.com");
	}

}
