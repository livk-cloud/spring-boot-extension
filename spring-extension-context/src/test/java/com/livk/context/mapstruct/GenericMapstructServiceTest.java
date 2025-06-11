package com.livk.context.mapstruct;

import com.livk.context.mapstruct.converter.Converter;
import com.livk.context.mapstruct.repository.InMemoryConverterRepository;
import com.livk.context.mapstruct.repository.SpringMapstructLocator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author livk
 */
@SpringJUnitConfig(classes = GenericMapstructServiceTest.Config.class)
class GenericMapstructServiceTest {

	@Autowired
	private GenericMapstructService service;

	@Test
	void test() {
		DoubleConverter converter = new DoubleConverter();

		assertEquals(converter, service.addConverter(converter));

		assertEquals(123, service.convert("123", Integer.class));
		assertEquals(123L, service.convert("123", Long.class));
		assertEquals(123.1, service.convert("123.1", Double.class));

		assertEquals("123", service.convert(123, String.class));
		assertEquals("123", service.convert(123L, String.class));
		assertEquals("123.1", service.convert(123.1, String.class));
	}

	@TestConfiguration
	static class Config {

		@Bean
		public IntConverter intConverter() {
			return new IntConverter();
		}

		@Bean
		public LongConverter longConverter() {
			return new LongConverter();
		}

		@Bean
		public SpringMapstructLocator springMapstructLocator() {
			return new SpringMapstructLocator();
		}

		@Bean
		public GenericMapstructService mapstructService() {
			return new GenericMapstructService(new InMemoryConverterRepository());
		}

	}

	static class DoubleConverter implements Converter<Double, String> {

		@Override
		public Double getSource(String s) {
			return Double.parseDouble(s);
		}

		@Override
		public String getTarget(Double d) {
			return d.toString();
		}

	}

}
