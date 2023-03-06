package com.livk.commons.util;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * <p>
 * YamlUtilsTest
 * </p>
 *
 * @author livk
 */
class YamlUtilsTest {

    Map<String, String> map = Map.of("spring.redis.host", "livk.com", "spring.redis.port", "5672");

    @Test
    void mapToYmlTest() {
        String yml = """
                spring:
                  redis:
                    port: '5672'
                    host: livk.com
                    """;
        String result = YamlUtils.mapToYml(map);
        assertEquals(yml, result);
    }

    @Test
    public void mapToMapTest() {
        Map<String, Object> ymlMap = YamlUtils.mapToYmlMap(map);
        Map<String, Object> result = YamlUtils.ymlMapToMap(ymlMap);
        assertEquals(map, result);
    }
}
