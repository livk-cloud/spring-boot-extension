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

package com.livk.sso.auth.controller;

import com.livk.commons.jackson.JsonMapperUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

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
@SpringBootTest({ "spring.datasource.driver-class-name=org.h2.Driver", "spring.datasource.url=jdbc:h2:mem:test",
		"spring.sql.init.platform=h2", "spring.sql.init.mode=embedded" })
@AutoConfigureMockMvc
class UserControllerTest {

	@Autowired
	MockMvc mockMvc;

	String token;

	@BeforeEach
	public void init() throws Exception {
		Map<String, String> body = new HashMap<>();
		body.put("username", "livk");
		body.put("password", "123456");
		MockHttpServletResponse response = mockMvc
			.perform(post("/login").contentType(MediaType.APPLICATION_JSON)
				.content(JsonMapperUtils.writeValueAsString(body)))
			.andExpect(status().isOk())
			.andDo(print())
			.andExpect(jsonPath("code").value(200))
			.andReturn()
			.getResponse();
		token = "Bearer "
				+ JsonMapperUtils.readValueMap(response.getContentAsString(), String.class, String.class).get("data");
	}

	@Test
	void testList() throws Exception {
		mockMvc.perform(get("/user/list").header(HttpHeaders.AUTHORIZATION, token))
			.andExpect(status().isOk())
			.andDo(print())
			.andExpect(content().string("list"));
	}

	@Test
	void testUpdate() throws Exception {
		mockMvc.perform(put("/user/update").header(HttpHeaders.AUTHORIZATION, token))
			.andExpect(status().isOk())
			.andDo(print())
			.andExpect(content().string("update"));
	}

}

// Generated with love by TestMe :) Please report issues and submit feature requests at:
// http://weirddev.com/forum#!/testme
