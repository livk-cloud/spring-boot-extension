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

import lombok.experimental.UtilityClass;
import org.springframework.util.CollectionUtils;
import org.yaml.snakeyaml.Yaml;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * <p>
 * YamlUtils
 * </p>
 *
 * @author livk
 */
@UtilityClass
public class YamlUtils {

    private static final Yaml YAML = new Yaml();

    /**
     * {example Map.of(" a.b.c ", " 1 ") -> YAML}
     *
     * @param map properties key map
     * @return yml str
     */
    public String toYml(Map<?, ?> map) {
        if (CollectionUtils.isEmpty(map)) {
            return "";
        }
        Map<String, Object> yamlMap = toYmlMap(map);
        return YAML.dumpAsMap(yamlMap);
    }

    /**
     * example Map.of(" a.b.c ", " 1 ") -> Map.of("a",Map.of("b",Map.of("c","1")))
     *
     * @param map properties key map
     * @return yml map
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> toYmlMap(Map<?, ?> map) {
        Map<String, Object> result = new HashMap<>();
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            String key = String.valueOf(entry.getKey());
            Object value = map.get(key);
            String[] keys = key.split("\\.");

            Map<String, Object> cursor = result;
            for (int i = 0; i < keys.length; i++) {
                String k = keys[i];
                if (cursor.containsKey(k)) {
                    // 检查下一级Map中是否存在该键
                    cursor = (Map<String, Object>) cursor.get(k);
                } else if (i == keys.length - 1) {
                    cursor.put(k, value);
                } else {
                    Map<String, Object> next = new HashMap<>();
                    cursor.put(k, next);
                    cursor = next;
                }
            }
        }
        return result;
    }

    /**
     * example Map.of("a",Map.of("b",Map.of("c","1"))) -> Map.of(" a.b.c ", " 1 ")
     *
     * @param map the map
     * @return the map
     */
    @SuppressWarnings("unchecked")
    public Properties ymlMapToMap(Map<String, Object> map) {
        Properties result = new Properties();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof Map) {
                Map<String, Object> childMap = (Map<String, Object>) value;
                Properties childResult = ymlMapToMap(childMap);
                for (Map.Entry<Object, Object> childEntry : childResult.entrySet()) {
                    result.put(key + "." + childEntry.getKey(), childEntry.getValue());
                }
            } else {
                result.put(key, value);
            }
        }
        return result;
    }
}

