package com.livk.commons.jackson;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
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
}
