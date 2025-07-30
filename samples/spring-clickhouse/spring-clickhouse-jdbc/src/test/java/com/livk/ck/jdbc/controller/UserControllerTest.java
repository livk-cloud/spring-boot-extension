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

package com.livk.ck.jdbc.controller;

import com.livk.ck.jdbc.entity.User;
import com.livk.commons.jackson.JsonMapperUtils;
import com.livk.commons.util.DateUtils;
import com.livk.testcontainers.DockerImageNames;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.clickhouse.ClickHouseContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author livk
 */
@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers(disabledWithoutDocker = true, parallel = true)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserControllerTest {

	@Container
	@ServiceConnection
	static final ClickHouseContainer clickhouse = new ClickHouseContainer(DockerImageNames.clickhouse())
		.withExposedPorts(8123, 9000);

	@DynamicPropertySource
	static void properties(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", () -> "jdbc:clickhouse://" + clickhouse.getHost() + ":"
				+ clickhouse.getFirstMappedPort() + "/" + clickhouse.getDatabaseName());
		registry.add("spring.datasource.username", clickhouse::getUsername);
		registry.add("spring.datasource.password", clickhouse::getPassword);
	}

	@Autowired
	MockMvc mockMvc;

	@Order(2)
	@Test
	void testList() throws Exception {
		mockMvc.perform(get("/user"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("[0].appId").value("appId"))
			.andExpect(jsonPath("[0].version").value("version"));
	}

	@Order(3)
	@Test
	void testRemove() throws Exception {
		String format = DateUtils.format(LocalDateTime.now(), "yyyy-MM-dd");
		mockMvc.perform(delete("/user/" + format)).andDo(print()).andExpect(status().isOk());

		mockMvc.perform(get("/user"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$").isArray())
			.andExpect(jsonPath("$").value(hasSize(0)));
	}

	@Order(1)
	@Test
	void testSave() throws Exception {
		User user = new User().setId(Integer.MAX_VALUE)
			.setAppId("appId")
			.setVersion("version")
			.setRegTime(LocalDate.now());
		mockMvc
			.perform(post("/user").contentType(MediaType.APPLICATION_JSON)
				.content(JsonMapperUtils.writeValueAsString(user)))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().string("true"));
	}

}
