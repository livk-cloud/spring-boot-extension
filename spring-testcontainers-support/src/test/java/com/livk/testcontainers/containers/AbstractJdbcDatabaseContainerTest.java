/*
 * Copyright 2021-2024 spring-boot-extension the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *       https://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.livk.testcontainers.containers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author livk
 */
abstract class AbstractJdbcDatabaseContainerTest<T extends JdbcDatabaseContainer<T>> {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	protected void testWithDB(T db) throws SQLException {
		db.withLogConsumer(new Slf4jLogConsumer(logger)).start();
		try (Connection connection = DriverManager.getConnection(db.getJdbcUrl(), db.getUsername(), db.getPassword())) {
			try (PreparedStatement statement = connection.prepareStatement("SELECT 1")) {
				try (ResultSet resultSet = statement.executeQuery()) {
					if (resultSet.next()) {
						assertEquals(1, resultSet.getInt(1));
					}
					else {
						throw new IllegalStateException();
					}
				}
			}
		}
	}

}
