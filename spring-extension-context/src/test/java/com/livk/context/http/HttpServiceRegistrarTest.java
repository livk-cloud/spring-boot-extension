package com.livk.context.http;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author livk
 */
class HttpServiceRegistrarTest {

	@Test
	void registerBeanDefinitions() {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Config.class);

		ImportBeanDefinitionRegistrar registrar = new HttpServiceRegistrar();

		AnnotationMetadata metadata = AnnotationMetadata.introspect(HttpConfig.class);
		registrar.registerBeanDefinitions(metadata, context);

		assertEquals(1, context.getBeanProvider(HttpService.class).stream().count());
	}

	@TestConfiguration
	static class Config {

	}

}
