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

package com.livk.context.sequence.support.db;

import org.junit.jupiter.api.Test;
import org.springframework.boot.jdbc.DatabaseDriver;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

/**
 * @author livk
 */
class SequenceDbHelperSqlUnitTests {

	@Test
	void mysqlSqlTemplates() {
		MySqlSequenceDbHelper helper = new MySqlSequenceDbHelper();
		assertThat(helper.type()).isEqualTo(DatabaseDriver.MYSQL);
		assertThat(helper.createTableSql("seq")).containsIgnoringCase("CREATE TABLE").contains("seq");
		assertThat(helper.insertRangeSql("seq")).containsIgnoringCase("INSERT IGNORE INTO");
		assertThat(helper.updateRangeSql("seq")).containsIgnoringCase("UPDATE");
		assertThat(helper.selectRangeSql("seq")).containsIgnoringCase("SELECT");
	}

	@Test
	void postgresSqlTemplates() {
		PostgresSequenceDbHelper helper = new PostgresSequenceDbHelper();
		assertThat(helper.type()).isEqualTo(DatabaseDriver.POSTGRESQL);
		assertThat(helper.createTableSql("seq")).containsIgnoringCase("CREATE TABLE").contains("seq");
		assertThat(helper.insertRangeSql("seq")).containsIgnoringCase("ON CONFLICT");
		assertThat(helper.updateRangeSql("seq")).containsIgnoringCase("UPDATE");
		assertThat(helper.selectRangeSql("seq")).containsIgnoringCase("SELECT");
	}

	@Test
	void fromDataSourceWrapsConnectionFailure() throws Exception {
		DataSource dataSource = mock(DataSource.class);
		given(dataSource.getConnection()).willThrow(new SQLException("down"));
		assertThatThrownBy(() -> SequenceDbHelper.fromDataSource(dataSource)).isInstanceOf(IllegalStateException.class)
			.hasMessageContaining("database type");
	}

	@Test
	void fromDataSourceRejectsUnknownDatabase() throws Exception {
		DataSource dataSource = mock(DataSource.class);
		Connection connection = mock(Connection.class);
		DatabaseMetaData metaData = mock(DatabaseMetaData.class);
		given(dataSource.getConnection()).willReturn(connection);
		given(connection.getMetaData()).willReturn(metaData);
		given(metaData.getDatabaseProductName()).willReturn("UnknownDB");
		assertThatThrownBy(() -> SequenceDbHelper.fromDataSource(dataSource)).isInstanceOf(IllegalStateException.class)
			.hasMessageContaining("No SqlProvider");
	}

}
