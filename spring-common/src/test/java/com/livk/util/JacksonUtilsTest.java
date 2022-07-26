package com.livk.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;

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
        JsonNode result = JacksonUtils.toBean("{}", JsonNode.class);
        Assertions.assertNotNull(result);
    }

    @Test
    void testToBean2() throws IOException {
        InputStream inputStream = new ClassPathResource("input.json").getInputStream();
        JsonNode result = JacksonUtils.toBean(inputStream, JsonNode.class);
        Assertions.assertNotNull(result);
    }

    @Test
    void testToJsonStr() {
        String result = JacksonUtils.toJsonStr(Map.of("username", "password"));
        //language=JSON
        String json = "{\"username\":\"password\"}";
        Assertions.assertEquals(json, result);
    }

    @Test
    void testToList() {
        //language=JSON
        String json = "[" +
                      "{},{}" +
                      "]";
        List<JsonNode> result = JacksonUtils.toList(json, JsonNode.class);
        Assertions.assertNotNull(result);
    }

    @Test
    void testToMap() {
        Map<String, String> result = JacksonUtils.toMap("{}", String.class, String.class);
        Assertions.assertNotNull(result);
    }

    @Test
    void testToProperties() throws IOException {
        InputStream inputStream = new ClassPathResource("input.json").getInputStream();
        Properties result = JacksonUtils.toProperties(inputStream);
        Assertions.assertNotNull(result);
    }

    @Test
    void testToBean3() {
        JsonNode result = JacksonUtils.toBean("{}", new TypeReference<JsonNode>() {
        });
        Assertions.assertNotNull(result);
    }

    @Test
    void testReadTree() {
        JsonNode result = JacksonUtils.readTree("{}");
        Assertions.assertNotNull(result);
    }
}
