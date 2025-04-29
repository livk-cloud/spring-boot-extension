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
@SpringJUnitConfig(classes = RestClientAdapterFactoryTest.Config.class)
class RestClientAdapterFactoryTest {

	@Autowired
	BeanFactory beanFactory;

	static RestClientAdapterFactory factory = new RestClientAdapterFactory();

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
		assertEquals(AdapterType.REST_CLIENT, factory.type());
	}

	@TestConfiguration
	static class Config {

	}

}
