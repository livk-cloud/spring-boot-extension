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

import com.livk.context.sequence.exception.SequenceException;
import com.zaxxer.hikari.HikariDataSource;
import org.h2.Driver;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.Statement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SequenceDbHelperTests {

	static HikariDataSource dataSource;

	@BeforeAll
	static void setupDataSource() {
		dataSource = new HikariDataSource();
		dataSource.setDriverClassName(Driver.class.getName());
		dataSource.setJdbcUrl("jdbc:h2:mem:seqdb;DB_CLOSE_DELAY=-1");
	}

	@AfterAll
	static void closeDataSource() {
		if (dataSource != null) {
			dataSource.close();
		}
	}

	@Test
	void testCreateTableSuccess() throws Exception {
		String tableName = "seq_table_create";
		SequenceDbHelper helper = new SequenceDbHelper(dataSource, tableName);
		assertThatCode(helper::createTable).doesNotThrowAnyException();

		try (Connection conn = dataSource.getConnection(); Statement stmt = conn.createStatement()) {
			int updated = stmt.executeUpdate("INSERT INTO " + tableName
					+ " (name, val, create_time, update_time) VALUES ('foo', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)");
			assertThat(updated).isEqualTo(1);
		}
	}

	@Test
	void testSelectRangeReturnsExistingValue() throws Exception {
		String tableName = "seq_table_select";
		SequenceDbHelper helper = new SequenceDbHelper(dataSource, tableName);
		helper.createTable();

		try (Connection conn = dataSource.getConnection(); Statement stmt = conn.createStatement()) {
			stmt.executeUpdate("INSERT INTO " + tableName
					+ " (name, val, create_time, update_time) VALUES ('bar', 42, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)");
		}

		Long value = helper.selectRange("bar", 1L);
		assertThat(value).isNotNull().isEqualTo(42L);
	}

	@Test
	void testUpdateRangeSuccess() throws Exception {
		String tableName = "seq_table_update";
		SequenceDbHelper helper = new SequenceDbHelper(dataSource, tableName);
		helper.createTable();

		try (Connection conn = dataSource.getConnection(); Statement stmt = conn.createStatement()) {
			stmt.executeUpdate("INSERT INTO " + tableName
					+ " (name, val, create_time, update_time) VALUES ('baz', 100, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)");
		}

		boolean updated = helper.updateRange("baz", 200L, 100L);
		assertThat(updated).isTrue();

		try (Connection conn = dataSource.getConnection();
				Statement stmt = conn.createStatement();
				var rs = stmt.executeQuery("SELECT val FROM " + tableName + " WHERE name = 'baz'")) {
			assertThat(rs.next()).isTrue();
			assertThat(rs.getLong(1)).isEqualTo(200L);
		}
	}

	@Test
	void testSelectRangeThrowsOnNegativeValue() throws Exception {
		String tableName = "seq_table_neg";
		SequenceDbHelper helper = new SequenceDbHelper(dataSource, tableName);
		helper.createTable();

		try (Connection conn = dataSource.getConnection(); Statement stmt = conn.createStatement()) {
			stmt.executeUpdate("INSERT INTO " + tableName
					+ " (name, val, create_time, update_time) VALUES ('neg', -5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)");
		}

		assertThatThrownBy(() -> helper.selectRange("neg", 1L)).isInstanceOf(SequenceException.class)
			.hasMessageContaining("cannot be less than zero");
	}

	@Test
	void testSelectRangeThrowsOnOverflowValue() throws Exception {
		String tableName = "seq_table_overflow";
		SequenceDbHelper helper = new SequenceDbHelper(dataSource, tableName);
		helper.createTable();

		long overflowVal = Long.MAX_VALUE - 100_000_000L + 1;
		try (Connection conn = dataSource.getConnection(); Statement stmt = conn.createStatement()) {
			stmt.executeUpdate(
					"INSERT INTO " + tableName + " (name, val, create_time, update_time) VALUES ('overflow', "
							+ overflowVal + ", CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)");
		}

		assertThatThrownBy(() -> helper.selectRange("overflow", 1L)).isInstanceOf(SequenceException.class)
			.hasMessageContaining("overflow");
	}

}
