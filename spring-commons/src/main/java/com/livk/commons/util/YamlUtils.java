package com.livk.commons.util;

import lombok.experimental.UtilityClass;
import org.yaml.snakeyaml.LoaderOptions;
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
 * @date 2022/10/12
 */
@UtilityClass
public class YamlUtils {

    private static final Yaml YAML = new Yaml(new Constructor(Object.class, new LoaderOptions()));

    /**
     * {@example Map.of(" a.b.c ", " 1 ") -> YAML}
     *
     * @param map properties key map
     * @return yml str
     */
    public String mapToYml(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return "";
        }
        Map<String, Object> yamlMap = mapToYmlMap(map);
        return YAML.dumpAsMap(yamlMap);
    }

    /**
     * {@example Map.of(" a.b.c ", " 1 ") -> Map.of("a",Map.of("b",Map.of("c","1")))}
     *
     * @param map properties key map
     * @return yml map
     */
    @SuppressWarnings("unchecked")
    private synchronized Map<String, Object> mapToYmlMap(Map<String, String> map) {
        Map<String, Object> yml = new HashMap<>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
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
                Map<String, Object> childMap = mapToYmlMap((Map<String, String>) entry.getValue());
                yml.put(entry.getKey(), childMap);
            }
        }
        return yml;
    }
}

