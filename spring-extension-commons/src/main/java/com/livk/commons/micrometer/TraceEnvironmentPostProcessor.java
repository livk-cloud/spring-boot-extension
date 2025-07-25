/*
 * Copyright 2021-present the original author or authors.
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
 */

package com.livk.commons.micrometer;

import com.livk.auto.service.annotation.SpringFactories;
import com.livk.commons.util.ClassUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 用于设置trace日志格式
 * </p>
 *
 * @author livk
 */
@SpringFactories
public class TraceEnvironmentPostProcessor implements EnvironmentPostProcessor {

	private static final String PROPERTY_SOURCE_NAME = "traceProperties";

	private static final String PROBABILITY_KEY = "management.tracing.sampling.probability";

	private static final String LEVEL_KEY = "logging.pattern.level";

	@Override
	public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
		if (isServletApplication(application) && isTracingEnabled(environment)) {
			Map<String, Object> traceProperties = new HashMap<>();
			// Ensure default probability is set if not already configured
			setDefaultProbabilityIfNeeded(environment, traceProperties);
			// Ensure default level is set if not already configured
			setDefaultLevelIfNeeded(environment, traceProperties);
			// Add trace properties to environment if not already added
			addTracePropertiesToEnvironment(environment, traceProperties);
		}
	}

	private boolean isServletApplication(SpringApplication application) {
		return application.getWebApplicationType() == WebApplicationType.SERVLET;
	}

	private boolean isTracingEnabled(ConfigurableEnvironment environment) {
		return checkDependency() && environment.getProperty("management.tracing.enabled", Boolean.class, true);
	}

	private void setDefaultProbabilityIfNeeded(ConfigurableEnvironment environment,
			Map<String, Object> traceProperties) {
		if (!environment.containsProperty(PROBABILITY_KEY)) {
			traceProperties.put(PROBABILITY_KEY, 1.0);
		}
	}

	private void setDefaultLevelIfNeeded(ConfigurableEnvironment environment, Map<String, Object> traceProperties) {
		if (!environment.containsProperty(LEVEL_KEY)) {
			String applicationName = environment.getProperty("spring.application.name");
			String levelPattern = StringUtils.hasText(applicationName)
					? "%5p [${spring.application.name:},%X{traceId:-},%X{spanId:-}]"
					: "%5p [%X{traceId:-},%X{spanId:-}]";
			traceProperties.put(LEVEL_KEY, levelPattern);
		}
	}

	private void addTracePropertiesToEnvironment(ConfigurableEnvironment environment,
			Map<String, Object> traceProperties) {
		if (!traceProperties.isEmpty()) {
			MutablePropertySources propertySources = environment.getPropertySources();
			if (!propertySources.contains(PROPERTY_SOURCE_NAME)) {
				propertySources.addLast(new MapPropertySource(PROPERTY_SOURCE_NAME, traceProperties));
			}
		}
	}

	private boolean checkDependency() {
		return ClassUtils
			.isPresent("org.springframework.boot.actuate.autoconfigure.tracing.OpenTelemetryAutoConfiguration")
				&& ClassUtils.isPresent("io.micrometer.tracing.Tracer")
				&& ClassUtils.isPresent("io.micrometer.tracing.otel.bridge.OtelTracer");
	}

}
