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

package com.livk.commons.jackson.support;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.google.common.collect.Streams;
import com.livk.commons.jackson.TypeFactoryUtils;
import com.livk.commons.util.Pair;
import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.Test;
import org.springframework.core.ResolvableType;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
class JacksonSupportTests {

	static final JacksonSupport JSON = new JacksonSupport(new JsonMapper());

	static final JacksonSupport YAML = new JacksonSupport(new YAMLMapper());

	static final JacksonSupport XML = new JacksonSupport(new XmlMapper());

	@Language("json")
	static final String json = """
			{
			                    "c": "1",
			                    "a": "2",
			                    "b": {
			                        "c": 3
			                    }
			                }""";

	@Language("yaml")
	static final String yaml = """
			c: 1
			a: 2
			b:
			  c: 3
			""";

	@Language("xml")
	static final String xml = """
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
		assertThat(TypeFactoryUtils.javaType(String.class).getRawClass()).isEqualTo(String.class);
		assertThat(TypeFactoryUtils.javaType(Integer.class).getRawClass()).isEqualTo(Integer.class);
		assertThat(TypeFactoryUtils.javaType(Long.class).getRawClass()).isEqualTo(Long.class);

		JavaType javaType = TypeFactoryUtils.javaType(Pair.class, String.class, Integer.class);
		assertThat(javaType.getRawClass()).isEqualTo(Pair.class);
		assertThat(javaType.getBindings().getBoundType(0).getRawClass()).isEqualTo(String.class);
		assertThat(javaType.getBindings().getBoundType(1).getRawClass()).isEqualTo(Integer.class);

		ResolvableType resolvableType = ResolvableType.forClassWithGenerics(Pair.class, String.class, Integer.class);
		JavaType type = TypeFactoryUtils.javaType(resolvableType);
		assertThat(type.getRawClass()).isEqualTo(Pair.class);
		assertThat(type.getBindings().getBoundType(0).getRawClass()).isEqualTo(String.class);
		assertThat(type.getBindings().getBoundType(1).getRawClass()).isEqualTo(Integer.class);

		assertThat(TypeFactoryUtils.listType(String.class).getBindings().getBoundType(0).getRawClass())
			.isEqualTo(String.class);
		assertThat(TypeFactoryUtils.listType(Integer.class).getBindings().getBoundType(0).getRawClass())
			.isEqualTo(Integer.class);
		assertThat(TypeFactoryUtils.setType(Long.class).getBindings().getBoundType(0).getRawClass())
			.isEqualTo(Long.class);

		assertThat(TypeFactoryUtils.mapType(String.class, String.class).getBindings().getTypeParameters())
			.isEqualTo(List.of(TypeFactoryUtils.javaType(String.class), TypeFactoryUtils.javaType(String.class)));
		assertThat(TypeFactoryUtils.mapType(String.class, Integer.class).getBindings().getTypeParameters())
			.isEqualTo(List.of(TypeFactoryUtils.javaType(String.class), TypeFactoryUtils.javaType(Integer.class)));
		assertThat(TypeFactoryUtils.mapType(Integer.class, String.class).getBindings().getTypeParameters())
			.isEqualTo(List.of(TypeFactoryUtils.javaType(Integer.class), TypeFactoryUtils.javaType(String.class)));
		assertThat(TypeFactoryUtils.mapType(Integer.class, Integer.class).getBindings().getTypeParameters())
			.isEqualTo(List.of(TypeFactoryUtils.javaType(Integer.class), TypeFactoryUtils.javaType(Integer.class)));
	}

	@Test
	void testJsonReadValue() throws IOException {
		JsonNode result1 = JSON.readValue(json, JsonNode.class);
		assertThat(result1).isNotNull();
		assertThat(result1.get("c").asText()).isEqualTo("1");
		assertThat(result1.get("a").asText()).isEqualTo("2");
		assertThat(result1.get("b").get("c").asInt()).isEqualTo(3);

		InputStream inputStream = new ClassPathResource("input.json").getInputStream();
		JsonNode result2 = JSON.readValue(inputStream, JsonNode.class);
		assertThat(result2).isNotNull();
		assertThat(result2.get("c").asText()).isEqualTo("1");
		assertThat(result2.get("a").asText()).isEqualTo("2");
		assertThat(result2.get("b").get("c").asInt()).isEqualTo(3);

		JsonNode result3 = JSON.readValue(json, new TypeReference<>() {
		});
		assertThat(result3).isNotNull();
		assertThat(result3.get("c").asText()).isEqualTo("1");
		assertThat(result3.get("a").asText()).isEqualTo("2");
		assertThat(result3.get("b").get("c").asInt()).isEqualTo(3);

		JavaType javaType = TypeFactoryUtils.javaType(JsonNode.class);
		JsonNode result4 = JSON.readValue(json, javaType);
		assertThat(result4).isNotNull();
		assertThat(result4.get("c").asText()).isEqualTo("1");
		assertThat(result4.get("a").asText()).isEqualTo("2");
		assertThat(result4.get("b").get("c").asInt()).isEqualTo(3);
	}

	@Test
	void testYamlReadValue() throws IOException {
		JsonNode result1 = YAML.readValue(yaml, JsonNode.class);
		assertThat(result1).isNotNull();
		assertThat(result1.get("c").asText()).isEqualTo("1");
		assertThat(result1.get("a").asText()).isEqualTo("2");
		assertThat(result1.get("b").get("c").asInt()).isEqualTo(3);

		InputStream inputStream = new ClassPathResource("input.yml").getInputStream();
		JsonNode result2 = YAML.readValue(inputStream, JsonNode.class);
		assertThat(result2).isNotNull();
		assertThat(result2.get("c").asText()).isEqualTo("1");
		assertThat(result2.get("a").asText()).isEqualTo("2");
		assertThat(result2.get("b").get("c").asInt()).isEqualTo(3);

		JsonNode result3 = YAML.readValue(yaml, new TypeReference<>() {
		});
		assertThat(result3).isNotNull();
		assertThat(result3.get("c").asText()).isEqualTo("1");
		assertThat(result3.get("a").asText()).isEqualTo("2");
		assertThat(result3.get("b").get("c").asInt()).isEqualTo(3);

		JavaType javaType = TypeFactoryUtils.javaType(JsonNode.class);
		JsonNode result4 = YAML.readValue(yaml, javaType);
		assertThat(result4).isNotNull();
		assertThat(result4.get("c").asText()).isEqualTo("1");
		assertThat(result4.get("a").asText()).isEqualTo("2");
		assertThat(result4.get("b").get("c").asInt()).isEqualTo(3);

	}

	@Test
	void testXmlReadValue() throws IOException {
		JsonNode result1 = XML.readValue(xml, JsonNode.class);
		assertThat(result1).isNotNull();
		assertThat(result1.get("c").asText()).isEqualTo("1");
		assertThat(result1.get("a").asText()).isEqualTo("2");
		assertThat(result1.get("b").get("c").asInt()).isEqualTo(3);

		InputStream inputStream = new ClassPathResource("input.xml").getInputStream();
		JsonNode result2 = XML.readValue(inputStream, JsonNode.class);
		assertThat(result2).isNotNull();
		assertThat(result2.get("c").asText()).isEqualTo("1");
		assertThat(result2.get("a").asText()).isEqualTo("2");
		assertThat(result2.get("b").get("c").asInt()).isEqualTo(3);

		JsonNode result3 = XML.readValue(xml, new TypeReference<>() {
		});
		assertThat(result3).isNotNull();
		assertThat(result3.get("c").asText()).isEqualTo("1");
		assertThat(result3.get("a").asText()).isEqualTo("2");
		assertThat(result3.get("b").get("c").asInt()).isEqualTo(3);

		JavaType javaType = TypeFactoryUtils.javaType(JsonNode.class);
		JsonNode result4 = XML.readValue(xml, javaType);
		assertThat(result4).isNotNull();
		assertThat(result4.get("c").asText()).isEqualTo("1");
		assertThat(result4.get("a").asText()).isEqualTo("2");
		assertThat(result4.get("b").get("c").asInt()).isEqualTo(3);
	}

	@Test
	void testToJsonStr() {
		String result = JSON.writeValueAsString(Map.of("username", "password"));
		@Language("json")
		String json = "{\"username\":\"password\"}";
		assertThat(result).isEqualTo(json);

		byte[] bytes = JSON.writeValueAsBytes(Map.of("username", "password"));
		assertThat(bytes).isEqualTo(json.getBytes());
	}

	@Test
	void testToYamlStr() {
		String result = YAML.writeValueAsString(Map.of("username", "password"));
		@Language("yml")
		String yml = """
				---
				username: "password"
				""";
		assertThat(result).isEqualTo(yml);

		byte[] bytes = YAML.writeValueAsBytes(Map.of("username", "password"));
		assertThat(bytes).isEqualTo(yml.getBytes());
	}

	@Test
	void testToXmlStr() {
		String result = XML.writeValueAsString(Map.of("username", "password"));
		@Language("xml")
		String xml = "<Map1><username>password</username></Map1>";
		assertThat(result).isEqualTo(xml);

		byte[] bytes = XML.writeValueAsBytes(Map.of("username", "password"));
		assertThat(bytes).isEqualTo(xml.getBytes());
	}

	@Test
	void testReadTree() {
		JsonNode jsonNode = JSON.readTree(json);
		assertThat(jsonNode).isNotNull();
		assertThat(jsonNode.get("c").asText()).isEqualTo("1");
		assertThat(jsonNode.get("a").asText()).isEqualTo("2");
		assertThat(jsonNode.get("b").get("c").asInt()).isEqualTo(3);

		JsonNode yamlNode = YAML.readTree(yaml);
		assertThat(yamlNode).isNotNull();
		assertThat(yamlNode.get("c").asText()).isEqualTo("1");
		assertThat(yamlNode.get("a").asText()).isEqualTo("2");
		assertThat(yamlNode.get("b").get("c").asInt()).isEqualTo(3);

		JsonNode xmlNode = XML.readTree(xml);
		assertThat(xmlNode).isNotNull();
		assertThat(xmlNode.get("c").asText()).isEqualTo("1");
		assertThat(xmlNode.get("a").asText()).isEqualTo("2");
		assertThat(xmlNode.get("b").get("c").asInt()).isEqualTo(3);
	}

	@Test
	void convertJson() {
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
		JsonNode jsonNode = JSON.readTree(json);
		JsonNode dependencyArray = jsonNode.get("dependency");

		Map<String, String> loggingDependency = Map.of("groupId", "org.springframework.boot", "artifactId",
				"spring-boot-starter-logging");
		Map<String, String> jsonDependency = Map.of("groupId", "org.springframework.boot", "artifactId",
				"spring-boot-starter-json");

		MapType mapType = TypeFactoryUtils.mapType(String.class, String.class);
		List<JsonNode> jsonNodeList = Streams.stream(dependencyArray.elements()).toList();

		assertThat(JSON.<Map<String, String>>convertValue(jsonNodeList.get(0), mapType)).isEqualTo(loggingDependency);
		assertThat(JSON.<Map<String, String>>convertValue(jsonNodeList.get(1), mapType)).isEqualTo(jsonDependency);

		List<Map<String, String>> dependencyList = List.of(loggingDependency, jsonDependency);
		CollectionType collectionType = TypeFactoryUtils.listType(mapType);

		assertThat(JSON.<List<Map<String, String>>>convertValue(dependencyArray, collectionType))
			.isEqualTo(dependencyList);

		JavaType javaType = TypeFactoryUtils.javaType(String.class);
		Map<String, List<Map<String, String>>> dependencyManagement = Map.of("dependency", dependencyList);
		MapType constructMapType = TypeFactoryUtils.mapType(javaType, collectionType);

		assertThat(JSON.<Map<String, List<Map<String, String>>>>convertValue(jsonNode, constructMapType))
			.isEqualTo(dependencyManagement);
	}

	@Test
	void convertYaml() {
		@Language("yml")
		String yml = """
				---
				dependency:
				  - groupId: org.springframework.boot
				    artifactId: spring-boot-starter-logging
				  - groupId: org.springframework.boot
				    artifactId: spring-boot-starter-json
				""";
		JsonNode jsonNode = YAML.readTree(yml);
		JsonNode dependencyArray = jsonNode.get("dependency");

		Map<String, String> loggingDependency = Map.of("groupId", "org.springframework.boot", "artifactId",
				"spring-boot-starter-logging");
		Map<String, String> jsonDependency = Map.of("groupId", "org.springframework.boot", "artifactId",
				"spring-boot-starter-json");

		MapType mapType = TypeFactoryUtils.mapType(String.class, String.class);
		List<JsonNode> jsonNodeList = Streams.stream(dependencyArray.elements()).toList();

		assertThat(YAML.<Map<String, String>>convertValue(jsonNodeList.getFirst(), mapType))
			.isEqualTo(loggingDependency);
		assertThat(YAML.<Map<String, String>>convertValue(jsonNodeList.get(1), mapType)).isEqualTo(jsonDependency);

		List<Map<String, String>> dependencyList = List.of(loggingDependency, jsonDependency);
		CollectionType collectionType = TypeFactoryUtils.listType(mapType);

		assertThat(YAML.<List<Map<String, String>>>convertValue(dependencyArray, collectionType))
			.isEqualTo(dependencyList);

		JavaType javaType = TypeFactoryUtils.javaType(String.class);
		Map<String, List<Map<String, String>>> dependencyManagement = Map.of("dependency", dependencyList);
		MapType constructMapType = TypeFactoryUtils.mapType(javaType, collectionType);

		assertThat(YAML.<Map<String, List<Map<String, String>>>>convertValue(jsonNode, constructMapType))
			.isEqualTo(dependencyManagement);
	}

	@Test
	void convertXml() {
		@Language("xml")
		String xml = """
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

