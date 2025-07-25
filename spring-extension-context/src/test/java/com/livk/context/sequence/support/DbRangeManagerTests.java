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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DbRangeManagerTests {

	static HikariDataSource dataSource;

	@BeforeAll
	static void setupDataSource() {
		dataSource = new HikariDataSource();
		dataSource.setDriverClassName(Driver.class.getName());
		dataSource.setJdbcUrl("jdbc:h2:mem:test_db;DB_CLOSE_DELAY=-1");
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
		assertThat(range).isNotNull();
		assertThat(range.getMin()).isEqualTo(1);
		assertThat(range.getMax()).isEqualTo(1000);
		assertThat(range.isOver()).isFalse();
		assertThat(range.getAndIncrement()).isEqualTo(1);
		assertThat(range.getAndIncrement()).isEqualTo(2);
	}

	@Test
	void testInitCreatesTableSuccessfully() {
		DbRangeManager manager = new DbRangeManager(dataSource);
		assertThatCode(manager::init).doesNotThrowAnyException();

		SequenceRange range = manager.nextRange("init-table-seq");
		assertThat(range).isNotNull();
	}

	@Test
	void testNextRangeSucceedsAfterRetries() throws Exception {
		DbRangeManager manager = new DbRangeManager(dataSource);
		manager.init();
		String name = "retry-seq";

		SequenceRange firstRange = manager.nextRange(name);
		assertThat(firstRange).isNotNull();

		Field helperField = DbRangeManager.class.getDeclaredField("helper");
		helperField.setAccessible(true);
		SequenceDbHelper helper = (SequenceDbHelper) helperField.get(manager);
		assertThat(helper).isNotNull();

		DbRangeManager concurrentManager = new DbRangeManager(dataSource);
		SequenceRange concurrentRange = concurrentManager.nextRange(name);
		assertThat(concurrentRange).isNotNull();

		SequenceRange retriedRange = manager.nextRange(name);
		assertThat(retriedRange).isNotNull();
		assertThat(retriedRange.getMin()).isEqualTo(concurrentRange.getMax() + 1);
	}

	@Test
	void testNextRangeThrowsOnEmptyName() {
		DbRangeManager manager = new DbRangeManager(dataSource);
		manager.init();

		assertThatThrownBy(() -> manager.nextRange("")).isInstanceOf(SequenceException.class);

		assertThatThrownBy(() -> manager.nextRange(null)).isInstanceOf(SequenceException.class);

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

		assertThatThrownBy(() -> manager.nextRange("fail-retry-seq")).isInstanceOf(SequenceException.class);
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

		assertThatThrownBy(() -> manager.nextRange("null-helper-seq")).isInstanceOf(SequenceException.class);
	}

}
