package com.livk.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.Banner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.AnnotationConfigurationException;
import org.springframework.util.Assert;

@Slf4j
public class LivkSpring {

    private LivkSpring() {
    }

    public static <T> ConfigurableApplicationContext runServlet(Class<T> targetClass, String[] args) {
        return LivkSpring.run(targetClass, args, WebApplicationType.SERVLET);
    }

    public static <T> ConfigurableApplicationContext runReactive(Class<T> targetClass, String[] args) {
        return LivkSpring.run(targetClass, args, WebApplicationType.REACTIVE);
    }

    private static <T> ConfigurableApplicationContext run(Class<T> targetClass, String[] args, WebApplicationType webApplicationType) {
        if (!targetClass.isAnnotationPresent(SpringBootApplication.class)) {
            throw new AnnotationConfigurationException("must use @SpringBootApplication in start class");
        }
        Assert.notNull(webApplicationType, "run web app type not null!");
        return new SpringApplicationBuilder(targetClass)
                .web(webApplicationType)
                .banner(LivkBanner.create())
                .bannerMode(Banner.Mode.CONSOLE)
                .run(args);
    }
}
