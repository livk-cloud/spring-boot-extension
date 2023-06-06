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

package com.livk.commons.jackson.support;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.livk.commons.bean.domain.Pair;
import com.livk.commons.collect.util.StreamUtils;
import com.livk.commons.jackson.core.JacksonSupport;
import com.livk.commons.jackson.util.TypeFactoryUtils;
import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.Test;
import org.springframework.core.ResolvableType;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author livk
 */
class JacksonSupportTest {

	static final JacksonSupport<JsonMapper> JSON = JacksonSupport.create(MapperFactory.JSON);

	static final JacksonSupport<YAMLMapper> YAML = JacksonSupport.create(MapperFactory.YAML);

	static final JacksonSupport<XmlMapper> XML = JacksonSupport.create(MapperFactory.XML);

	@Language("json")
	static String json = """
		{
		                    "c": "1",
		                    "a": "2",
		                    "b": {
		                        "c": 3
		                    }
		                }""";

	@Language("yaml")
	static String yaml = """
		c: 1
		a: 2
		b:
		  c: 3
		""";

	@Language("xml")
	static String xml = """
		<pro>
		    <c>1</c>
		    <a>2</a>
		    <b>
		        <c>3</c>
		    </b>
		</pro>
		            """;

	@Test
	void javaType() {
		assertEquals(String.class, TypeFactoryUtils.javaType(String.class).getRawClass());
		assertEquals(Integer.class, TypeFactoryUtils.javaType(Integer.class).getRawClass());
		assertEquals(Long.class, TypeFactoryUtils.javaType(Long.class).getRawClass());

		JavaType javaType = TypeFactoryUtils.javaType(Pair.class, String.class, Integer.class);
		assertEquals(Pair.class, javaType.getRawClass());
		assertEquals(String.class, javaType.getBindings().getBoundType(0).getRawClass());
		assertEquals(Integer.class, javaType.getBindings().getBoundType(1).getRawClass());

		ResolvableType resolvableType = ResolvableType.forClassWithGenerics(Pair.class, String.class, Integer.class);
		JavaType type = TypeFactoryUtils.javaType(resolvableType);
		assertEquals(Pair.class, type.getRawClass());
		assertEquals(String.class, type.getBindings().getBoundType(0).getRawClass());
		assertEquals(Integer.class, type.getBindings().getBoundType(1).getRawClass());

		assertEquals(String.class, TypeFactoryUtils.collectionType(String.class).getBindings().getBoundType(0).getRawClass());
		assertEquals(Integer.class, TypeFactoryUtils.collectionType(Integer.class).getBindings().getBoundType(0).getRawClass());
		assertEquals(Long.class, TypeFactoryUtils.collectionType(Long.class).getBindings().getBoundType(0).getRawClass());

		assertEquals(List.of(TypeFactoryUtils.javaType(String.class), TypeFactoryUtils.javaType(String.class)),
			TypeFactoryUtils.mapType(String.class, String.class).getBindings().getTypeParameters());
		assertEquals(List.of(TypeFactoryUtils.javaType(String.class), TypeFactoryUtils.javaType(Integer.class)),
			TypeFactoryUtils.mapType(String.class, Integer.class).getBindings().getTypeParameters());
		assertEquals(List.of(TypeFactoryUtils.javaType(Integer.class), TypeFactoryUtils.javaType(String.class)),
			TypeFactoryUtils.mapType(Integer.class, String.class).getBindings().getTypeParameters());
		assertEquals(List.of(TypeFactoryUtils.javaType(Integer.class), TypeFactoryUtils.javaType(Integer.class)),
			TypeFactoryUtils.mapType(Integer.class, Integer.class).getBindings().getTypeParameters());
	}

	@Test
	void testJsonReadValue() throws IOException {

		JsonNode result1 = JSON.readValue(json, JsonNode.class);
		assertNotNull(result1);
		assertEquals("1", result1.get("c").asText());
		assertEquals("2", result1.get("a").asText());
		assertEquals(3, result1.get("b").get("c").asInt());

		InputStream inputStream = new ClassPathResource("input.json").getInputStream();
		JsonNode result2 = JSON.readValue(inputStream, JsonNode.class);
		assertNotNull(result2);
		assertEquals("1", result2.get("c").asText());
		assertEquals("2", result2.get("a").asText());
		assertEquals(3, result2.get("b").get("c").asInt());

		JsonNode result3 = JSON.readValue(json, new TypeReference<>() {
		});
		assertNotNull(result3);
		assertEquals("1", result3.get("c").asText());
		assertEquals("2", result3.get("a").asText());
		assertEquals(3, result3.get("b").get("c").asInt());

		JavaType javaType = TypeFactoryUtils.javaType(JsonNode.class);
		JsonNode result4 = JSON.readValue(json, javaType);
		assertNotNull(result3);
		assertEquals("1", result4.get("c").asText());
		assertEquals("2", result4.get("a").asText());
		assertEquals(3, result4.get("b").get("c").asInt());
	}

