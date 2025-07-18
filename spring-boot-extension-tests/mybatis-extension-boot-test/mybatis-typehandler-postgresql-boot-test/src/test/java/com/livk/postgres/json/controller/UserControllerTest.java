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

package com.livk.postgres.json.controller;

import com.livk.postgres.json.PGSQLTypeHandlerExampleApp;
import com.livk.testcontainers.containers.PostgresqlContainer;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * <p>
 * UserControllerTest
 * </p>
 *
 * @author livk
 */
@SpringBootTest(classes = PGSQLTypeHandlerExampleApp.class)
@AutoConfigureMockMvc
@Testcontainers(disabledWithoutDocker = true, parallel = true)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserControllerTest {

	@Container
	@ServiceConnection
	static final PostgresqlContainer postgresql = new PostgresqlContainer().withEnv("POSTGRES_PASSWORD", "123456")
		.withDatabaseName("mybatis");

	@DynamicPropertySource
	static void properties(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.username", postgresql::getUsername);
		registry.add("spring.datasource.password", postgresql::getPassword);
		registry.add("spring.datasource.url", () -> "jdbc:postgresql://" + postgresql.getHost() + ":"
				+ postgresql.getFirstMappedPort() + "/" + postgresql.getDatabaseName());
	}

	@Autowired
	MockMvc mockMvc;

	@Order(1)
	@Test
	void testSave() throws Exception {
		mockMvc.perform(post("/user")).andDo(print()).andExpect(status().isOk()).andExpect(content().string("true"));
	}

	@Order(2)
	@Test
	void testUsers() throws Exception {
		mockMvc.perform(get("/user")).andDo(print()).andExpect(status().isOk());
	}

}
