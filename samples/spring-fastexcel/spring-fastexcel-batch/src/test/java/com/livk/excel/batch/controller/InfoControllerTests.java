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

package com.livk.excel.batch.controller;

import com.livk.testcontainers.containers.MysqlContainer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.http.HttpMethod.POST;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author livk
 */
@SpringBootTest({ "spring.sql.init.mode=always",
		"spring.sql.init.schema-locations=classpath:org/springframework/batch/core/schema-mysql.sql" })
@AutoConfigureMockMvc
@Testcontainers(disabledWithoutDocker = true, parallel = true)
class InfoControllerTests {

	@Container
	@ServiceConnection
	static final MysqlContainer mysql = new MysqlContainer().withEnv("MYSQL_ROOT_PASSWORD", "123456")
		.withDatabaseName("fastexcel_batch");

	@DynamicPropertySource
	static void properties(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.username", mysql::getUsername);
		registry.add("spring.datasource.password", mysql::getPassword);
		registry.add("spring.datasource.url", () -> "jdbc:mysql://" + mysql.getHost() + ":" + mysql.getFirstMappedPort()
				+ "/" + mysql.getDatabaseName() + "?createDatabaseIfNotExist=true");
	}

	@Autowired
	MockMvc mockMvc;

	@Test
	void upload() throws Exception {
		ClassPathResource resource = new ClassPathResource("mobile-test.xlsx");

		MockMultipartFile file = new MockMultipartFile("file", "mobile-test.xlsx", MediaType.MULTIPART_FORM_DATA_VALUE,
				resource.getInputStream());

		mockMvc.perform(multipart(HttpMethod.POST, "/upload").file(file)).andDo(print()).andExpect(status().isOk());
	}

	@Test
	void up() throws Exception {
		ClassPathResource resource = new ClassPathResource("mobile-test.xlsx");

		MockMultipartFile file = new MockMultipartFile("file", "mobile-test.xlsx", MediaType.MULTIPART_FORM_DATA_VALUE,
				resource.getInputStream());

		mockMvc.perform(multipart(POST, "/excel").file(file))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.length()").value(100000))
			.andExpect(jsonPath("$[99999]").exists());
	}

}
