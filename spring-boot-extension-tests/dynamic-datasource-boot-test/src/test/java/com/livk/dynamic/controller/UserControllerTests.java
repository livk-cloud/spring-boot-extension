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

package com.livk.dynamic.controller;

import com.livk.testcontainers.DockerImageNames;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.mysql.MySQLContainer;
import org.testcontainers.postgresql.PostgreSQLContainer;

/**
 * @author livk
 */
@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers(disabledWithoutDocker = true, parallel = true)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserControllerTests {

	@Container
	@ServiceConnection
	static final PostgreSQLContainer postgresql = new PostgreSQLContainer(DockerImageNames.postgres())
		.withExposedPorts(PostgreSQLContainer.POSTGRESQL_PORT)
		.withEnv("POSTGRES_PASSWORD", "123456")
		.withDatabaseName("mybatis");

	@Container
	@ServiceConnection
	static final MySQLContainer mysql = new MySQLContainer(DockerImageNames.mysql())
		.withExposedPorts(MySQLContainer.MYSQL_PORT)
		.withEnv("MYSQL_ROOT_PASSWORD", "123456")
		.withDatabaseName("mybatis");

	@DynamicPropertySource
	static void properties(DynamicPropertyRegistry registry) {
		registry.add("spring.dynamic.primary", () -> "mysql");
		registry.add("spring.dynamic.datasource.mysql.username", mysql::getUsername);
		registry.add("spring.dynamic.datasource.mysql.password", mysql::getPassword);
		registry.add("spring.dynamic.datasource.mysql.url", () -> "jdbc:mysql://" + mysql.getHost() + ":"
				+ mysql.getFirstMappedPort() + "/" + mysql.getDatabaseName() + "?createDatabaseIfNotExist=true");

		registry.add("spring.dynamic.datasource.pgsql.username", postgresql::getUsername);
		registry.add("spring.dynamic.datasource.pgsql.password", postgresql::getPassword);
		registry.add("spring.dynamic.datasource.pgsql.url", () -> "jdbc:postgresql://" + postgresql.getHost() + ":"
				+ postgresql.getFirstMappedPort() + "/" + postgresql.getDatabaseName());
	}

	@Autowired
	MockMvcTester tester;

	@Order(1)
	@Test
	void testMysqlSave() {
		tester.post().uri("/user/mysql").assertThat().hasStatusOk().bodyText().isEqualTo("true");
	}

	@Order(2)
	@Test
	void testMysqlUser() {
		tester.get().uri("/user/mysql").assertThat().hasStatusOk();
	}

	@Order(3)
	@Test
	void testPgsqlSave() {
		tester.post().uri("/user/pgsql").assertThat().hasStatusOk().bodyText().isEqualTo("true");
	}

	@Order(4)
	@Test
	void testPgsqlUser() {
		tester.get().uri("/user/pgsql").assertThat().hasStatusOk();
	}

}

// Generated with love by TestMe :) Please report issues and submit feature requests at:
// http://weirddev.com/forum#!/testme
