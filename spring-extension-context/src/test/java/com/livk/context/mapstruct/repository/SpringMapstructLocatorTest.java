package com.livk.context.mapstruct.repository;

import com.livk.context.mapstruct.IntConverter;
import com.livk.context.mapstruct.LongConverter;
import com.livk.context.mapstruct.converter.ConverterPair;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author livk
 */
@SpringJUnitConfig(classes = SpringMapstructLocatorTest.Config.class)
class SpringMapstructLocatorTest {

	@Autowired
	MapstructLocator mapstructLocator;

	@Test
	void test() {
		assertNotNull(mapstructLocator.get(ConverterPair.of(Integer.class, String.class)));
		assertNotNull(mapstructLocator.get(ConverterPair.of(Long.class, String.class)));
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

	}

}
