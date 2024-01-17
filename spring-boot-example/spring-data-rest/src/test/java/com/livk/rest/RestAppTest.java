/*
 * Copyright 2021-2024 spring-boot-extension the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.livk.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.livk.commons.jackson.core.JacksonSupport;
import com.livk.rest.entity.User;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author livk
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@AutoConfigureMockMvc
class RestAppTest {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	Jackson2ObjectMapperBuilder builder;

	@Order(2)
	@Test
	void testGetById() throws Exception {
		mockMvc.perform(get("/rest/api/user/{id}", 1))
			.andDo(print())
			.andExpect(status().is2xxSuccessful())
			.andExpect(jsonPath("username", "root").exists())
			.andExpect(jsonPath("password", "root").exists())
			.andExpect(jsonPath("age", 18).exists());
	}

	@Order(1)
	@Test
	void testSave() throws Exception {
		ObjectMapper mapper = builder.build();
		JacksonSupport support = new JacksonSupport(mapper);
		User user = new User();
		user.setUsername("root");
		user.setPassword("root");
		user.setAge(18);
		mockMvc.perform(post("/rest/api/user").content(support.writeValueAsBytes(user)))
			.andDo(print())
			.andExpect(status().is2xxSuccessful());
	}

	@Order(3)
	@Test
	void testUpdate() throws Exception {
		ObjectMapper mapper = builder.build();
		JacksonSupport support = new JacksonSupport(mapper);
		User user = new User();
		user.setUsername("admin");
		user.setPassword("admin");
		user.setAge(19);
		mockMvc.perform(put("/rest/api/user/{id}", 1).content(support.writeValueAsBytes(user)))
			.andDo(print())
			.andExpect(status().is2xxSuccessful());
	}

	@Order(6)
	@Test
	void testDelete() throws Exception {
		mockMvc.perform(delete("/rest/api/user/{id}", 1)).andDo(print()).andExpect(status().is2xxSuccessful());
	}

	@Order(4)
	@Test
	void testAuth() throws Exception {
		mockMvc.perform(get("/rest/api/user/search/auth").param("name", "admin").param("pwd", "admin"))
			.andDo(print())
			.andExpect(status().is2xxSuccessful())
			.andExpect(jsonPath("username", "admin").exists())
			.andExpect(jsonPath("password", "admin").exists())
			.andExpect(jsonPath("age", 19).exists());
	}

	@Order(5)
	@Test
	void testList() throws Exception {
		mockMvc.perform(get("/rest/api/user"))
			.andDo(print())
			.andExpect(status().is2xxSuccessful())
			.andExpect(jsonPath("_embedded.users[0].username", "admin").exists())
			.andExpect(jsonPath("_embedded.users[0].password", "admin").exists())
			.andExpect(jsonPath("_embedded.users[0].age", 19).exists())
			.andExpect(jsonPath("page.size", 20).exists())
			.andExpect(jsonPath("page.totalElements", 1).exists())
			.andExpect(jsonPath("page.totalPages", 1).exists())
			.andExpect(jsonPath("page.number", 0).exists());
	}

}
