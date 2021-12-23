package com.livk.common;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.Banner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@Slf4j
public class LivkSpring {

    private static final String HTTP_PREFIX = "IP Address: http";

    private LivkSpring() {
    }

    public static <T> ConfigurableApplicationContext runServlet(Class<T> targetClass, String[] args) {
        return LivkSpring.run(targetClass, args, WebApplicationType.SERVLET);
    }

    public static <T> ConfigurableApplicationContext runReactive(Class<T> targetClass, String[] args) {
        return LivkSpring.run(targetClass, args, WebApplicationType.REACTIVE);
    }

    @SneakyThrows
    private static <T> ConfigurableApplicationContext run(Class<T> targetClass, String[] args, WebApplicationType webApplicationType) {
        return new SpringApplicationBuilder(targetClass)
                .web(webApplicationType)
                .banner(LivkBanner.create())
                .bannerMode(Banner.Mode.CONSOLE)
                .run(args);
    }
}