	@Test
	void testYamlReadValue() throws IOException {

		JsonNode result1 = YAML.readValue(yaml, JsonNode.class);
		assertNotNull(result1);
		assertEquals("1", result1.get("c").asText());
		assertEquals("2", result1.get("a").asText());
		assertEquals(3, result1.get("b").get("c").asInt());

		InputStream inputStream = new ClassPathResource("input.yml").getInputStream();
		JsonNode result2 = YAML.readValue(inputStream, JsonNode.class);
		assertNotNull(result2);
		assertEquals("1", result2.get("c").asText());
		assertEquals("2", result2.get("a").asText());
		assertEquals(3, result2.get("b").get("c").asInt());

		JsonNode result3 = YAML.readValue(yaml, new TypeReference<>() {
		});
		assertNotNull(result3);
		assertEquals("1", result3.get("c").asText());
		assertEquals("2", result3.get("a").asText());
		assertEquals(3, result3.get("b").get("c").asInt());

		JavaType javaType = TypeFactoryUtils.javaType(JsonNode.class);
		JsonNode result4 = YAML.readValue(yaml, javaType);
		assertNotNull(result3);
		assertEquals("1", result4.get("c").asText());
		assertEquals("2", result4.get("a").asText());
		assertEquals(3, result4.get("b").get("c").asInt());
	}

	@Test
	void testXmlReadValue() throws IOException {

		JsonNode result1 = XML.readValue(xml, JsonNode.class);
		assertNotNull(result1);
		assertEquals("1", result1.get("c").asText());
		assertEquals("2", result1.get("a").asText());
		assertEquals(3, result1.get("b").get("c").asInt());

		InputStream inputStream = new ClassPathResource("input.xml").getInputStream();
		JsonNode result2 = XML.readValue(inputStream, JsonNode.class);
		assertNotNull(result2);
		assertEquals("1", result2.get("c").asText());
		assertEquals("2", result2.get("a").asText());
		assertEquals(3, result2.get("b").get("c").asInt());

		JsonNode result3 = XML.readValue(xml, new TypeReference<JsonNode>() {
		});
		assertNotNull(result3);
		assertEquals("1", result3.get("c").asText());
		assertEquals("2", result3.get("a").asText());
		assertEquals(3, result3.get("b").get("c").asInt());

		JavaType javaType = TypeFactoryUtils.javaType(JsonNode.class);
		JsonNode result4 = XML.readValue(xml, javaType);
		assertNotNull(result3);
		assertEquals("1", result4.get("c").asText());
		assertEquals("2", result4.get("a").asText());
		assertEquals(3, result4.get("b").get("c").asInt());
	}

	@Test
	void testToJsonStr() {
		String result = JSON.writeValueAsString(Map.of("username", "password"));
		@Language("json") String json = "{\"username\":\"password\"}";
		assertEquals(json, result);

		byte[] bytes = JSON.writeValueAsBytes(Map.of("username", "password"));
		assertArrayEquals(json.getBytes(), bytes);
	}

	@Test
	void testToYamlStr() {
		String result = YAML.writeValueAsString(Map.of("username", "password"));
		@Language("yml") String yml = """
			---
			username: "password"
			""";
		assertEquals(yml, result);

		byte[] bytes = YAML.writeValueAsBytes(Map.of("username", "password"));
		assertArrayEquals(yml.getBytes(), bytes);
	}

	@Test
	void testToXmlStr() {
		String result = XML.writeValueAsString(Map.of("username", "password"));
		@Language("xml") String xml = "<Map1><username>password</username></Map1>";
		assertEquals(xml, result);

		byte[] bytes = XML.writeValueAsBytes(Map.of("username", "password"));
		assertArrayEquals(xml.getBytes(), bytes);
	}

	@Test
	void testReadTree() {
		JsonNode jsonNode = JSON.readTree(json);
		assertNotNull(jsonNode);
		assertEquals("1", jsonNode.get("c").asText());
		assertEquals("2", jsonNode.get("a").asText());
		assertEquals(3, jsonNode.get("b").get("c").asInt());

		JsonNode yamlNode = YAML.readTree(yaml);
		assertNotNull(yamlNode);
		assertEquals("1", yamlNode.get("c").asText());
		assertEquals("2", yamlNode.get("a").asText());
		assertEquals(3, yamlNode.get("b").get("c").asInt());

		JsonNode xmlNode = XML.readTree(xml);
		assertNotNull(xmlNode);
		assertEquals("1", xmlNode.get("c").asText());
		assertEquals("2", xmlNode.get("a").asText());
		assertEquals(3, xmlNode.get("b").get("c").asInt());
	}

