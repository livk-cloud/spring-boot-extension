package com.livk.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.AnnotationConfigurationException;

@Slf4j
public class LivkSpring {

	private LivkSpring() {
	}

	public static <T> ConfigurableApplicationContext run(Class<T> targetClass, String[] args) {
		if (!targetClass.isAnnotationPresent(SpringBootApplication.class)) {
			throw new AnnotationConfigurationException("must use @SpringBootApplication in start class");
		}
		return new SpringApplicationBuilder(targetClass).banner(LivkBanner.create()).bannerMode(Banner.Mode.CONSOLE)
				.run(args);
	}

}
