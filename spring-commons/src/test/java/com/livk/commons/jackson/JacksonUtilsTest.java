package com.livk.commons.jackson;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;
import com.livk.commons.domain.Pair;
import com.livk.commons.util.StreamUtils;
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

    @Test
    void javaType() {
        assertEquals(String.class, JacksonUtils.javaType(String.class).getRawClass());
        assertEquals(Integer.class, JacksonUtils.javaType(Integer.class).getRawClass());
        assertEquals(Long.class, JacksonUtils.javaType(Long.class).getRawClass());

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
    void testToBean() {
        JsonNode result = JacksonUtils.toBean("""
                {
                                    "c": "1",
                                    "a": "2",
                                    "b": {
                                        "c": 3
                                    }
                                }""", JsonNode.class);
        assertNotNull(result);
    }

    @Test
    void testToBean2() throws IOException {
        InputStream inputStream = new ClassPathResource("input.json").getInputStream();
        JsonNode result = JacksonUtils.toBean(inputStream, JsonNode.class);
        assertNotNull(result);
    }

    @Test
    void testToJsonStr() {
        String result = JacksonUtils.toJsonStr(Map.of("username", "password"));
        String json = "{\"username\":\"password\"}";
        assertEquals(json, result);
    }

    @Test
    void testToList() {
        //language=JSON
        String json = "[" +
                      "{},{}" +
                      "]";
        List<JsonNode> result = JacksonUtils.toList(json, JsonNode.class);
        assertNotNull(result);
    }

    @Test
    void testToMap() {
        Map<String, Object> result = JacksonUtils.toMap("""
                {
                                    "c": "1",
                                    "a": "2",
                                    "b": {
                                        "c": 3
                                    }
                                }""", String.class, Object.class);
        assertNotNull(result);
    }

    @Test
    void testToProperties() throws IOException {
        InputStream inputStream = new ClassPathResource("input.json").getInputStream();
        Properties result = JacksonUtils.toProperties(inputStream);
        assertNotNull(result);
    }

    @Test
    void testToBean3() {
        JsonNode result = JacksonUtils.toBean("""
                {
                                    "c": "1",
                                    "a": "2",
                                    "b": {
                                        "c": 3
                                    }
                                }""", new TypeReference<>() {
        });
        assertNotNull(result);
    }

    @Test
    void testReadTree() {
        JsonNode result = JacksonUtils.readTree("""
                {
                                    "c": "1",
                                    "a": "2",
                                    "b": {
                                        "c": 3
                                    }
                                }""");
        assertNotNull(result);
    }

    @Test
    void objectToMap() {
        Pair<String, String> pair = Pair.of("username", "password");
        Map<String, String> map = JacksonUtils.objectToMap(pair, String.class, String.class);
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
        assertEquals(loggingDependency, JacksonUtils.convert(jsonNodeList.get(0), mapType));
        assertEquals(jsonDependency, JacksonUtils.convert(jsonNodeList.get(1), mapType));

        List<Map<String, String>> dependencyList = List.of(loggingDependency, jsonDependency);
        CollectionType collectionType = JacksonUtils.typeFactory().constructCollectionType(List.class, mapType);
        assertEquals(dependencyList, JacksonUtils.convert(dependencyArray, collectionType));

        JavaType javaType = JacksonUtils.typeFactory().constructType(String.class);
        Map<String, List<Map<String, String>>> dependencyManagement = Map.of("dependency", dependencyList);
        MapType constructMapType = JacksonUtils.typeFactory().constructMapType(Map.class, javaType, collectionType);
        assertEquals(dependencyManagement, JacksonUtils.convert(jsonNode, constructMapType));
    }
}
