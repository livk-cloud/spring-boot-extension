package com.livk.context.mapstruct.converter;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author livk
 */
class ConverterPairTest {

	@Test
	void of() {
		ConverterPair pair = ConverterPair.of(String.class, Object.class);

		assertEquals(String.class, pair.getSourceType());
		assertEquals(Object.class, pair.getTargetType());

		ConverterPair converterPair = ConverterPair.of(new TestConverter());

		assertNotNull(converterPair);
		assertEquals(String.class, converterPair.getSourceType());
		assertEquals(Object.class, converterPair.getTargetType());
	}

	static class TestConverter implements Converter<String, Object> {

		@Override
		public String getSource(Object o) {
			return o.toString();
		}

		@Override
		public Object getTarget(String s) {
			return s;
		}

	}

}
