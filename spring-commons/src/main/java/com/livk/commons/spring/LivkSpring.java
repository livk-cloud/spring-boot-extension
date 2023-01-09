package com.livk.commons.spring;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.AnnotationConfigurationException;

/**
 * The type Livk spring.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LivkSpring {

    private static SpringApplication application;

    /**
     * Run configurable application context.
     *
     * @param <T>         the type parameter
     * @param targetClass the target class
     * @param args        the args
     * @return the configurable application context
     */
    public static <T> ConfigurableApplicationContext run(Class<T> targetClass, String[] args) {
        if (!targetClass.isAnnotationPresent(SpringBootApplication.class)) {
            throw new AnnotationConfigurationException("must use @SpringBootApplication in start class");
        }
        SpringApplicationBuilder builder = new SpringApplicationBuilder(targetClass)
                .banner(LivkBanner.create())
                .bannerMode(Banner.Mode.CONSOLE);
        application = builder.application();
        return builder.run(args);
    }

    /**
     * Application spring application.
     *
     * @return the spring application
     */
    public static SpringApplication application() {
        return application;
    }

}
