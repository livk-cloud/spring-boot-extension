/*
 * Copyright 2021 spring-boot-extension the original author or authors.
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
 *
 */

package com.livk.commons.jackson;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.livk.commons.jackson.util.JsonMapperUtils;
import com.livk.commons.jackson.util.JsonNodeUtils;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * <p>
 * JsonNodeUtilsTest
 * </p>
 *
 * @author livk
 */
class JsonNodeUtilsTest {

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
		assertEquals("root", username);
	}

	@Test
	void findValue() {
		Map<String, Object> map = JsonNodeUtils.findValue(node, "info", JsonNodeUtils.STRING_OBJECT_MAP,
				JsonMapper.builder().build());
		assertEquals(Map.of("name", "livk", "email", "1375632510@qq.com"), map);
	}

	@Test
	void findObjectNode() {
		JsonNode jsonNode = JsonNodeUtils.findObjectNode(node, "info");
		assertEquals("livk", jsonNode.get("name").asText());
		assertEquals("1375632510@qq.com", jsonNode.get("email").asText());
	}

	@Test
	void getNodeFirst() {
		JsonNode node = JsonMapperUtils.readTree("""
				{
				    "c": "1",
				    "a": "2",
				    "b": {
				        "c": 3
				    }
				}""");
		assertEquals("1", JsonNodeUtils.findNodeFirst(node, "c").asText());
		assertEquals("2", JsonNodeUtils.findNodeFirst(node, "a").asText());
	}

	@Test
	void getNodeFirstAll() {
		JsonNode node = JsonMapperUtils.readTree("""
				{
				    "c": "1",
				    "a": "2",
				    "b": {
				        "c": "3",
				        "a": "4"
				    }
				}""");
		assertArrayEquals(new String[] { "1", "3" },
				JsonNodeUtils.findNodeAll(node, "c").stream().map(JsonNode::asText).toArray(String[]::new));
		assertArrayEquals(new String[] { "2", "4" },
				JsonNodeUtils.findNodeAll(node, "a").stream().map(JsonNode::asText).toArray(String[]::new));
	}

	@Test
	void getNode() {
		JsonNode node = JsonMapperUtils.readTree("""
				{
				    "c": "1",
				    "a": "2",
				    "b": {
				        "c": "3",
				        "d": {
				            "ab": "6"
				        }
				    },
				    "f": [
				        {
				            "a": "7"
				        },
				        {
				            "a": "8"
				        },
				        {
				            "a": "9"
				        }
				    ]
				}""");
		assertEquals("1", JsonNodeUtils.findNode(node, "c").asText());
		assertEquals("2", JsonNodeUtils.findNode(node, "a").asText());
		assertEquals("3", JsonNodeUtils.findNode(node, "b.c").asText());
		assertEquals("6", JsonNodeUtils.findNode(node, "b.d.ab").asText());
		assertEquals("7", JsonNodeUtils.findNode(node, "f.0.a").asText());
		assertEquals("8", JsonNodeUtils.findNode(node, "f.1.a").asText());
		assertEquals("9", JsonNodeUtils.findNode(node, "f.2.a").asText());
	}

}
