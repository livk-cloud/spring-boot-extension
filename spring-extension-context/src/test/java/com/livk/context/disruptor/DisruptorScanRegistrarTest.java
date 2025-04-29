package com.livk.context.disruptor;

import com.livk.context.disruptor.support.SpringDisruptor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.support.DefaultBeanNameGenerator;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.ResolvableType;
import org.springframework.core.type.AnnotationMetadata;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author livk
 */
class DisruptorScanRegistrarTest {

	@Test
	void registerBeanDefinitions() {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Config.class);

		ImportBeanDefinitionRegistrar registrar = new DisruptorScanRegistrar();

		AnnotationMetadata metadata = AnnotationMetadata.introspect(DisruptorConfig.class);
		registrar.registerBeanDefinitions(metadata, context, DefaultBeanNameGenerator.INSTANCE);

		ResolvableType type = ResolvableType.forClassWithGenerics(SpringDisruptor.class, Entity.class);
		assertEquals(1, context.getBeanProvider(type).stream().count());

	}

	@TestConfiguration
	static class Config {

	}

}
