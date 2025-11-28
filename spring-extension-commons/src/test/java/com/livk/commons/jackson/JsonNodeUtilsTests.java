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

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.node.JsonNodeFactory;
import tools.jackson.databind.node.ObjectNode;

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
		assertThat(JsonNodeUtils.findStringValue(null, "username")).isNull();
	}

	@Test
	void findValue() {
		Map<String, Object> map = JsonNodeUtils.findValue(node, "info", JsonNodeUtils.STRING_OBJECT_MAP,
				JsonMapper.builder().build());
		assertThat(map).containsExactlyInAnyOrderEntriesOf(Map.of("name", "livk", "email", "1375632510@qq.com"));

		JsonNode nodeMap = JsonNodeUtils.findValue(node, "info", JsonNode.class, JsonMapper.builder().build());
		ObjectNode valNode = new ObjectNode(JsonNodeFactory.instance).put("name", "livk")
			.put("email", "1375632510@qq.com");
		assertThat(nodeMap).isNotNull().isEqualTo(valNode);
	}

	@Test
	void findObjectNode() {
		assertThat(JsonNodeUtils.findObjectNode(null, "info")).isNull();
		JsonNode jsonNode = JsonNodeUtils.findObjectNode(node, "info");
		assertThat(jsonNode.get("name").asString()).isEqualTo("livk");
		assertThat(jsonNode.get("email").asString()).isEqualTo("1375632510@qq.com");
	}

}
