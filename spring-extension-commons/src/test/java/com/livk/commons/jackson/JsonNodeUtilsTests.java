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

import org.junit.jupiter.api.Test;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;
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
			  "count": 42,
			  "info": {
			    "name": "livk",
			    "email": "1375632510@qq.com"
			  }
			}""";

	static final JsonNode node = JsonMapperUtils.readTree(json);

	static final JsonMapper mapper = JsonMapper.builder().build();

	@Test
	void findStringValueReturnsValue() {
		assertThat(JsonNodeUtils.findStringValue(node, "username")).isEqualTo("root");
	}

	@Test
	void findStringValueWithNullNodeReturnsNull() {
		assertThat(JsonNodeUtils.findStringValue(null, "username")).isNull();
	}

	@Test
	void findStringValueWithMissingFieldReturnsNull() {
		assertThat(JsonNodeUtils.findStringValue(node, "nonexistent")).isNull();
	}

	@Test
	void findStringValueWithNonStringFieldReturnsNull() {
		assertThat(JsonNodeUtils.findStringValue(node, "count")).isNull();
	}

	@Test
	void findObjectNodeReturnsObjectNode() {
		JsonNode jsonNode = JsonNodeUtils.findObjectNode(node, "info");
		assertThat(jsonNode).isNotNull();
		assertThat(jsonNode.get("name").asString()).isEqualTo("livk");
		assertThat(jsonNode.get("email").asString()).isEqualTo("1375632510@qq.com");
	}

	@Test
	void findObjectNodeWithNullNodeReturnsNull() {
		assertThat(JsonNodeUtils.findObjectNode(null, "info")).isNull();
	}

	@Test
	void findObjectNodeWithMissingFieldReturnsNull() {
		assertThat(JsonNodeUtils.findObjectNode(node, "nonexistent")).isNull();
	}

	@Test
	void findObjectNodeWithNonObjectFieldReturnsNull() {
		assertThat(JsonNodeUtils.findObjectNode(node, "username")).isNull();
	}

	@Test
	void findValueWithTypeReference() {
		Map<String, Object> map = JsonNodeUtils.findValue(node, "info", JsonNodeUtils.STRING_OBJECT_MAP, mapper);
		assertThat(map).containsExactlyInAnyOrderEntriesOf(Map.of("name", "livk", "email", "1375632510@qq.com"));
	}

	@Test
	void findValueWithClassType() {
		JsonNode result = JsonNodeUtils.findValue(node, "info", JsonNode.class, mapper);
		ObjectNode expected = new ObjectNode(JsonNodeFactory.instance).put("name", "livk")
			.put("email", "1375632510@qq.com");
		assertThat(result).isNotNull().isEqualTo(expected);
	}

	@Test
	void findValueWithJavaType() {
		Map<String, Object> map = JsonNodeUtils.findValue(node, "info",
				TypeFactoryUtils.javaType(JsonNodeUtils.STRING_OBJECT_MAP), mapper);
		assertThat(map).containsExactlyInAnyOrderEntriesOf(Map.of("name", "livk", "email", "1375632510@qq.com"));
	}

	@Test
	void findValueWithNullNodeReturnsNull() {
		assertThat(JsonNodeUtils.findValue(null, "info", JsonNodeUtils.STRING_OBJECT_MAP, mapper)).isNull();
	}

	@Test
	void findValueWithMissingFieldReturnsNull() {
		assertThat(JsonNodeUtils.findValue(node, "nonexistent", JsonNodeUtils.STRING_OBJECT_MAP, mapper)).isNull();
	}

	@Test
	void findValueWithNonContainerFieldReturnsNull() {
		assertThat(JsonNodeUtils.findValue(node, "username", JsonNodeUtils.STRING_OBJECT_MAP, mapper)).isNull();
	}

}
