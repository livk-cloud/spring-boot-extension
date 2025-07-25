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

import com.livk.testcontainers.containers.PostgresqlContainer;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.postgresql.Driver;
import org.springframework.boot.jdbc.DatabaseDriver;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Testcontainers(disabledWithoutDocker = true, parallel = true)
class PostgresProviderTests {

	@Container
	static final PostgresqlContainer postgresql = new PostgresqlContainer().withEnv("POSTGRES_PASSWORD", "123456")
		.withDatabaseName("sequence");

	static SqlProvider provider;

	static JdbcClient jdbcClient;

	@BeforeAll
	static void setupDataSource() {
		postgresql.start();
		HikariDataSource dataSource = new HikariDataSource();
		dataSource.setDriverClassName(Driver.class.getName());
		dataSource.setJdbcUrl("jdbc:postgresql://" + postgresql.getHost() + ":" + postgresql.getFirstMappedPort() + "/"
				+ postgresql.getDatabaseName());
		dataSource.setUsername(postgresql.getUsername());
		dataSource.setPassword(postgresql.getPassword());

		provider = SqlProvider.fromDataSource(dataSource);

		jdbcClient = JdbcClient.create(dataSource);
	}

	@Order(1)
	@Test
	void type() {
		assertThat(provider.type()).isEqualTo(DatabaseDriver.POSTGRESQL);
	}

	@Order(2)
	@Test
	void createTableSql() {
		String tableSql = provider.createTableSql("test");
		assertThat(tableSql).startsWithIgnoringCase("CREATE TABLE").containsIgnoringCase("test");

		assertThat(jdbcClient.sql(tableSql).update()).isEqualTo(0);
	}

	@Order(3)
	@Test
	void insertRangeSql() {
		String insertedRangeSql = provider.insertRangeSql("test");
		assertThat(insertedRangeSql).startsWithIgnoringCase("INSERT INTO").containsIgnoringCase("test");

		Timestamp now = Timestamp.valueOf(LocalDateTime.now());
		assertThat(jdbcClient.sql(insertedRangeSql)
			.param("name", "testName")
			.param("val", 1)
			.param("create_time", now)
			.param("update_time", now)
			.update()).isEqualTo(1);
	}

	@Order(4)
	@Test
	void updateRangeSql() {
		String updatedRangeSql = provider.updateRangeSql("test");
		assertThat(updatedRangeSql).startsWithIgnoringCase("UPDATE").containsIgnoringCase("test");

		Timestamp now = Timestamp.valueOf(LocalDateTime.now());
		assertThat(jdbcClient.sql(updatedRangeSql)
			.param("new_val", 2)
			.param("update_time", now)
			.param("name", "testName")
			.param("old_val", 1)
			.update()).isEqualTo(1);
	}

	@Order(5)
	@Test
	void selectRangeSql() {
		String selectRangeSql = provider.selectRangeSql("test");
		assertThat(selectRangeSql).startsWithIgnoringCase("SELECT").containsIgnoringCase("test");

		assertThat(jdbcClient.sql(selectRangeSql).param("name", "testName").query(Long.class).single()).isEqualTo(2);
	}

}
