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

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;
import com.google.common.collect.Streams;
import com.livk.commons.util.Pair;
import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

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

	@Test
	void readValueJsonParser() throws IOException {
		JsonFactory jsonFactory = new JsonFactory();
		try (JsonParser parser = jsonFactory.createParser(json)) {
			JsonNode parserJsonNode = JsonMapperUtils.readValue(parser, JsonNode.class);
			assertThat(parserJsonNode).isNotNull();
			assertThat(parserJsonNode.get("c").asText()).isEqualTo("1");
			assertThat(parserJsonNode.get("a").asText()).isEqualTo("2");
			assertThat(parserJsonNode.get("b").get("c").asInt()).isEqualTo(3);
		}

		try (JsonParser parser = jsonFactory.createParser(json)) {
			JsonNode parserJsonNode = JsonMapperUtils.readValue(parser, new TypeReference<>() {
			});
			assertThat(parserJsonNode).isNotNull();
			assertThat(parserJsonNode.get("c").asText()).isEqualTo("1");
			assertThat(parserJsonNode.get("a").asText()).isEqualTo("2");
			assertThat(parserJsonNode.get("b").get("c").asInt()).isEqualTo(3);
		}

		try (JsonParser parser = jsonFactory.createParser(json)) {
			JsonNode parserJsonNode = JsonMapperUtils.readValue(parser, javaType);
			assertThat(parserJsonNode).isNotNull();
			assertThat(parserJsonNode.get("c").asText()).isEqualTo("1");
			assertThat(parserJsonNode.get("a").asText()).isEqualTo("2");
			assertThat(parserJsonNode.get("b").get("c").asInt()).isEqualTo(3);
		}
	}

	@Test
	void readValueFile() throws IOException {
		File file = new ClassPathResource("input.json").getFile();

		JsonNode fileJsonNode = JsonMapperUtils.readValue(file, JsonNode.class);
		assertThat(fileJsonNode).isNotNull();
		assertThat(fileJsonNode.get("c").asText()).isEqualTo("1");
		assertThat(fileJsonNode.get("a").asText()).isEqualTo("2");
		assertThat(fileJsonNode.get("b").get("c").asInt()).isEqualTo(3);

		fileJsonNode = JsonMapperUtils.readValue(file, new TypeReference<>() {
		});
		assertThat(fileJsonNode).isNotNull();
		assertThat(fileJsonNode.get("c").asText()).isEqualTo("1");
		assertThat(fileJsonNode.get("a").asText()).isEqualTo("2");
		assertThat(fileJsonNode.get("b").get("c").asInt()).isEqualTo(3);

		fileJsonNode = JsonMapperUtils.readValue(file, javaType);
		assertThat(fileJsonNode).isNotNull();
		assertThat(fileJsonNode.get("c").asText()).isEqualTo("1");
		assertThat(fileJsonNode.get("a").asText()).isEqualTo("2");
		assertThat(fileJsonNode.get("b").get("c").asInt()).isEqualTo(3);

	}

	@Test
	void readValueURL() throws IOException {
		URL url = new ClassPathResource("input.json").getURL();

		JsonNode urlJsonNode = JsonMapperUtils.readValue(url, JsonNode.class);
		assertThat(urlJsonNode).isNotNull();
		assertThat(urlJsonNode.get("c").asText()).isEqualTo("1");
		assertThat(urlJsonNode.get("a").asText()).isEqualTo("2");
		assertThat(urlJsonNode.get("b").get("c").asInt()).isEqualTo(3);

		urlJsonNode = JsonMapperUtils.readValue(url, new TypeReference<>() {
		});
		assertThat(urlJsonNode).isNotNull();
		assertThat(urlJsonNode.get("c").asText()).isEqualTo("1");
		assertThat(urlJsonNode.get("a").asText()).isEqualTo("2");
		assertThat(urlJsonNode.get("b").get("c").asInt()).isEqualTo(3);

		urlJsonNode = JsonMapperUtils.readValue(url, javaType);
		assertThat(urlJsonNode).isNotNull();
		assertThat(urlJsonNode.get("c").asText()).isEqualTo("1");
		assertThat(urlJsonNode.get("a").asText()).isEqualTo("2");
		assertThat(urlJsonNode.get("b").get("c").asInt()).isEqualTo(3);

	}

	@Test
	void readValueStr() {
		JsonNode strJsonNode = JsonMapperUtils.readValue(json, JsonNode.class);
		assertThat(strJsonNode).isNotNull();
		assertThat(strJsonNode.get("c").asText()).isEqualTo("1");
		assertThat(strJsonNode.get("a").asText()).isEqualTo("2");
		assertThat(strJsonNode.get("b").get("c").asInt()).isEqualTo(3);

		JsonNode typeReferenceJsonNode = JsonMapperUtils.readValue(json, new TypeReference<>() {
		});
		assertThat(typeReferenceJsonNode).isNotNull();
		assertThat(typeReferenceJsonNode.get("c").asText()).isEqualTo("1");
		assertThat(typeReferenceJsonNode.get("a").asText()).isEqualTo("2");
		assertThat(typeReferenceJsonNode.get("b").get("c").asInt()).isEqualTo(3);

		JsonNode javaTypeJsonNode = JsonMapperUtils.readValue(json, javaType);
		assertThat(javaTypeJsonNode).isNotNull();
		assertThat(javaTypeJsonNode.get("c").asText()).isEqualTo("1");
		assertThat(javaTypeJsonNode.get("a").asText()).isEqualTo("2");
		assertThat(javaTypeJsonNode.get("b").get("c").asInt()).isEqualTo(3);

	}

	@Test
	void readValueReader() throws IOException {
		try (InputStream inputStream = new ClassPathResource("input.json").getInputStream();
				InputStreamReader reader = new InputStreamReader(inputStream)) {
			JsonNode readerJsonNode = JsonMapperUtils.readValue(reader, JsonNode.class);
			assertThat(readerJsonNode).isNotNull();
			assertThat(readerJsonNode.get("c").asText()).isEqualTo("1");
			assertThat(readerJsonNode.get("a").asText()).isEqualTo("2");
			assertThat(readerJsonNode.get("b").get("c").asInt()).isEqualTo(3);
		}

		try (InputStream inputStream = new ClassPathResource("input.json").getInputStream();
				InputStreamReader reader = new InputStreamReader(inputStream)) {
			JsonNode readerJsonNode = JsonMapperUtils.readValue(reader, new TypeReference<>() {
			});
			assertThat(readerJsonNode).isNotNull();
			assertThat(readerJsonNode.get("c").asText()).isEqualTo("1");
			assertThat(readerJsonNode.get("a").asText()).isEqualTo("2");
			assertThat(readerJsonNode.get("b").get("c").asInt()).isEqualTo(3);
		}

		try (InputStream inputStream = new ClassPathResource("input.json").getInputStream();
				InputStreamReader reader = new InputStreamReader(inputStream)) {
			JsonNode readerJsonNode = JsonMapperUtils.readValue(reader, javaType);
			assertThat(readerJsonNode).isNotNull();
			assertThat(readerJsonNode.get("c").asText()).isEqualTo("1");
			assertThat(readerJsonNode.get("a").asText()).isEqualTo("2");
			assertThat(readerJsonNode.get("b").get("c").asInt()).isEqualTo(3);
		}
	}

	@Test
	void readValueInputStream() throws IOException {
		try (InputStream inputStream = new ClassPathResource("input.json").getInputStream()) {
			JsonNode inputStreamJsonNode = JsonMapperUtils.readValue(inputStream, JsonNode.class);
			assertThat(inputStreamJsonNode).isNotNull();
			assertThat(inputStreamJsonNode.get("c").asText()).isEqualTo("1");
			assertThat(inputStreamJsonNode.get("a").asText()).isEqualTo("2");
			assertThat(inputStreamJsonNode.get("b").get("c").asInt()).isEqualTo(3);
		}

		try (InputStream inputStream = new ClassPathResource("input.json").getInputStream()) {
			JsonNode inputStreamJsonNode = JsonMapperUtils.readValue(inputStream, new TypeReference<>() {
			});
			assertThat(inputStreamJsonNode).isNotNull();
			assertThat(inputStreamJsonNode.get("c").asText()).isEqualTo("1");
			assertThat(inputStreamJsonNode.get("a").asText()).isEqualTo("2");
			assertThat(inputStreamJsonNode.get("b").get("c").asInt()).isEqualTo(3);
		}

		try (InputStream inputStream = new ClassPathResource("input.json").getInputStream()) {
			JsonNode inputStreamJsonNode = JsonMapperUtils.readValue(inputStream, javaType);
			assertThat(inputStreamJsonNode).isNotNull();
			assertThat(inputStreamJsonNode.get("c").asText()).isEqualTo("1");
			assertThat(inputStreamJsonNode.get("a").asText()).isEqualTo("2");
			assertThat(inputStreamJsonNode.get("b").get("c").asInt()).isEqualTo(3);
		}
	}

	@Test
	void readValueByteArray() {
		byte[] jsonBytes = json.getBytes();

		JsonNode byteArrayJsonNode = JsonMapperUtils.readValue(jsonBytes, JsonNode.class);
		assertThat(byteArrayJsonNode).isNotNull();
		assertThat(byteArrayJsonNode.get("c").asText()).isEqualTo("1");
		assertThat(byteArrayJsonNode.get("a").asText()).isEqualTo("2");
		assertThat(byteArrayJsonNode.get("b").get("c").asInt()).isEqualTo(3);

		byteArrayJsonNode = JsonMapperUtils.readValue(jsonBytes, new TypeReference<>() {
		});
		assertThat(byteArrayJsonNode).isNotNull();
		assertThat(byteArrayJsonNode.get("c").asText()).isEqualTo("1");
		assertThat(byteArrayJsonNode.get("a").asText()).isEqualTo("2");
		assertThat(byteArrayJsonNode.get("b").get("c").asInt()).isEqualTo(3);

		byteArrayJsonNode = JsonMapperUtils.readValue(jsonBytes, javaType);
		assertThat(byteArrayJsonNode).isNotNull();
		assertThat(byteArrayJsonNode.get("c").asText()).isEqualTo("1");
		assertThat(byteArrayJsonNode.get("a").asText()).isEqualTo("2");
		assertThat(byteArrayJsonNode.get("b").get("c").asInt()).isEqualTo(3);
	}

	@Test
	void readValueDataInput() throws IOException {
		try (InputStream inputStream = new ClassPathResource("input.json").getInputStream()) {
			DataInput dataInput = new DataInputStream(inputStream);
			JsonNode dataInputJsonNode = JsonMapperUtils.readValue(dataInput, JsonNode.class);
			assertThat(dataInputJsonNode).isNotNull();
			assertThat(dataInputJsonNode.get("c").asText()).isEqualTo("1");
			assertThat(dataInputJsonNode.get("a").asText()).isEqualTo("2");
			assertThat(dataInputJsonNode.get("b").get("c").asInt()).isEqualTo(3);
		}
		try (InputStream inputStream = new ClassPathResource("input.json").getInputStream()) {
			DataInput dataInput = new DataInputStream(inputStream);
			JsonNode jsonNode = JsonMapperUtils.readValue(dataInput, new TypeReference<JsonNode>() {
			});
			assertThat(jsonNode).isNotNull();
			assertThat(jsonNode.get("c").asText()).isEqualTo("1");
			assertThat(jsonNode.get("a").asText()).isEqualTo("2");
			assertThat(jsonNode.get("b").get("c").asInt()).isEqualTo(3);
		}
		try (InputStream inputStream = new ClassPathResource("input.json").getInputStream()) {
			DataInput dataInput = new DataInputStream(inputStream);
			JsonNode jsonNode = JsonMapperUtils.readValue(dataInput, javaType);
			assertThat(jsonNode).isNotNull();
			assertThat(jsonNode.get("c").asText()).isEqualTo("1");
			assertThat(jsonNode.get("a").asText()).isEqualTo("2");
			assertThat(jsonNode.get("b").get("c").asInt()).isEqualTo(3);
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
	void readTree() throws IOException {
		{
			JsonFactory jsonFactory = new JsonFactory();
			try (JsonParser parser = jsonFactory.createParser(json)) {
				JsonNode parserJsonNode = JsonMapperUtils.readTree(parser);
				assertThat(parserJsonNode).isNotNull();
				assertThat(parserJsonNode.get("c").asText()).isEqualTo("1");
				assertThat(parserJsonNode.get("a").asText()).isEqualTo("2");
				assertThat(parserJsonNode.get("b").get("c").asInt()).isEqualTo(3);
			}
		}
		{
			File file = new ClassPathResource("input.json").getFile();
			JsonNode fileJsonNode = JsonMapperUtils.readTree(file);
			assertThat(fileJsonNode).isNotNull();
			assertThat(fileJsonNode.get("c").asText()).isEqualTo("1");
			assertThat(fileJsonNode.get("a").asText()).isEqualTo("2");
			assertThat(fileJsonNode.get("b").get("c").asInt()).isEqualTo(3);
		}
		{
			URL url = new ClassPathResource("input.json").getURL();
			JsonNode urlJsonNode = JsonMapperUtils.readTree(url);
			assertThat(urlJsonNode).isNotNull();
			assertThat(urlJsonNode.get("c").asText()).isEqualTo("1");
			assertThat(urlJsonNode.get("a").asText()).isEqualTo("2");
			assertThat(urlJsonNode.get("b").get("c").asInt()).isEqualTo(3);
		}
		{
			JsonNode result = JsonMapperUtils.readTree(json);
			assertThat(result).isNotNull();
			assertThat(result.get("c").asText()).isEqualTo("1");
			assertThat(result.get("a").asText()).isEqualTo("2");
			assertThat(result.get("b").get("c").asInt()).isEqualTo(3);
		}
		{
			InputStream inputStream = new ClassPathResource("input.json").getInputStream();
			InputStreamReader reader = new InputStreamReader(inputStream);
			JsonNode readerJsonNode = JsonMapperUtils.readTree(reader);
			assertThat(readerJsonNode).isNotNull();
			assertThat(readerJsonNode.get("c").asText()).isEqualTo("1");
			assertThat(readerJsonNode.get("a").asText()).isEqualTo("2");
			assertThat(readerJsonNode.get("b").get("c").asInt()).isEqualTo(3);
		}
		{
			InputStream inputStream = new ClassPathResource("input.json").getInputStream();
			JsonNode inputStreamJsonNode = JsonMapperUtils.readTree(inputStream);
			assertThat(inputStreamJsonNode).isNotNull();
			assertThat(inputStreamJsonNode.get("c").asText()).isEqualTo("1");
			assertThat(inputStreamJsonNode.get("a").asText()).isEqualTo("2");
			assertThat(inputStreamJsonNode.get("b").get("c").asInt()).isEqualTo(3);
		}
		{
			JsonNode byteArrayJsonNode = JsonMapperUtils.readTree(json.getBytes());
			assertThat(byteArrayJsonNode).isNotNull();
			assertThat(byteArrayJsonNode.get("c").asText()).isEqualTo("1");
			assertThat(byteArrayJsonNode.get("a").asText()).isEqualTo("2");
			assertThat(byteArrayJsonNode.get("b").get("c").asInt()).isEqualTo(3);
		}
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
		List<JsonNode> jsonNodeList = Streams.stream(dependencyArray.elements()).toList();

		assertThat(JsonMapperUtils.<Map<String, String>>convertValue(jsonNodeList.get(0), mapType))
			.containsExactlyInAnyOrderEntriesOf(loggingDependency);

		assertThat(JsonMapperUtils.<Map<String, String>>convertValue(jsonNodeList.get(1), mapType))
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
