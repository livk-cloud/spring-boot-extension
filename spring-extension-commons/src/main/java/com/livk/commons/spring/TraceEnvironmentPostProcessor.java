package com.livk.commons.spring;

import com.livk.auto.service.annotation.SpringFactories;
import com.livk.commons.bean.util.ClassUtils;
import org.springframework.boot.SpringApplication;
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
@SpringFactories(EnvironmentPostProcessor.class)
public class TraceEnvironmentPostProcessor implements EnvironmentPostProcessor {

    private static final String PROPERTY_SOURCE_NAME = "traceProperties";

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (ClassUtils.isPresent("io.micrometer.tracing.Tracer", classLoader) &&
            ClassUtils.isPresent("io.micrometer.tracing.otel.bridge.OtelTracer", classLoader)) {
            if (Boolean.parseBoolean(environment.getProperty("management.tracing.enabled", "true"))) {
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
}
