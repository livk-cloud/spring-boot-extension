/*
 * Copyright 2021-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * you may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.livk.commons.jackson;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;
import com.google.common.collect.Lists;
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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * <p>
 * JsonMapperUtilsTest
 * </p>
 *
 * @author livk
 */
class JsonMapperUtilsTest {

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
			assertNotNull(parserJsonNode);
			assertEquals("1", parserJsonNode.get("c").asText());
			assertEquals("2", parserJsonNode.get("a").asText());
			assertEquals(3, parserJsonNode.get("b").get("c").asInt());
		}

		try (JsonParser parser = jsonFactory.createParser(json)) {
			JsonNode parserJsonNode = JsonMapperUtils.readValue(parser, new TypeReference<>() {
			});
			assertNotNull(parserJsonNode);
			assertEquals("1", parserJsonNode.get("c").asText());
			assertEquals("2", parserJsonNode.get("a").asText());
			assertEquals(3, parserJsonNode.get("b").get("c").asInt());
		}

		try (JsonParser parser = jsonFactory.createParser(json)) {
			JsonNode parserJsonNode = JsonMapperUtils.readValue(parser, javaType);
			assertNotNull(parserJsonNode);
			assertEquals("1", parserJsonNode.get("c").asText());
			assertEquals("2", parserJsonNode.get("a").asText());
			assertEquals(3, parserJsonNode.get("b").get("c").asInt());
		}
	}

	@Test
	void readValueFile() throws IOException {
		File file = new ClassPathResource("input.json").getFile();
		JsonNode fileJsonNode = JsonMapperUtils.readValue(file, JsonNode.class);
		assertNotNull(fileJsonNode);
		assertEquals("1", fileJsonNode.get("c").asText());
		assertEquals("2", fileJsonNode.get("a").asText());
		assertEquals(3, fileJsonNode.get("b").get("c").asInt());

		fileJsonNode = JsonMapperUtils.readValue(file, new TypeReference<>() {
		});
		assertNotNull(fileJsonNode);
		assertEquals("1", fileJsonNode.get("c").asText());
		assertEquals("2", fileJsonNode.get("a").asText());
		assertEquals(3, fileJsonNode.get("b").get("c").asInt());

		fileJsonNode = JsonMapperUtils.readValue(file, javaType);
		assertNotNull(fileJsonNode);
		assertEquals("1", fileJsonNode.get("c").asText());
		assertEquals("2", fileJsonNode.get("a").asText());
		assertEquals(3, fileJsonNode.get("b").get("c").asInt());
	}

	@Test
	void readValueURL() throws IOException {
		URL url = new ClassPathResource("input.json").getURL();
		JsonNode urlJsonNode = JsonMapperUtils.readValue(url, JsonNode.class);
		assertNotNull(urlJsonNode);
		assertEquals("1", urlJsonNode.get("c").asText());
		assertEquals("2", urlJsonNode.get("a").asText());
		assertEquals(3, urlJsonNode.get("b").get("c").asInt());

		urlJsonNode = JsonMapperUtils.readValue(url, new TypeReference<>() {
		});
		assertNotNull(urlJsonNode);
		assertEquals("1", urlJsonNode.get("c").asText());
		assertEquals("2", urlJsonNode.get("a").asText());
		assertEquals(3, urlJsonNode.get("b").get("c").asInt());

		urlJsonNode = JsonMapperUtils.readValue(url, javaType);
		assertNotNull(urlJsonNode);
		assertEquals("1", urlJsonNode.get("c").asText());
		assertEquals("2", urlJsonNode.get("a").asText());
		assertEquals(3, urlJsonNode.get("b").get("c").asInt());
	}

	@Test
	void readValueStr() {
		JsonNode strJsonNode = JsonMapperUtils.readValue(json, JsonNode.class);
		assertNotNull(strJsonNode);
		assertEquals("1", strJsonNode.get("c").asText());
		assertEquals("2", strJsonNode.get("a").asText());
		assertEquals(3, strJsonNode.get("b").get("c").asInt());

		JsonNode typeReferenceJsonNode = JsonMapperUtils.readValue(json, new TypeReference<>() {
		});
		assertNotNull(typeReferenceJsonNode);
		assertEquals("1", typeReferenceJsonNode.get("c").asText());
		assertEquals("2", typeReferenceJsonNode.get("a").asText());
		assertEquals(3, typeReferenceJsonNode.get("b").get("c").asInt());

		JsonNode javaTypeJsonNode = JsonMapperUtils.readValue(json, javaType);
		assertNotNull(javaTypeJsonNode);
		assertEquals("1", javaTypeJsonNode.get("c").asText());
		assertEquals("2", javaTypeJsonNode.get("a").asText());
		assertEquals(3, javaTypeJsonNode.get("b").get("c").asInt());
	}

	@Test
	void readValueReader() throws IOException {
		{
			InputStream inputStream = new ClassPathResource("input.json").getInputStream();
			InputStreamReader reader = new InputStreamReader(inputStream);
			JsonNode readerJsonNode = JsonMapperUtils.readValue(reader, JsonNode.class);
			assertNotNull(readerJsonNode);
			assertEquals("1", readerJsonNode.get("c").asText());
			assertEquals("2", readerJsonNode.get("a").asText());
			assertEquals(3, readerJsonNode.get("b").get("c").asInt());
		}
		{
			InputStream inputStream = new ClassPathResource("input.json").getInputStream();
			InputStreamReader reader = new InputStreamReader(inputStream);
			JsonNode readerJsonNode = JsonMapperUtils.readValue(reader, new TypeReference<>() {
			});
			assertNotNull(readerJsonNode);
			assertEquals("1", readerJsonNode.get("c").asText());
			assertEquals("2", readerJsonNode.get("a").asText());
			assertEquals(3, readerJsonNode.get("b").get("c").asInt());
		}
		{
			InputStream inputStream = new ClassPathResource("input.json").getInputStream();
			InputStreamReader reader = new InputStreamReader(inputStream);
			JsonNode readerJsonNode = JsonMapperUtils.readValue(reader, javaType);
			assertNotNull(readerJsonNode);
			assertEquals("1", readerJsonNode.get("c").asText());
			assertEquals("2", readerJsonNode.get("a").asText());
			assertEquals(3, readerJsonNode.get("b").get("c").asInt());
		}
	}

	@Test
	void readValueInputStream() throws IOException {
		{
			InputStream inputStream = new ClassPathResource("input.json").getInputStream();
			JsonNode inputStreamJsonNode = JsonMapperUtils.readValue(inputStream, JsonNode.class);
			assertNotNull(inputStreamJsonNode);
			assertEquals("1", inputStreamJsonNode.get("c").asText());
			assertEquals("2", inputStreamJsonNode.get("a").asText());
			assertEquals(3, inputStreamJsonNode.get("b").get("c").asInt());
		}
		{
			InputStream inputStream = new ClassPathResource("input.json").getInputStream();
			JsonNode inputStreamJsonNode = JsonMapperUtils.readValue(inputStream, new TypeReference<>() {
			});
			assertNotNull(inputStreamJsonNode);
			assertEquals("1", inputStreamJsonNode.get("c").asText());
			assertEquals("2", inputStreamJsonNode.get("a").asText());
			assertEquals(3, inputStreamJsonNode.get("b").get("c").asInt());
		}
		{
			InputStream inputStream = new ClassPathResource("input.json").getInputStream();
			JsonNode inputStreamJsonNode = JsonMapperUtils.readValue(inputStream, javaType);
			assertNotNull(inputStreamJsonNode);
			assertEquals("1", inputStreamJsonNode.get("c").asText());
			assertEquals("2", inputStreamJsonNode.get("a").asText());
			assertEquals(3, inputStreamJsonNode.get("b").get("c").asInt());
		}
	}

	@Test
	void readValueByteArray() {
		JsonNode byteArrayJsonNode = JsonMapperUtils.readValue(json.getBytes(), JsonNode.class);
		assertNotNull(byteArrayJsonNode);
		assertEquals("1", byteArrayJsonNode.get("c").asText());
		assertEquals("2", byteArrayJsonNode.get("a").asText());
		assertEquals(3, byteArrayJsonNode.get("b").get("c").asInt());

		byteArrayJsonNode = JsonMapperUtils.readValue(json.getBytes(), new TypeReference<>() {
		});
		assertNotNull(byteArrayJsonNode);
		assertEquals("1", byteArrayJsonNode.get("c").asText());
		assertEquals("2", byteArrayJsonNode.get("a").asText());
		assertEquals(3, byteArrayJsonNode.get("b").get("c").asInt());

		byteArrayJsonNode = JsonMapperUtils.readValue(json.getBytes(), javaType);
		assertNotNull(byteArrayJsonNode);
		assertEquals("1", byteArrayJsonNode.get("c").asText());
		assertEquals("2", byteArrayJsonNode.get("a").asText());
		assertEquals(3, byteArrayJsonNode.get("b").get("c").asInt());
	}

	@Test
	void readValueDataInput() throws IOException {
		{
			InputStream inputStream = new ClassPathResource("input.json").getInputStream();
			DataInput dataInput = new DataInputStream(inputStream);
			JsonNode dataInputJsonNode = JsonMapperUtils.readValue(dataInput, JsonNode.class);
			assertNotNull(dataInputJsonNode);
			assertEquals("1", dataInputJsonNode.get("c").asText());
			assertEquals("2", dataInputJsonNode.get("a").asText());
			assertEquals(3, dataInputJsonNode.get("b").get("c").asInt());
		}
		{
			InputStream inputStream = new ClassPathResource("input.json").getInputStream();
			DataInput dataInput = new DataInputStream(inputStream);
			JsonNode dataInputJsonNode = JsonMapperUtils.readValue(dataInput, new TypeReference<>() {
			});
			assertNotNull(dataInputJsonNode);
			assertEquals("1", dataInputJsonNode.get("c").asText());
			assertEquals("2", dataInputJsonNode.get("a").asText());
			assertEquals(3, dataInputJsonNode.get("b").get("c").asInt());
		}
		{
			InputStream inputStream = new ClassPathResource("input.json").getInputStream();
			DataInput dataInput = new DataInputStream(inputStream);
			JsonNode dataInputJsonNode = JsonMapperUtils.readValue(dataInput, javaType);
			assertNotNull(dataInputJsonNode);
			assertEquals("1", dataInputJsonNode.get("c").asText());
			assertEquals("2", dataInputJsonNode.get("a").asText());
			assertEquals(3, dataInputJsonNode.get("b").get("c").asInt());
		}
	}

	@Test
	void writeValueAsString() {
		String result = JsonMapperUtils.writeValueAsString(Map.of("username", "password"));
		String json = "{\"username\":\"password\"}";
		assertEquals(json, result);

		byte[] bytes = JsonMapperUtils.writeValueAsBytes(Map.of("username", "password"));
		assertArrayEquals(json.getBytes(), bytes);
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
		assertNotNull(result);
		assertEquals(1, result.get(0).get("a").asInt());
		assertEquals(1, result.get(1).get("a").asInt());
	}

	@SuppressWarnings("unchecked")
	@Test
	void readValueMap() throws IOException {
		Map<String, Object> result = JsonMapperUtils.readValueMap(json, String.class, Object.class);
		assertNotNull(result);
		assertEquals("1", result.get("c"));
		assertEquals("2", result.get("a"));
		assertEquals(3, ((Map<String, Object>) result.get("b")).get("c"));

		Map<String, Object> result1 = JsonMapperUtils.readValueMap(new ClassPathResource("input.json").getInputStream(),
				String.class, Object.class);
		assertNotNull(result1);
		assertEquals("1", result1.get("c"));
		assertEquals("2", result1.get("a"));
		assertEquals(3, ((Map<String, Object>) result1.get("b")).get("c"));
	}

	@Test
	void readTree() throws IOException {
		{
			JsonFactory jsonFactory = new JsonFactory();
			try (JsonParser parser = jsonFactory.createParser(json)) {
				JsonNode parserJsonNode = JsonMapperUtils.readTree(parser);
				assertNotNull(parserJsonNode);
				assertEquals("1", parserJsonNode.get("c").asText());
				assertEquals("2", parserJsonNode.get("a").asText());
				assertEquals(3, parserJsonNode.get("b").get("c").asInt());
			}
		}

		{
			File file = new ClassPathResource("input.json").getFile();
			JsonNode fileJsonNode = JsonMapperUtils.readTree(file);
			assertNotNull(fileJsonNode);
			assertEquals("1", fileJsonNode.get("c").asText());
			assertEquals("2", fileJsonNode.get("a").asText());
			assertEquals(3, fileJsonNode.get("b").get("c").asInt());
		}

		{
			URL url = new ClassPathResource("input.json").getURL();
			JsonNode urlJsonNode = JsonMapperUtils.readTree(url);
			assertNotNull(urlJsonNode);
			assertEquals("1", urlJsonNode.get("c").asText());
			assertEquals("2", urlJsonNode.get("a").asText());
			assertEquals(3, urlJsonNode.get("b").get("c").asInt());
		}

		{
			JsonNode result = JsonMapperUtils.readTree(json);
			assertNotNull(result);
			assertEquals("1", result.get("c").asText());
			assertEquals("2", result.get("a").asText());
			assertEquals(3, result.get("b").get("c").asInt());
		}

		{
			InputStream inputStream = new ClassPathResource("input.json").getInputStream();
			InputStreamReader reader = new InputStreamReader(inputStream);
			JsonNode readerJsonNode = JsonMapperUtils.readTree(reader);
			assertNotNull(readerJsonNode);
			assertEquals("1", readerJsonNode.get("c").asText());
			assertEquals("2", readerJsonNode.get("a").asText());
			assertEquals(3, readerJsonNode.get("b").get("c").asInt());
		}

		{
			InputStream inputStream = new ClassPathResource("input.json").getInputStream();
			JsonNode inputStreamJsonNode = JsonMapperUtils.readTree(inputStream);
			assertNotNull(inputStreamJsonNode);
			assertEquals("1", inputStreamJsonNode.get("c").asText());
			assertEquals("2", inputStreamJsonNode.get("a").asText());
			assertEquals(3, inputStreamJsonNode.get("b").get("c").asInt());
		}

		{

			JsonNode byteArrayJsonNode = JsonMapperUtils.readTree(json.getBytes());
			assertNotNull(byteArrayJsonNode);
			assertEquals("1", byteArrayJsonNode.get("c").asText());
			assertEquals("2", byteArrayJsonNode.get("a").asText());
			assertEquals(3, byteArrayJsonNode.get("b").get("c").asInt());
		}
	}

	@Test
	void convertValueList() {
		Set<String> set = Set.of("username", "password");
		List<String> list = JsonMapperUtils.convertValueList(set, String.class);
		assertEquals(Lists.newArrayList(set), list);
	}

	@Test
	void convertValueMap() {
		Pair<String, String> pair = Pair.of("username", "password");
		Map<String, String> map = JsonMapperUtils.convertValueMap(pair, String.class, String.class);
		assertEquals(Map.of(pair.key(), pair.value()), map);
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
		assertEquals(loggingDependency, JsonMapperUtils.convertValue(jsonNodeList.get(0), mapType));
		assertEquals(jsonDependency, JsonMapperUtils.convertValue(jsonNodeList.get(1), mapType));

		List<Map<String, String>> dependencyList = List.of(loggingDependency, jsonDependency);
		CollectionType collectionType = TypeFactoryUtils.listType(mapType);
		assertEquals(dependencyList, JsonMapperUtils.convertValue(dependencyArray, collectionType));

		JavaType javaType = TypeFactoryUtils.javaType(String.class);
		Map<String, List<Map<String, String>>> dependencyManagement = Map.of("dependency", dependencyList);
		MapType constructMapType = TypeFactoryUtils.mapType(javaType, collectionType);
		assertEquals(dependencyManagement, JsonMapperUtils.convertValue(jsonNode, constructMapType));
	}

}
