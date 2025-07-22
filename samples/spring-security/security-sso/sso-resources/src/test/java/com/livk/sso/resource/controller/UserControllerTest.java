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

package com.livk.sso.resource.controller;

import com.livk.commons.http.annotation.EnableHttpClient;
import com.livk.commons.http.annotation.HttpClientType;
import com.livk.commons.jackson.JsonMapperUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestClient;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author livk
 */
@Disabled("需要启动授权服务器")
@EnableHttpClient(HttpClientType.REST_CLIENT)
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	RestClient restClient;

	String token;

	@BeforeEach
	public void init() {
		Map<String, String> body = new HashMap<>();
		body.put("username", "livk");
		body.put("password", "123456");
		ResponseEntity<String> responseEntity = restClient.post()
			.uri("http://localhost:9987/login")
			.body(body)
			.retrieve()
			.toEntity(String.class);
		token = "Bearer "
				+ JsonMapperUtils.readValueMap(responseEntity.getBody(), String.class, String.class).get("data");
	}

	@Test
	void test() {
		System.out.println(token);
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
