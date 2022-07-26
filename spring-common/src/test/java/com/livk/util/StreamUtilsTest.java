package com.livk.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * StreamUtilsTest
 * </p>
 *
 * @author livk
 * @date 2022/7/26
 */
class StreamUtilsTest {

    @Test
    void testConcat3() {
        Map<String, List<String>> result = StreamUtils.concat(Map.of("username", "livk", "password", "123456"),
                Map.of("username", "root", "host", "192.168.1.1"));
        Map<String, List<String>> listMap = Map.of("username", List.of("livk", "root"), "password",
                List.of("123456"), "host", List.of("192.168.1.1"));
        Assertions.assertEquals(listMap, result);
    }
}
