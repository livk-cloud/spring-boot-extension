package com.livk.context.http.factory;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author livk
 */
@SpringJUnitConfig(classes = WebClientAdapterFactoryTest.Config.class)
class WebClientAdapterFactoryTest {

	@Autowired
	BeanFactory beanFactory;

	static final WebClientAdapterFactory factory = new WebClientAdapterFactory();

	@Test
	void support() {
		assertTrue(factory.support());
	}

	@Test
	void create() {
		assertNotNull(factory.create(beanFactory));
	}

	@Test
	void type() {
		assertEquals(AdapterType.WEB_CLIENT, factory.type());
	}

	@TestConfiguration
	static class Config {

	}

}
