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
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.yaml.snakeyaml.Yaml;

import java.util.*;

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
		Map<String, Object> yamlMap = convertMapToYaml(map);
		return YAML.dumpAsMap(yamlMap);
	}

	/**
	 * example Map.of(" a.b.c ", " 1 ") -> Map.of("a",Map.of("b",Map.of("c","1")))
	 *
	 * @param map properties key map
	 * @return yml map
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> convertMapToYaml(Map<?, ?> map) {
		Map<String, Object> yamlMap = new LinkedHashMap<>();
		for (Map.Entry<?, ?> entry : map.entrySet()) {
			String[] keys = entry.getKey().toString().split("\\.");
			Map<String, Object> tempMap = yamlMap;
			for (int i = 0; i < keys.length - 1; i++) {
				String key = keys[i];
				if (key.contains("[")) {
					int index = index(key);
					key = key.substring(0, key.indexOf("["));
					tempMap.putIfAbsent(key, new ArrayList<>());
					List<Object> list = (List<Object>) tempMap.get(key);
					while (list.size() <= index) {
						list.add(new LinkedHashMap<>());
					}
					tempMap = (Map<String, Object>) list.get(index);
				} else {
					tempMap.putIfAbsent(key, new LinkedHashMap<>());
					tempMap = (Map<String, Object>) tempMap.get(key);
				}
			}
			String lastKey = keys[keys.length - 1];
			if (lastKey.contains("[")) {
				int index = index(lastKey);
				lastKey = lastKey.substring(0, lastKey.indexOf("["));
				tempMap.putIfAbsent(lastKey, new ArrayList<>());
				List<Object> list = (List<Object>) tempMap.get(lastKey);
				while (list.size() <= index) {
					list.add(null);
				}
				list.set(index, entry.getValue());
			} else {
				tempMap.put(keys[keys.length - 1], entry.getValue());
			}
		}
		return yamlMap;
	}

	private static Integer index(String key) {
		return Integer.parseInt(key.substring(key.indexOf("[") + 1, key.indexOf("]")));
	}

	/**
	 * example Map.of("a",Map.of("b",Map.of("c","1"))) -> Map.of(" a.b.c ", " 1 ")
	 * <p>
	 * {@see org.springframework.beans.factory.config.YamlProcessor#getFlattenedMap(java.util.Map)}
	 *
	 * @param source the map
	 * @return the map
	 */
	public static Properties convertYamlToMap(Map<String, Object> source) {
		Properties result = new Properties();
		buildFlattenedMap(result, source, null);
		return result;
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	private void buildFlattenedMap(Properties result, Map<String, Object> source, @Nullable String path) {
		source.forEach((key, value) -> {
			if (StringUtils.hasText(path)) {
				if (key.startsWith("[")) {
					key = path + key;
				} else {
					key = path + '.' + key;
				}
			}
			if (value instanceof String) {
				result.put(key, value);
			} else if (value instanceof Map map) {
				// Need a compound key
				buildFlattenedMap(result, map, key);
			} else if (value instanceof Collection collection) {
				// Need a compound key
				if (collection.isEmpty()) {
					result.put(key, "");
				} else {
					int count = 0;
					for (Object object : collection) {
						buildFlattenedMap(result, Collections.singletonMap(
							"[" + (count++) + "]", object), key);
					}
				}
			} else {
				result.put(key, (value != null ? value : ""));
			}
		});
	}
}

