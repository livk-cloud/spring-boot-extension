package com.livk.commons.jackson;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;
import com.livk.commons.bean.domain.Pair;
import com.livk.commons.collect.util.StreamUtils;
import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * <p>
 * JacksonUtilsTest
 * </p>
 *
 * @author livk
 */
class JacksonUtilsTest {

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
    void javaType() {
        assertEquals(String.class, JacksonUtils.javaType(String.class).getRawClass());
        assertEquals(Integer.class, JacksonUtils.javaType(Integer.class).getRawClass());
        assertEquals(Long.class, JacksonUtils.javaType(Long.class).getRawClass());

        JavaType javaType = JacksonUtils.javaType(Pair.class, String.class, Integer.class);
        assertEquals(Pair.class, javaType.getRawClass());
        assertEquals(String.class, javaType.getBindings().getBoundType(0).getRawClass());
        assertEquals(Integer.class, javaType.getBindings().getBoundType(1).getRawClass());

        assertEquals(String.class, JacksonUtils.collectionType(String.class).getBindings().getBoundType(0).getRawClass());
        assertEquals(Integer.class, JacksonUtils.collectionType(Integer.class).getBindings().getBoundType(0).getRawClass());
        assertEquals(Long.class, JacksonUtils.collectionType(Long.class).getBindings().getBoundType(0).getRawClass());

        assertEquals(List.of(JacksonUtils.javaType(String.class), JacksonUtils.javaType(String.class)),
                JacksonUtils.mapType(String.class, String.class).getBindings().getTypeParameters());
        assertEquals(List.of(JacksonUtils.javaType(String.class), JacksonUtils.javaType(Integer.class)),
                JacksonUtils.mapType(String.class, Integer.class).getBindings().getTypeParameters());
        assertEquals(List.of(JacksonUtils.javaType(Integer.class), JacksonUtils.javaType(String.class)),
                JacksonUtils.mapType(Integer.class, String.class).getBindings().getTypeParameters());
        assertEquals(List.of(JacksonUtils.javaType(Integer.class), JacksonUtils.javaType(Integer.class)),
                JacksonUtils.mapType(Integer.class, Integer.class).getBindings().getTypeParameters());
    }

    @Test
    void testReadValue() throws IOException {


        JsonNode result1 = JacksonUtils.readValue(json, JsonNode.class);
        assertNotNull(result1);
        assertEquals("1", result1.get("c").asText());
        assertEquals("2", result1.get("a").asText());
        assertEquals(3, result1.get("b").get("c").asInt());

        InputStream inputStream = new ClassPathResource("input.json").getInputStream();
        JsonNode result2 = JacksonUtils.readValue(inputStream, JsonNode.class);
        assertNotNull(result2);
        assertEquals("1", result2.get("c").asText());
        assertEquals("2", result2.get("a").asText());
        assertEquals(3, result2.get("b").get("c").asInt());

        JsonNode result3 = JacksonUtils.readValue(json, new TypeReference<>() {
        });
        assertNotNull(result3);
        assertEquals("1", result3.get("c").asText());
        assertEquals("2", result3.get("a").asText());
        assertEquals(3, result3.get("b").get("c").asInt());
    }

    @Test
    void testToJsonStr() {
        String result = JacksonUtils.writeValueAsString(Map.of("username", "password"));
        String json = "{\"username\":\"password\"}";
        assertEquals(json, result);
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
        List<JsonNode> result = JacksonUtils.readValueList(json, JsonNode.class);
        assertNotNull(result);
        assertEquals(1, result.get(0).get("a").asInt());
        assertEquals(1, result.get(1).get("a").asInt());
    }

    @SuppressWarnings("unchecked")
    @Test
    void testToMap() {
        Map<String, Object> result = JacksonUtils.readValueMap(json, String.class, Object.class);
        assertNotNull(result);
        assertEquals("1", result.get("c"));
        assertEquals("2", result.get("a"));
        assertEquals(3, ((Map<String, Object>) result.get("b")).get("c"));
    }

    @Test
    void testToProperties() throws IOException {
        InputStream inputStream = new ClassPathResource("properties.json").getInputStream();
        Properties result = JacksonUtils.readValueProperties(inputStream);
        assertNotNull(result);
        assertEquals("1", result.get("c"));
        assertEquals("2", result.get("a"));
    }

    @Test
    void testReadTree() {
        JsonNode result = JacksonUtils.readTree(json);
        assertNotNull(result);
        assertEquals("1", result.get("c").asText());
        assertEquals("2", result.get("a").asText());
        assertEquals(3, result.get("b").get("c").asInt());
    }

    @Test
    void objectToMap() {
        Pair<String, String> pair = Pair.of("username", "password");
        Map<String, String> map = JacksonUtils.convertValueMap(pair, String.class, String.class);
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
        JsonNode jsonNode = JacksonUtils.readTree(json);
        JsonNode dependencyArray = jsonNode.get("dependency");

        Map<String, String> loggingDependency = Map.of("groupId", "org.springframework.boot", "artifactId", "spring-boot-starter-logging");
        Map<String, String> jsonDependency = Map.of("groupId", "org.springframework.boot", "artifactId", "spring-boot-starter-json");
        MapType mapType = JacksonUtils.typeFactory().constructMapType(Map.class, String.class, String.class);
        List<JsonNode> jsonNodeList = StreamUtils.convert(dependencyArray.elements()).toList();
        assertEquals(loggingDependency, JacksonUtils.convertValue(jsonNodeList.get(0), mapType));
        assertEquals(jsonDependency, JacksonUtils.convertValue(jsonNodeList.get(1), mapType));

        List<Map<String, String>> dependencyList = List.of(loggingDependency, jsonDependency);
        CollectionType collectionType = JacksonUtils.typeFactory().constructCollectionType(List.class, mapType);
        assertEquals(dependencyList, JacksonUtils.convertValue(dependencyArray, collectionType));

        JavaType javaType = JacksonUtils.typeFactory().constructType(String.class);
        Map<String, List<Map<String, String>>> dependencyManagement = Map.of("dependency", dependencyList);
        MapType constructMapType = JacksonUtils.typeFactory().constructMapType(Map.class, javaType, collectionType);
        assertEquals(dependencyManagement, JacksonUtils.convertValue(jsonNode, constructMapType));
    }
}
