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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;
import com.livk.commons.bean.domain.Pair;
import com.livk.commons.collect.util.StreamUtils;
import com.livk.commons.jackson.support.JacksonSupport;
import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * <p>
 * JsonMapperUtilsTest
 * </p>
 *
 * @author livk
 */
class JsonMapperUtilsTest {

    @Language("json")
    static String json = """
            {
                                "c": "1",
                                "a": "2",
                                "b": {
                                    "c": 3
                                }
                            }""";

    @Test
    void testReadValue() throws IOException {


        JsonNode result1 = JsonMapperUtils.readValue(json, JsonNode.class);
        assertNotNull(result1);
        assertEquals("1", result1.get("c").asText());
        assertEquals("2", result1.get("a").asText());
        assertEquals(3, result1.get("b").get("c").asInt());

        InputStream inputStream = new ClassPathResource("input.json").getInputStream();
        JsonNode result2 = JsonMapperUtils.readValue(inputStream, JsonNode.class);
        assertNotNull(result2);
        assertEquals("1", result2.get("c").asText());
        assertEquals("2", result2.get("a").asText());
        assertEquals(3, result2.get("b").get("c").asInt());

        JsonNode result3 = JsonMapperUtils.readValue(json, new TypeReference<>() {
        });
        assertNotNull(result3);
        assertEquals("1", result3.get("c").asText());
        assertEquals("2", result3.get("a").asText());
        assertEquals(3, result3.get("b").get("c").asInt());

        JavaType javaType = JacksonSupport.javaType(JsonNode.class);
        JsonNode result4 = JsonMapperUtils.readValue(json, javaType);
        assertNotNull(result3);
        assertEquals("1", result4.get("c").asText());
        assertEquals("2", result4.get("a").asText());
        assertEquals(3, result4.get("b").get("c").asInt());
    }

    @Test
    void testToJsonStr() {
        String result = JsonMapperUtils.writeValueAsString(Map.of("username", "password"));
        String json = "{\"username\":\"password\"}";
        assertEquals(json, result);

        byte[] bytes = JsonMapperUtils.writeValueAsBytes(Map.of("username", "password"));
        assertArrayEquals(json.getBytes(), bytes);
    }

    @Test
    void testToList() {
        @Language("json") String json = """
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
    void testToMap() throws IOException {
        Map<String, Object> result = JsonMapperUtils.readValueMap(json, String.class, Object.class);
        assertNotNull(result);
        assertEquals("1", result.get("c"));
        assertEquals("2", result.get("a"));
        assertEquals(3, ((Map<String, Object>) result.get("b")).get("c"));

        Map<String, Object> result1 = JsonMapperUtils.readValueMap(new ClassPathResource("input.json").getInputStream(), String.class, Object.class);
        assertNotNull(result1);
        assertEquals("1", result1.get("c"));
        assertEquals("2", result1.get("a"));
        assertEquals(3, ((Map<String, Object>) result1.get("b")).get("c"));
    }

    @Test
    void testReadTree() {
        JsonNode result = JsonMapperUtils.readTree(json);
        assertNotNull(result);
        assertEquals("1", result.get("c").asText());
        assertEquals("2", result.get("a").asText());
        assertEquals(3, result.get("b").get("c").asInt());
    }

    @Test
    void objectToMap() {
        Pair<String, String> pair = Pair.of("username", "password");
        Map<String, String> map = JsonMapperUtils.convertValueMap(pair, String.class, String.class);
        assertEquals(pair.toMap(), map);
    }

    @Test
    void convert() {
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
        JsonNode jsonNode = JsonMapperUtils.readTree(json);
        JsonNode dependencyArray = jsonNode.get("dependency");

        Map<String, String> loggingDependency = Map.of("groupId", "org.springframework.boot", "artifactId", "spring-boot-starter-logging");
        Map<String, String> jsonDependency = Map.of("groupId", "org.springframework.boot", "artifactId", "spring-boot-starter-json");
        MapType mapType = JacksonSupport.mapType(String.class, String.class);
        List<JsonNode> jsonNodeList = StreamUtils.convert(dependencyArray.elements()).toList();
        assertEquals(loggingDependency, JsonMapperUtils.convertValue(jsonNodeList.get(0), mapType));
        assertEquals(jsonDependency, JsonMapperUtils.convertValue(jsonNodeList.get(1), mapType));

        List<Map<String, String>> dependencyList = List.of(loggingDependency, jsonDependency);
        CollectionType collectionType = JacksonSupport.typeFactory().constructCollectionType(List.class, mapType);
        assertEquals(dependencyList, JsonMapperUtils.convertValue(dependencyArray, collectionType));

        JavaType javaType = JacksonSupport.javaType(String.class);
        Map<String, List<Map<String, String>>> dependencyManagement = Map.of("dependency", dependencyList);
        MapType constructMapType = JacksonSupport.typeFactory().constructMapType(Map.class, javaType, collectionType);
        assertEquals(dependencyManagement, JsonMapperUtils.convertValue(jsonNode, constructMapType));
    }
}
