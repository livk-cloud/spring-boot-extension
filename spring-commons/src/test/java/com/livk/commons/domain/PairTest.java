package com.livk.commons.domain;

import com.fasterxml.jackson.core.type.TypeReference;
import com.livk.commons.jackson.JacksonUtils;
import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * <p>
 * PairTest
 * </p>
 *
 * @author livk
 */
class PairTest {

    static Pair<String, Integer> pair = Pair.of("livk", 123456);

    @Test
    void toMap() {
        assertEquals(pair.toMap(), Map.of("livk", 123456));
    }

    @Test
    public void pairJsonSerializerTest() {
        String json = "{\"livk\":123456}";
        String result = JacksonUtils.toJsonStr(pair);
        assertEquals(json, result);
    }

    @Test
    public void pairJsonDeserializerTest() {
        @Language("JSON") String json = "{\"livk\":123456}";
        Pair<String, Integer> result = JacksonUtils.toBean(json, new TypeReference<>() {
        });
        assertEquals("livk", result.key());
        assertEquals(123456, result.value());

        @Language("JSON") String json2 = """
                {"livk": {"root": "username"}}""";
        Pair<String, Pair<String, String>> result2 = JacksonUtils.toBean(json2, new TypeReference<>() {
        });
        assertEquals("livk", result2.key());
        assertEquals("root", result2.value().key());
        assertEquals("username", result2.value().value());

        @Language("JSON") String json3 = """
                {"livk":  [1,2,3]}""";
        Pair<String, List<Integer>> result3 = JacksonUtils.toBean(json3, new TypeReference<>() {
        });
        assertEquals("livk", result3.key());
        assertEquals(List.of(1, 2, 3), result3.value());

        @Language("JSON") String json4 = """
                {
                  "livk": {
                    "username": "root",
                    "password": "root"
                  }
                }""";
        Pair<String, Map<String, String>> result4 = JacksonUtils.toBean(json4, new TypeReference<>() {
        });
        assertEquals("livk", result4.key());
        assertEquals(Map.of("username", "root", "password", "root"), result4.value());
    }
}
