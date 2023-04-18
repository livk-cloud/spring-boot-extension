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

package com.livk.commons.util;

import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Properties;

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
        String result = YamlUtils.toYml(map);
        assertEquals(yml, result);
    }

    @Test
    public void mapToMapTest() {
        Map<String, Object> ymlMap = YamlUtils.toYmlMap(map);
        Properties result = YamlUtils.ymlMapToMap(ymlMap);
        assertEquals(map, result);
    }
}
