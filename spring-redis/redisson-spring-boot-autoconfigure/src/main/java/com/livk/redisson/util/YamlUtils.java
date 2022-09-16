package com.livk.redisson.util;

import lombok.experimental.UtilityClass;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * YamlUtils
 * </p>
 *
 * @author livk
 * @date 2022/9/16
 */
@UtilityClass
public class YamlUtils {

    private static final Yaml YAML = new Yaml(new Constructor());

    public String mapToYml(Map<String, Object> map) {
        if (map == null || map.isEmpty()) {
            return "";
        }
        Map<String, Object> yamlMap = mapToYmlMap(map);
        return YAML.dumpAsMap(yamlMap);
    }


    @SuppressWarnings("unchecked")
    private synchronized Map<String, Object> mapToYmlMap(Map<String, Object> map) {
        Map<String, Object> yml = new HashMap<>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            int index = key.indexOf('.');
            if (index != -1) {
                String newKey = key.substring(0, index);
                String childKey = key.substring(index).replaceFirst(".", "");
                if (yml.containsKey(newKey)) {
                    Map<String, Object> child = (Map<String, Object>) yml.get(newKey);
                    child.put(childKey, value);
                } else {
                    Map<String, Object> child = new HashMap<>();
                    child.put(childKey, value);
                    yml.put(newKey, child);
                }
            } else {
                yml.put(key, value);
            }
        }
        for (Map.Entry<String, Object> entry : yml.entrySet()) {
            if (entry.getValue() instanceof Map) {
                Map<String, Object> childMap = mapToYmlMap((Map<String, Object>) entry.getValue());
                yml.put(entry.getKey(), childMap);
            }
        }
        return yml;
    }
}

