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

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
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

	final Resource yml = new ClassPathResource("yamlData.yml");

	final Map<String, Object> map = Map.of("spring.redis.host", "livk.com", "spring.redis.port", 5672, "spring.env[0]",
			1, "spring.env[1]", 2);

	@Test
	void convertMapToYaml() throws IOException {
		Map<String, Object> load = new Yaml().load(yml.getInputStream());
		Map<String, Object> result = YamlUtils.convertMapToYaml(map);
		assertEquals(load, result);
	}

	@Test
	void convertYamlToMap() throws IOException {
		Map<String, Object> load = new Yaml().load(yml.getInputStream());
		Properties result = YamlUtils.convertYamlToMap(load);
		assertEquals(map, result);
	}

}
