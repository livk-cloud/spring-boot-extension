package com.livk.commons.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.livk.commons.util.JacksonUtils;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

/**
 * <p>
 * JacksonUtilsTest
 * </p>
 *
 * @author livk
 * @date 2022/7/5
 */
class JacksonUtilsTest {

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
    void getNodeFirst() {
        JsonNode node = JacksonUtils.readTree("""
                {
                    "c": "1",
                    "a": "2",
                    "b": {
                        "c": 3
                    }
                }""");
        assertEquals("1", JacksonUtils.findNodeFirst(node, "c").asText());
        assertEquals("2", JacksonUtils.findNodeFirst(node, "a").asText());
    }

    @Test
    void getNodeFirstAll() {
        JsonNode node = JacksonUtils.readTree("""
                {
                    "c": "1",
                    "a": "2",
                    "b": {
                        "c": "3",
                        "a": "4"
                    }
                }""");
        assertArrayEquals(new String[]{"1", "3"}, JacksonUtils.findNodeAll(node, "c").stream().map(JsonNode::asText).toArray(String[]::new));
        assertArrayEquals(new String[]{"2", "4"}, JacksonUtils.findNodeAll(node, "a").stream().map(JsonNode::asText).toArray(String[]::new));
    }

    @Test
    void getNode() {
        JsonNode node = JacksonUtils.readTree("""
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
        assertEquals("1", JacksonUtils.findNode(node, "c").asText());
        assertEquals("2", JacksonUtils.findNode(node, "a").asText());
        assertEquals("3", JacksonUtils.findNode(node, "b.c").asText());
        assertEquals("6", JacksonUtils.findNode(node, "b.d.ab").asText());
        assertEquals("7", JacksonUtils.findNode(node, "f.0.a").asText());
        assertEquals("8", JacksonUtils.findNode(node, "f.1.a").asText());
        assertEquals("9", JacksonUtils.findNode(node, "f.2.a").asText());
    }
}