		Map<String, String> loggingDependency = Map.of("groupId", "org.springframework.boot", "artifactId",
				"spring-boot-starter-logging");
		Map<String, String> jsonDependency = Map.of("groupId", "org.springframework.boot", "artifactId",
				"spring-boot-starter-json");

		MapType mapType = TypeFactoryUtils.mapType(String.class, String.class);
		List<JsonNode> jsonNodeList = Streams.stream(dependencyArray.elements()).toList();

		assertThat(XML.<Map<String, String>>convertValue(jsonNodeList.get(0), mapType)).isEqualTo(loggingDependency);
		assertThat(XML.<Map<String, String>>convertValue(jsonNodeList.get(1), mapType)).isEqualTo(jsonDependency);

		List<Map<String, String>> dependencyList = List.of(loggingDependency, jsonDependency);
		CollectionType collectionType = TypeFactoryUtils.listType(mapType);

		assertThat(XML.<List<Map<String, String>>>convertValue(dependencyArray, collectionType))
			.isEqualTo(dependencyList);

		JavaType javaType = TypeFactoryUtils.javaType(String.class);
		Map<String, List<Map<String, String>>> dependencyManagement = Map.of("dependency", dependencyList);
		MapType constructMapType = TypeFactoryUtils.mapType(javaType, collectionType);

		assertThat(XML.<Map<String, List<Map<String, String>>>>convertValue(jsonNode, constructMapType))
			.isEqualTo(dependencyManagement);

	}

}
