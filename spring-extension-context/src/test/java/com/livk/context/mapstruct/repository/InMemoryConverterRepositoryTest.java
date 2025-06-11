package com.livk.context.mapstruct.repository;

import com.livk.context.mapstruct.IntConverter;
import com.livk.context.mapstruct.LongConverter;
import com.livk.context.mapstruct.converter.Converter;
import com.livk.context.mapstruct.converter.ConverterPair;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author livk
 */
class InMemoryConverterRepositoryTest {

	@Test
	void test() {
		InMemoryConverterRepository repository = new InMemoryConverterRepository();
		IntConverter intConverter = new IntConverter();
		LongConverter longConverter = new LongConverter();
		repository.put(ConverterPair.of(Integer.class, String.class), intConverter);
		Converter<Long, String> result = repository.computeIfAbsent(ConverterPair.of(Long.class, String.class),
				longConverter);

		assertNotNull(result);

		assertTrue(repository.contains(ConverterPair.of(Integer.class, String.class)));
		assertTrue(repository.contains(Integer.class, String.class));
		assertFalse(repository.contains(ConverterPair.of(String.class, Integer.class)));
		assertFalse(repository.contains(String.class, Integer.class));

		assertTrue(repository.contains(ConverterPair.of(Long.class, String.class)));
		assertTrue(repository.contains(Long.class, String.class));
		assertFalse(repository.contains(ConverterPair.of(String.class, Long.class)));
		assertFalse(repository.contains(String.class, Long.class));

		assertEquals(intConverter, repository.<Integer, String>get(ConverterPair.of(Integer.class, String.class)));
		assertEquals(longConverter, repository.<Long, String>get(ConverterPair.of(Long.class, String.class)));
	}

}
