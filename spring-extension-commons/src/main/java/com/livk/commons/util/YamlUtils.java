/*
 * Copyright 2021-2024 spring-boot-extension the original author or authors.
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
 */

package com.livk.commons.util;

import com.google.common.base.CharMatcher;
import lombok.experimental.UtilityClass;
import org.jspecify.annotations.Nullable;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.yaml.snakeyaml.Yaml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * <p>
 * YAML相关工具类
 * </p>
 *
 * @author livk
 * @see Yaml
 */
@UtilityClass
public class YamlUtils {

	/**
	 * properties map转 yaml
	 * <p>
	 * {example Map.of(" a.b.c ", " 1 ") -> YAML}
	 * @param map properties key map
	 * @return yml str
	 */
	public String toYml(Map<?, ?> map) {
		if (CollectionUtils.isEmpty(map)) {
			return "";
		}
		Map<String, Object> yamlMap = convertMapToYaml(map);
		return new Yaml().dumpAsMap(yamlMap);
	}

	/**
	 * properties map转 yaml map
	 * <p>
	 * example Map.of(" a.b.c ", " 1 ") -> Map.of("a",Map.of("b",Map.of("c","1")))
	 * @param map properties key map
	 * @return yml map
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> convertMapToYaml(Map<?, ?> map) {
		Map<String, Object> yamlMap = new LinkedHashMap<>();
		for (Map.Entry<String, Object> entry : envTransform(map).entrySet()) {
			String[] keys = entry.getKey().split("\\.");
			Map<String, Object> tempMap = yamlMap;
			for (int i = 0; i < keys.length - 1; i++) {
				String key = keys[i];
				if (key.contains("[")) {
					int index = index(key);
					key = key.substring(0, key.indexOf('['));
					tempMap.putIfAbsent(key, new ArrayList<>());
					List<Object> list = (List<Object>) tempMap.get(key);
					while (list.size() <= index) {
						list.add(new LinkedHashMap<>());
					}
					tempMap = (Map<String, Object>) list.get(index);
				}
				else {
					tempMap.putIfAbsent(key, new LinkedHashMap<>());
					tempMap = (Map<String, Object>) tempMap.get(key);
				}
			}
			String lastKey = keys[keys.length - 1];
			if (lastKey.contains("[")) {
				int index = index(lastKey);
				lastKey = lastKey.substring(0, lastKey.indexOf('['));
				tempMap.putIfAbsent(lastKey, new ArrayList<>());
				List<Object> list = (List<Object>) tempMap.get(lastKey);
				while (list.size() <= index) {
					list.add(null);
				}
				list.set(index, entry.getValue());
			}
			else {
				tempMap.put(keys[keys.length - 1], entry.getValue());
			}
		}
		return yamlMap;
	}

	private static Map<String, Object> envTransform(Map<?, ?> map) {
		Map<String, Object> result = new HashMap<>();
		for (Map.Entry<?, ?> entry : map.entrySet()) {
			StringBuilder builder = new StringBuilder();
			String[] keys = entry.getKey().toString().split("\\.");
			for (String key : keys) {
				if (CharMatcher.inRange('0', '9').matchesAllOf(key)) {
					builder.append("[").append(key).append("]");
				}
				else {
					builder.append(".").append(key);
				}
			}
			builder.deleteCharAt(0);
			result.put(builder.toString(), entry.getValue());
		}
		return result;
	}

	private static Integer index(String key) {
		return Integer.parseInt(key.substring(key.indexOf('[') + 1, key.indexOf(']')));
	}

	/**
	 * yaml map转 properties map example Map.of("a",Map.of("b",Map.of("c","1"))) ->
	 * Map.of(" a.b.c ", " 1 ")
	 * <p>
	 * {@see org.springframework.beans.factory.config.YamlProcessor#getFlattenedMap(java.util.Map)}
	 * @param source the map
	 * @return the map
	 */
	public static Properties convertYamlToMap(Map<String, Object> source) {
		Properties result = new Properties();
		buildFlattenedMap(result, source, null);
		return result;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void buildFlattenedMap(Properties result, Map<String, Object> source, @Nullable String path) {
		source.forEach((key, value) -> {
			if (StringUtils.hasText(path)) {
				if (key.startsWith("[")) {
					key = path + key;
				}
				else {
					key = path + '.' + key;
				}
			}
			switch (value) {
				case String s -> result.put(key, s);
				case Map map ->
					// Need a compound key
					buildFlattenedMap(result, map, key);
				case Collection collection -> {
					// Need a compound key
					if (collection.isEmpty()) {
						result.put(key, "");
					}
					else {
						int count = 0;
						for (Object object : collection) {
							buildFlattenedMap(result, Collections.singletonMap("[" + (count++) + "]", object), key);
						}
					}
				}
				case null, default -> result.put(key, (value != null ? value : ""));
			}
		});
	}

}
