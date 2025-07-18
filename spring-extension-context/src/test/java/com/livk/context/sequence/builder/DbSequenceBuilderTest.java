/*
 * Copyright 2021-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * you may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.livk.context.sequence.builder;

import com.livk.context.sequence.Sequence;
import com.livk.context.sequence.support.DbRangeManager;
import com.zaxxer.hikari.HikariDataSource;
import org.h2.Driver;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DbSequenceBuilderTest {

	static HikariDataSource dataSource;

	@BeforeAll
	static void setupDataSource() {
		dataSource = new HikariDataSource();
		dataSource.setDriverClassName(Driver.class.getName());
		dataSource.setJdbcUrl("jdbc:h2:mem:test");
	}

	@AfterAll
	static void closeDataSource() {
		if (dataSource != null) {
			dataSource.close();
		}
	}

	@Test
	void testBuildSequenceWithAllRequiredParameters() {
		DbSequenceBuilder builder = new DbSequenceBuilder(dataSource).bizName("test-biz");
		Sequence sequence = builder.build();
		assertNotNull(sequence);
		for (int i = 0; i < 10; i++) {
			assertEquals(i + 1, sequence.nextValue());
		}
	}

	@Test
	void testCustomStepValueApplied() throws Exception {
		int customStep = 123;
		DbSequenceBuilder builder = new DbSequenceBuilder(dataSource).bizName("step-biz").step(customStep);
		Sequence sequence = builder.build();
		assertNotNull(sequence);

		// Use reflection to access the step value in DbRangeManager
		Field managerField = sequence.getClass().getDeclaredField("manager");
		managerField.setAccessible(true);
		Object manager = managerField.get(sequence);
		Field stepField = DbRangeManager.class.getSuperclass().getDeclaredField("step");
		stepField.setAccessible(true);
		int actualStep = (int) stepField.get(manager);
		assertEquals(customStep, actualStep);
	}

	@Test
	void testCustomStepStartValueApplied() throws Exception {
		long customStepStart = 55L;
		DbSequenceBuilder builder = new DbSequenceBuilder(dataSource).bizName("stepStart-biz")
			.stepStart(customStepStart);
		Sequence sequence = builder.build();
		assertNotNull(sequence);

		// Use reflection to access the stepStart value in DbRangeManager
		Field managerField = sequence.getClass().getDeclaredField("manager");
		managerField.setAccessible(true);
		Object manager = managerField.get(sequence);
		Field stepStartField = DbRangeManager.class.getSuperclass().getDeclaredField("stepStart");
		stepStartField.setAccessible(true);
		long actualStepStart = (long) stepStartField.get(manager);
		assertEquals(customStepStart, actualStepStart);
	}

	@Test
	void testBuildThrowsExceptionWhenBizNameMissing() {
		DbSequenceBuilder builder = new DbSequenceBuilder(dataSource);
		Exception exception = assertThrows(IllegalArgumentException.class, builder::build);
		assertTrue(exception.getMessage().contains("name is required"));
	}

	@Test
	void testNegativeStepValueIgnored() throws Exception {
		DbSequenceBuilder builder = new DbSequenceBuilder(dataSource).bizName("neg-step-biz").step(-100);
		Sequence sequence = builder.build();
		assertNotNull(sequence);

		// Should still use default step (1000)
		Field managerField = sequence.getClass().getDeclaredField("manager");
		managerField.setAccessible(true);
		Object manager = managerField.get(sequence);
		Field stepField = DbRangeManager.class.getSuperclass().getDeclaredField("step");
		stepField.setAccessible(true);
		int actualStep = (int) stepField.get(manager);
		assertEquals(1000, actualStep);
	}

	@Test
	void testNegativeStepStartValueIgnored() throws Exception {
		DbSequenceBuilder builder = new DbSequenceBuilder(dataSource).bizName("neg-stepStart-biz").stepStart(-99L);
		Sequence sequence = builder.build();
		assertNotNull(sequence);

		// Should still use default stepStart (0)
		Field managerField = sequence.getClass().getDeclaredField("manager");
		managerField.setAccessible(true);
		Object manager = managerField.get(sequence);
		Field stepStartField = DbRangeManager.class.getSuperclass().getDeclaredField("stepStart");
		stepStartField.setAccessible(true);
		long actualStepStart = (long) stepStartField.get(manager);
		assertEquals(0L, actualStepStart);
	}

}
