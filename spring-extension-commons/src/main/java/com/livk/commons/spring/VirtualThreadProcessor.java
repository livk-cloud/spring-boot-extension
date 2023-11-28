/*
 * Copyright 2021 spring-boot-extension the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.livk.commons.spring;

import com.livk.auto.service.annotation.SpringFactories;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;

import java.util.Map;

/**
 * <p>
 * 设置默认启用虚拟线程
 * </p>
 *
 * @author livk
 */
@SpringFactories
public class VirtualThreadProcessor implements EnvironmentPostProcessor {

	private static final String PROPERTY_SOURCE_NAME = "virtualProperties";

	@Override
	public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
		if (!environment.containsProperty("spring.threads.virtual.enabled")) {
			Map<String, Object> map = Map.of("spring.threads.virtual.enabled", true);
			MutablePropertySources propertySources = environment.getPropertySources();
			if (!propertySources.contains(PROPERTY_SOURCE_NAME)) {
				MapPropertySource target = new MapPropertySource(PROPERTY_SOURCE_NAME, map);
				propertySources.addLast(target);
			}
		}
	}
}
