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

package com.livk.mybatisplugins.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.livk.mybatisplugins.entity.User;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * <p>
 * UserControllerTest
 * </p>
 *
 * @author livk
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest({ "spring.datasource.driver-class-name=org.h2.Driver", "spring.datasource.url=jdbc:h2:mem:test",
		"spring.sql.init.platform=h2", "spring.sql.init.mode=embedded" })
@AutoConfigureMockMvc
class UserControllerTest {

	@Autowired
	MockMvc mockMvc;

	final Integer id = 10;

	final ObjectMapper mapper = JsonMapper.builder().build();

	@Order(3)
	@Test
	void testGetById() throws Exception {
		mockMvc.perform(get("/user/{id}", id))
			.andExpect(status().isOk())
			.andDo(print())
			.andExpect(jsonPath("id").value(id))
			.andExpect(jsonPath("insertTime").isNotEmpty())
			.andExpect(jsonPath("updateTime").isNotEmpty());
	}

	@Order(2)
	@Test
	void testUpdateById() throws Exception {
		User user = new User();
		user.setUsername("livk https");
		mockMvc
			.perform(put("/user/{id}", id).contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(user)))
			.andExpect(status().isOk())
			.andDo(print())
			.andExpect(content().string("true"));
	}

	@Order(1)
	@Test
	void testSave() throws Exception {
		User user = new User();
		user.setId(id);
		user.setUsername("livk");
		mockMvc.perform(post("/user").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(user)))
			.andExpect(status().isOk())
			.andDo(print())
			.andExpect(content().string("true"));
	}

	@Order(5)
	@Test
	void testDeleteById() throws Exception {
		mockMvc.perform(delete("/user/{id}", id))
			.andExpect(status().isOk())
			.andDo(print())
			.andExpect(content().string("true"));
	}

	@Order(4)
	@Test
	void testList() throws Exception {
		mockMvc.perform(get("/user"))
			.andExpect(status().isOk())
			.andDo(print())
			.andExpect(jsonPath("list.[*].id").value(id))
			.andExpect(jsonPath("pageNum").value(1))
			.andExpect(jsonPath("pageSize").value(10));
	}

}

// Generated with love by TestMe :) Please report issues and submit feature requests at:
// http://weirddev.com/forum#!/testme
