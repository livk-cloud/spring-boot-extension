/*
 * Copyright 2021-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.livk.context.sequence.support;

import com.livk.context.sequence.SequenceRange;
import com.livk.context.sequence.exception.SequenceException;
import com.zaxxer.hikari.HikariDataSource;
import org.h2.Driver;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DbRangeManagerTests {

	static HikariDataSource dataSource;

	@BeforeAll
	static void setupDataSource() {
		dataSource = new HikariDataSource();
		dataSource.setDriverClassName(Driver.class.getName());
		dataSource.setJdbcUrl("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
	}

	@AfterAll
	static void closeDataSource() {
		if (dataSource != null) {
			dataSource.close();
		}
	}

	@Test
	void testNextRangeReturnsValidSequenceRange() {
		DbRangeManager manager = new DbRangeManager(dataSource);
		manager.init();
		String name = "test-seq";
		SequenceRange range = manager.nextRange(name);
		assertNotNull(range);
		assertEquals(1, range.getMin());
		assertEquals(1000, range.getMax());
		assertFalse(range.isOver());
		assertEquals(1, range.getAndIncrement());
		assertEquals(2, range.getAndIncrement());
	}

	@Test
	void testInitCreatesTableSuccessfully() {
		DbRangeManager manager = new DbRangeManager(dataSource);
		assertDoesNotThrow(manager::init);
		// Try to allocate a range to ensure table exists and is usable
		SequenceRange range = manager.nextRange("init-table-seq");
		assertNotNull(range);
	}

	@Test
	void testNextRangeSucceedsAfterRetries() throws Exception {
		DbRangeManager manager = new DbRangeManager(dataSource);
		manager.init();
		String name = "retry-seq";

		SequenceRange firstRange = manager.nextRange(name);
		assertNotNull(firstRange);

		Field helperField = DbRangeManager.class.getDeclaredField("helper");
		helperField.setAccessible(true);
		SequenceDbHelper helper = (SequenceDbHelper) helperField.get(manager);
		assertNotNull(helper);

		DbRangeManager concurrentManager = new DbRangeManager(dataSource);
		SequenceRange concurrentRange = concurrentManager.nextRange(name);
		assertNotNull(concurrentRange);

		SequenceRange retriedRange = manager.nextRange(name);
		assertNotNull(retriedRange);
		assertEquals(concurrentRange.getMax() + 1, retriedRange.getMin());
	}

	@Test
	void testNextRangeThrowsOnEmptyName() {
		DbRangeManager manager = new DbRangeManager(dataSource);
		manager.init();
		assertThrows(SequenceException.class, () -> manager.nextRange(""));
		assertThrows(SequenceException.class, () -> manager.nextRange(null));
	}

	@Test
	void testNextRangeThrowsAfterMaxRetries() throws Exception {
		// We'll create a subclass that always fails updateRange to simulate retry
		// exhaustion
		class FailingDbRangeManager extends DbRangeManager {

			public FailingDbRangeManager(DataSource ds) {
				super(ds);
			}

			@Override
			public SequenceRange nextRange(String name) {
				// Use reflection to set step to 1 for faster test
				try {
					Field stepField = AbstractRangeManager.class.getDeclaredField("step");
					stepField.setAccessible(true);
					stepField.setInt(this, 1);
				}
				catch (Exception ignored) {
				}
				return super.nextRange(name);
			}

		}
		FailingDbRangeManager manager = new FailingDbRangeManager(dataSource);
		manager.init();

		// Use reflection to replace helper with one that always returns true for
		// selectRange but false for updateRange
		Field helperField = DbRangeManager.class.getDeclaredField("helper");
		helperField.setAccessible(true);
		SequenceDbHelper realHelper = (SequenceDbHelper) helperField.get(manager);

		SequenceDbHelper failingHelper = new SequenceDbHelper(dataSource, "sequence_range") {
			@Override
			public Long selectRange(String name, long stepStart) {
				return 0L;
			}

			@Override
			public boolean updateRange(String name, long newValue, long oldValue) {
				return false;
			}
		};
		helperField.set(manager, failingHelper);

		assertThrows(SequenceException.class, () -> manager.nextRange("fail-retry-seq"));
	}

	@Test
	void testNextRangeHandlesPersistentNullFromHelper() throws Exception {
		// We'll create a subclass that always returns null from selectRange to simulate
		// persistent DB failure
		class NullReturningDbRangeManager extends DbRangeManager {

			public NullReturningDbRangeManager(DataSource ds) {
				super(ds);
			}

		}
		NullReturningDbRangeManager manager = new NullReturningDbRangeManager(dataSource);
		manager.init();

		// Use reflection to replace helper with one that always returns null for
		// selectRange
		Field helperField = DbRangeManager.class.getDeclaredField("helper");
		helperField.setAccessible(true);
		SequenceDbHelper nullHelper = new SequenceDbHelper(dataSource, "sequence_range") {
			@Override
			public Long selectRange(String name, long stepStart) {
				return null;
			}
		};
		helperField.set(manager, nullHelper);

		assertThrows(SequenceException.class, () -> manager.nextRange("null-helper-seq"));
	}

}
