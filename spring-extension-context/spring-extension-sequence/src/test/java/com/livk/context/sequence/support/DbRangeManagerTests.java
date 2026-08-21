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
import org.springframework.dao.DuplicateKeyException;

import javax.sql.DataSource;

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
		String name = "test-seq";
		SequenceRange range = manager.nextRange(name, 1000, 0);
		assertThat(range).isNotNull();
		assertThat(range.getMin()).isEqualTo(1);
		assertThat(range.getMax()).isEqualTo(1000);
		assertThat(range.isOver()).isFalse();
		assertThat(range.next()).isEqualTo(1);
		assertThat(range.next()).isEqualTo(2);
	}

	@Test
	void testInitCreatesTableSuccessfully() {
		assertThatCode(() -> new DbRangeManager(dataSource)).doesNotThrowAnyException();

		SequenceRange range = new DbRangeManager(dataSource).nextRange("init-table-seq", 1000, 0);
		assertThat(range).isNotNull();
	}

	@Test
	void testNextRangeSucceedsAfterRetries() {
		DbRangeManager manager = new DbRangeManager(dataSource);
		String name = "retry-seq";

		SequenceRange firstRange = manager.nextRange(name, 1000, 0);
		assertThat(firstRange).isNotNull();

		DbRangeManager concurrentManager = new DbRangeManager(dataSource);
		SequenceRange concurrentRange = concurrentManager.nextRange(name, 1000, 0);
		assertThat(concurrentRange).isNotNull();

		SequenceRange retriedRange = manager.nextRange(name, 1000, 0);
		assertThat(retriedRange).isNotNull();
		assertThat(retriedRange.getMin()).isEqualTo(concurrentRange.getMax() + 1);
	}

	@Test
	void testNextRangeRetriesWhenConcurrentInsertWins() {
		class DuplicateKeyOnceDbRangeManager extends DbRangeManager {

			private boolean duplicateKeyThrown;

			DuplicateKeyOnceDbRangeManager(DataSource ds) {
				super(ds);
			}

			@Override
			protected void insertRange(String name, long stepStart) {
				super.insertRange(name, stepStart);
				if (!duplicateKeyThrown) {
					duplicateKeyThrown = true;
					throw new DuplicateKeyException("concurrent insert");
				}
			}

		}
		DuplicateKeyOnceDbRangeManager manager = new DuplicateKeyOnceDbRangeManager(dataSource);

		SequenceRange range = manager.nextRange("duplicate-key-retry-seq", 1000, 0);

		assertThat(range.getMin()).isEqualTo(1);
		assertThat(range.getMax()).isEqualTo(1000);
	}

	@Test
	void testNextRangeThrowsOnEmptyName() {
		DbRangeManager manager = new DbRangeManager(dataSource);

		assertThatThrownBy(() -> manager.nextRange("", 1000, 0)).isInstanceOf(SequenceException.class);

		assertThatThrownBy(() -> manager.nextRange(null, 1000, 0)).isInstanceOf(SequenceException.class);

	}

	@Test
	void testNextRangeThrowsAfterMaxRetries() {
		// We'll create a subclass that always fails updateRange to simulate retry
		// exhaustion
		class FailingDbRangeManager extends DbRangeManager {

			FailingDbRangeManager(DataSource ds) {
				super(ds);
			}

			@Override
			protected Long selectRange(String name, long stepStart) {
				return 0L;
			}

			@Override
			protected boolean updateRange(String name, long newValue, long oldValue) {
				return false;
			}

		}
		FailingDbRangeManager manager = new FailingDbRangeManager(dataSource);

		assertThatThrownBy(() -> manager.nextRange("fail-retry-seq", 1, 0)).isInstanceOf(SequenceException.class);
	}

	@Test
	void testNextRangeHandlesPersistentNullFromHelper() {
		// We'll create a subclass that always returns null from selectRange to simulate
		// persistent DB failure
		class NullReturningDbRangeManager extends DbRangeManager {

			NullReturningDbRangeManager(DataSource ds) {
				super(ds);
			}

			@Override
			protected Long selectRange(String name, long stepStart) {
				return null;
			}

		}
		NullReturningDbRangeManager manager = new NullReturningDbRangeManager(dataSource);

		assertThatThrownBy(() -> manager.nextRange("null-helper-seq", 1000, 0)).isInstanceOf(SequenceException.class);
	}

}
