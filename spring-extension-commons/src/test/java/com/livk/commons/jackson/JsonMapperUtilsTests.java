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

import com.livk.commons.util.Pair;
import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import tools.jackson.core.JsonParser;
import tools.jackson.core.ObjectReadContext;
import tools.jackson.core.json.JsonFactory;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.type.CollectionType;
import tools.jackson.databind.type.MapType;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
class JsonMapperUtilsTests {

	static final JavaType javaType = TypeFactoryUtils.javaType(JsonNode.class);

	@Language("json")
	static final String json = """
			{
			                    "c": "1",
			                    "a": "2",
			                    "b": {
			                        "c": 3
			                    }
			                }""";

	private static void assertJsonContent(JsonNode node) {
		assertThat(node).isNotNull();
		assertThat(node.get("c").asString()).isEqualTo("1");
		assertThat(node.get("a").asString()).isEqualTo("2");
		assertThat(node.get("b").get("c").asInt()).isEqualTo(3);
	}

	@Test
	void testReadValueWithJsonNodeClass() {
		JsonFactory jsonFactory = new JsonFactory();
		try (JsonParser parser = jsonFactory.createParser(ObjectReadContext.empty(), json)) {
			assertJsonContent(JsonMapperUtils.readValue(parser, JsonNode.class));
		}
	}

	@Test
	void testReadValueWithTypeReference() {
		JsonFactory jsonFactory = new JsonFactory();
		try (JsonParser parser = jsonFactory.createParser(ObjectReadContext.empty(), json)) {
			assertJsonContent(JsonMapperUtils.readValue(parser, new TypeReference<>() {
			}));
		}
	}

	@Test
	void testReadValueWithJavaType() {
		JsonFactory jsonFactory = new JsonFactory();
		try (JsonParser parser = jsonFactory.createParser(ObjectReadContext.empty(), json)) {
			assertJsonContent(JsonMapperUtils.readValue(parser, javaType));
		}
	}

	@Test
	void readValueFile() throws IOException {
		File file = new ClassPathResource("input.json").getFile();
		assertJsonContent(JsonMapperUtils.readValue(file, JsonNode.class));
		assertJsonContent(JsonMapperUtils.readValue(file, new TypeReference<>() {
		}));
		assertJsonContent(JsonMapperUtils.readValue(file, javaType));
	}

	@Test
	void readValueURL() throws IOException {
		URL url = new ClassPathResource("input.json").getURL();
		assertJsonContent(JsonMapperUtils.readValue(url, JsonNode.class));
		assertJsonContent(JsonMapperUtils.readValue(url, new TypeReference<>() {
		}));
		assertJsonContent(JsonMapperUtils.readValue(url, javaType));
	}

	@Test
	void readValueStr() {
		assertJsonContent(JsonMapperUtils.readValue(json, JsonNode.class));
		assertJsonContent(JsonMapperUtils.readValue(json, new TypeReference<>() {
		}));
		assertJsonContent(JsonMapperUtils.readValue(json, javaType));
	}

	@Test
	void readValueReader() throws IOException {
		try (InputStream inputStream = new ClassPathResource("input.json").getInputStream();
				InputStreamReader reader = new InputStreamReader(inputStream)) {
			assertJsonContent(JsonMapperUtils.readValue(reader, JsonNode.class));
		}
		try (InputStream inputStream = new ClassPathResource("input.json").getInputStream();
				InputStreamReader reader = new InputStreamReader(inputStream)) {
			assertJsonContent(JsonMapperUtils.readValue(reader, new TypeReference<>() {
			}));
		}
		try (InputStream inputStream = new ClassPathResource("input.json").getInputStream();
				InputStreamReader reader = new InputStreamReader(inputStream)) {
			assertJsonContent(JsonMapperUtils.readValue(reader, javaType));
		}
	}

	@Test
	void readValueInputStream() throws IOException {
		try (InputStream inputStream = new ClassPathResource("input.json").getInputStream()) {
			assertJsonContent(JsonMapperUtils.readValue(inputStream, JsonNode.class));
		}
		try (InputStream inputStream = new ClassPathResource("input.json").getInputStream()) {
			assertJsonContent(JsonMapperUtils.readValue(inputStream, new TypeReference<>() {
			}));
		}
		try (InputStream inputStream = new ClassPathResource("input.json").getInputStream()) {
			assertJsonContent(JsonMapperUtils.readValue(inputStream, javaType));
		}
	}

	@Test
	void readValueByteArray() {
		byte[] jsonBytes = json.getBytes();
		assertJsonContent(JsonMapperUtils.readValue(jsonBytes, JsonNode.class));
		assertJsonContent(JsonMapperUtils.readValue(jsonBytes, new TypeReference<>() {
		}));
		assertJsonContent(JsonMapperUtils.readValue(jsonBytes, javaType));
	}

	@Test
	void readValueDataInput() throws IOException {
		try (InputStream inputStream = new ClassPathResource("input.json").getInputStream()) {
			DataInput dataInput = new DataInputStream(inputStream);
			assertJsonContent(JsonMapperUtils.readValue(dataInput, JsonNode.class));
		}
		try (InputStream inputStream = new ClassPathResource("input.json").getInputStream()) {
			DataInput dataInput = new DataInputStream(inputStream);
			assertJsonContent(JsonMapperUtils.readValue(dataInput, new TypeReference<>() {
			}));
		}
		try (InputStream inputStream = new ClassPathResource("input.json").getInputStream()) {
			DataInput dataInput = new DataInputStream(inputStream);
			assertJsonContent(JsonMapperUtils.readValue(dataInput, javaType));
		}
	}