	@Test
	void convertJson() {
		@Language("json") String json = """
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
		JsonNode jsonNode = JSON.readTree(json);
		JsonNode dependencyArray = jsonNode.get("dependency");

		Map<String, String> loggingDependency = Map.of("groupId", "org.springframework.boot", "artifactId", "spring-boot-starter-logging");
		Map<String, String> jsonDependency = Map.of("groupId", "org.springframework.boot", "artifactId", "spring-boot-starter-json");
		MapType mapType = TypeFactoryUtils.mapType(String.class, String.class);
		List<JsonNode> jsonNodeList = StreamUtils.convert(dependencyArray.elements()).toList();
		assertEquals(loggingDependency, JSON.convertValue(jsonNodeList.get(0), mapType));
		assertEquals(jsonDependency, JSON.convertValue(jsonNodeList.get(1), mapType));

		List<Map<String, String>> dependencyList = List.of(loggingDependency, jsonDependency);
		CollectionType collectionType = TypeFactoryUtils.collectionType(mapType);
		assertEquals(dependencyList, JSON.convertValue(dependencyArray, collectionType));

		JavaType javaType = TypeFactoryUtils.javaType(String.class);
		Map<String, List<Map<String, String>>> dependencyManagement = Map.of("dependency", dependencyList);
		MapType constructMapType = TypeFactoryUtils.mapType(javaType, collectionType);
		assertEquals(dependencyManagement, JSON.convertValue(jsonNode, constructMapType));
	}

	@Test
	void convertYaml() {
		@Language("yml") String yml = """
			---
			dependency:
			  - groupId: org.springframework.boot
			    artifactId: spring-boot-starter-logging
			  - groupId: org.springframework.boot
			    artifactId: spring-boot-starter-json
			                                                """;
		JsonNode jsonNode = YAML.readTree(yml);
		JsonNode dependencyArray = jsonNode.get("dependency");

		Map<String, String> loggingDependency = Map.of("groupId", "org.springframework.boot", "artifactId", "spring-boot-starter-logging");
		Map<String, String> jsonDependency = Map.of("groupId", "org.springframework.boot", "artifactId", "spring-boot-starter-json");
		MapType mapType = TypeFactoryUtils.mapType(String.class, String.class);
		List<JsonNode> jsonNodeList = StreamUtils.convert(dependencyArray.elements()).toList();
		assertEquals(loggingDependency, YAML.convertValue(jsonNodeList.get(0), mapType));
		assertEquals(jsonDependency, YAML.convertValue(jsonNodeList.get(1), mapType));

		List<Map<String, String>> dependencyList = List.of(loggingDependency, jsonDependency);
		CollectionType collectionType = TypeFactoryUtils.collectionType(mapType);
		assertEquals(dependencyList, YAML.convertValue(dependencyArray, collectionType));

		JavaType javaType = TypeFactoryUtils.javaType(String.class);
		Map<String, List<Map<String, String>>> dependencyManagement = Map.of("dependency", dependencyList);
		MapType constructMapType = TypeFactoryUtils.mapType(javaType, collectionType);
		assertEquals(dependencyManagement, YAML.convertValue(jsonNode, constructMapType));
	}

	@Test
	void convertXml() {
		@Language("xml") String xml = """
			<pro>
			    <dependency>
			        <groupId>org.springframework.boot</groupId>
			        <artifactId>spring-boot-starter-logging</artifactId>
			    </dependency>
			    <dependency>
			        <groupId>org.springframework.boot</groupId>
			        <artifactId>spring-boot-starter-json</artifactId>
			    </dependency>
			</pro>
			                                               """;
		JsonNode jsonNode = XML.readTree(xml);
		JsonNode dependencyArray = jsonNode.get("dependency");

		Map<String, String> loggingDependency = Map.of("groupId", "org.springframework.boot", "artifactId", "spring-boot-starter-logging");
		Map<String, String> jsonDependency = Map.of("groupId", "org.springframework.boot", "artifactId", "spring-boot-starter-json");
		MapType mapType = TypeFactoryUtils.mapType(String.class, String.class);
		List<JsonNode> jsonNodeList = StreamUtils.convert(dependencyArray.elements()).toList();
		assertEquals(loggingDependency, XML.convertValue(jsonNodeList.get(0), mapType));
		assertEquals(jsonDependency, XML.convertValue(jsonNodeList.get(1), mapType));

		List<Map<String, String>> dependencyList = List.of(loggingDependency, jsonDependency);
		CollectionType collectionType = TypeFactoryUtils.collectionType(mapType);
		assertEquals(dependencyList, XML.convertValue(dependencyArray, collectionType));

		JavaType javaType = TypeFactoryUtils.javaType(String.class);
		Map<String, List<Map<String, String>>> dependencyManagement = Map.of("dependency", dependencyList);
		MapType constructMapType = TypeFactoryUtils.mapType(javaType, collectionType);
		assertEquals(dependencyManagement, XML.convertValue(jsonNode, constructMapType));
	}
}
