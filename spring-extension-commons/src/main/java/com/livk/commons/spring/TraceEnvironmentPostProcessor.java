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

package com.livk.commons.spring;

import com.livk.auto.service.annotation.SpringFactories;
import com.livk.commons.util.ClassUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * TraceEnvironmentPostProcessor
 * </p>
 *
 * @author livk
 */
@SpringFactories
public class TraceEnvironmentPostProcessor implements EnvironmentPostProcessor {

	private static final String PROPERTY_SOURCE_NAME = "traceProperties";

	@Override
	public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
		if (application.getWebApplicationType() == WebApplicationType.SERVLET) {
			if (isPresent() && Boolean.parseBoolean(environment.getProperty("management.tracing.enabled", "true"))) {
				Map<String, Object> map = new HashMap<>();
				if (!environment.containsProperty("management.tracing.sampling.probability")) {
					map.put("management.tracing.sampling.probability", 1.0);
				}
				if (!environment.containsProperty("logging.pattern.level")) {
					map.put("logging.pattern.level", "%5p [${spring.application.name:},%X{traceId:-},%X{spanId:-}]");
				}
				MutablePropertySources propertySources = environment.getPropertySources();
				if (!propertySources.contains(PROPERTY_SOURCE_NAME) && !map.isEmpty()) {
					MapPropertySource target = new MapPropertySource(PROPERTY_SOURCE_NAME, map);
					propertySources.addLast(target);
				}
			}
		}
	}

	private boolean isPresent() {
		return ClassUtils.isPresent("org.springframework.boot.actuate.autoconfigure.tracing.OpenTelemetryAutoConfiguration")
			&& ClassUtils.isPresent("io.micrometer.tracing.Tracer")
			&& ClassUtils.isPresent("io.micrometer.tracing.otel.bridge.OtelTracer");
	}
}