	@Test
	void writeValueAsString() {
		String result = JsonMapperUtils.writeValueAsString(Map.of("username", "password"));
		String json = "{\"username\":\"password\"}";
		assertThat(result).isEqualTo(json);

		byte[] bytes = JsonMapperUtils.writeValueAsBytes(Map.of("username", "password"));
		assertThat(bytes).isEqualTo(json.getBytes());
	}

	@Test
	void readValueList() {
		@Language("json")
		String json = """
				[
				  {
				    "a": 1
				  },
				  {
				    "a": 1
				  }
				]""";
		List<JsonNode> result = JsonMapperUtils.readValueList(json, JsonNode.class);
		assertThat(result).isNotNull();
		assertThat(result.get(0).get("a").asInt()).isEqualTo(1);
		assertThat(result.get(1).get("a").asInt()).isEqualTo(1);
	}

	@SuppressWarnings("unchecked")
	@Test
	void readValueMap() throws IOException {
		Map<String, Object> result = JsonMapperUtils.readValueMap(json, String.class, Object.class);
		assertThat(result).isNotNull();
		assertThat(result).containsEntry("c", "1");
		assertThat(result).containsEntry("a", "2");
		assertThat((Map<String, Object>) result.get("b")).containsEntry("c", 3);

		Map<String, Object> result1 = JsonMapperUtils.readValueMap(new ClassPathResource("input.json").getInputStream(),
				String.class, Object.class);
		assertThat(result1).isNotNull();
		assertThat(result1).containsEntry("c", "1");
		assertThat(result1).containsEntry("a", "2");
		assertThat((Map<String, Object>) result1.get("b")).containsEntry("c", 3);
	}

	@Test
	void testReadTreeWithJsonParser() {
		JsonFactory jsonFactory = new JsonFactory();
		try (JsonParser parser = jsonFactory.createParser(ObjectReadContext.empty(), json)) {
			assertJsonContent(JsonMapperUtils.readTree(parser));
		}
	}

	@Test
	void testReadTreeWithFile() throws IOException {
		File file = new ClassPathResource("input.json").getFile();
		assertJsonContent(JsonMapperUtils.readTree(file));
	}

	@Test
	void testReadTreeWithURL() throws IOException {
		URL url = new ClassPathResource("input.json").getURL();
		assertJsonContent(JsonMapperUtils.readTree(url));
	}

	@Test
	void testReadTreeWithString() {
		assertJsonContent(JsonMapperUtils.readTree(json));
	}

	@Test
	void testReadTreeWithInputStreamReader() throws IOException {
		InputStream inputStream = new ClassPathResource("input.json").getInputStream();
		try (InputStreamReader reader = new InputStreamReader(inputStream)) {
			assertJsonContent(JsonMapperUtils.readTree(reader));
		}
	}

	@Test
	void testReadTreeWithInputStream() throws IOException {
		try (InputStream inputStream = new ClassPathResource("input.json").getInputStream()) {
			assertJsonContent(JsonMapperUtils.readTree(inputStream));
		}
	}

	@Test
	void testReadTreeWithByteArray() {
		assertJsonContent(JsonMapperUtils.readTree(json.getBytes()));
	}

	@Test
	void convertValueList() {
		Set<String> set = Set.of("username", "password");
		List<String> list = JsonMapperUtils.convertValueList(set, String.class);
		assertThat(list).containsExactlyElementsOf(set);
	}

	@Test
	void convertValueMap() {
		Pair<String, String> pair = Pair.of("username", "password");
		Map<String, String> map = JsonMapperUtils.convertValueMap(pair, String.class, String.class);
		assertThat(map).containsExactly(Map.entry(pair.key(), pair.value()));
	}

	@Test
	void convert() {
		@Language("json")
		String json = """
				{
				  "dependency": [
				    {
				      "groupId": "org.springframework.boot",
				      "artifactId": "spring-boot-starter-logging"
				    },
				    {
				      "groupId": "org.springframework.boot",
				      "artifactId": "spring-boot-starter-json"
				    }
				  ]
				}
				""";
		JsonNode jsonNode = JsonMapperUtils.readTree(json);
		JsonNode dependencyArray = jsonNode.get("dependency");

		Map<String, String> loggingDependency = Map.of("groupId", "org.springframework.boot", "artifactId",
				"spring-boot-starter-logging");
		Map<String, String> jsonDependency = Map.of("groupId", "org.springframework.boot", "artifactId",
				"spring-boot-starter-json");
		MapType mapType = TypeFactoryUtils.mapType(String.class, String.class);

		assertThat(JsonMapperUtils.<Map<String, String>>convertValue(dependencyArray.get(0), mapType))
			.containsExactlyInAnyOrderEntriesOf(loggingDependency);

		assertThat(JsonMapperUtils.<Map<String, String>>convertValue(dependencyArray.get(1), mapType))
			.containsExactlyInAnyOrderEntriesOf(jsonDependency);

		List<Map<String, String>> dependencyList = List.of(loggingDependency, jsonDependency);
		CollectionType collectionType = TypeFactoryUtils.listType(mapType);

		assertThat(JsonMapperUtils.<List<Map<String, String>>>convertValue(dependencyArray, collectionType))
			.containsExactlyElementsOf(dependencyList);

		JavaType javaType = TypeFactoryUtils.javaType(String.class);
		Map<String, List<Map<String, String>>> dependencyManagement = Map.of("dependency", dependencyList);
		MapType constructMapType = TypeFactoryUtils.mapType(javaType, collectionType);

		assertThat(JsonMapperUtils.<Map<String, List<Map<String, String>>>>convertValue(jsonNode, constructMapType))
			.containsExactlyEntriesOf(dependencyManagement);
	}

}
