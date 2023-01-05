package com.livk.commons.jackson;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * <p>
 * JsonNodeUtilsTest
 * </p>
 *
 * @author livk
 * @date 2023/1/3
 */
class JsonNodeUtilsTest {

    static final String json = """
            {
              "username": "root",
              "password": "root",
              "info": {
                "name": "livk",
                "email": "1375632510@qq.com"
              }
            }""";

    static final JsonNode node = JacksonUtils.readTree(json);

    @Test
    void findStringValue() {
        String username = JsonNodeUtils.findStringValue(node, "username");
        assertEquals("root", username);
    }

    @Test
    void findValue() {
        Map<String, Object> map = JsonNodeUtils.findValue(node, "info",
                JsonNodeUtils.STRING_OBJECT_MAP, JsonMapper.builder().build());
        assertEquals(Map.of("name", "livk", "email", "1375632510@qq.com"), map);
    }

    @Test
    void findObjectNode() {
        JsonNode jsonNode = JsonNodeUtils.findObjectNode(node, "info");
        assertEquals("livk", jsonNode.get("name").asText());
        assertEquals("1375632510@qq.com", jsonNode.get("email").asText());
    }
}
