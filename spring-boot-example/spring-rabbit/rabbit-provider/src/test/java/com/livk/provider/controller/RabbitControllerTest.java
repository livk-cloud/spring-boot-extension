/*
 * Copyright 2021 spring-boot-extension the original author or authors.
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
 *
 */

package com.livk.provider.controller;

import com.livk.commons.jackson.util.JsonMapperUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author livk
 */
@SpringBootTest
@AutoConfigureMockMvc
class RabbitControllerTest {

	static String body = JsonMapperUtils.writeValueAsString(Map.of("msg", "hello", "data", "By Livk"));

	@Autowired
	MockMvc mockMvc;

	@Test
	void sendMsgDirect() throws Exception {
		mockMvc.perform(post("/provider/sendMsgDirect").contentType(MediaType.APPLICATION_JSON).content(body))
			.andDo(print())
			.andExpect(status().isOk());
	}

	@Test
	void sendMsgFanout() throws Exception {
		mockMvc.perform(post("/provider/sendMsgFanout").contentType(MediaType.APPLICATION_JSON).content(body))
			.andDo(print())
			.andExpect(status().isOk());
	}

	@Test
	void sendMsgTopic() throws Exception {
		mockMvc
			.perform(post("/provider/sendMsgTopic/{key}", "rabbit.a.b").contentType(MediaType.APPLICATION_JSON)
				.content(body))
			.andDo(print())
			.andExpect(status().isOk());

		mockMvc
			.perform(post("/provider/sendMsgTopic/{key}", "a.b").contentType(MediaType.APPLICATION_JSON).content(body))
			.andDo(print())
			.andExpect(status().isOk());
	}

	@Test
	void sendMsgHeaders() throws Exception {
		mockMvc
			.perform(post("/provider/sendMsgHeaders").queryParam("json", "{\"auth\":\"livk\"}")
				.contentType(MediaType.APPLICATION_JSON)
				.content(body))
			.andDo(print())
			.andExpect(status().isOk());

		mockMvc
			.perform(
					post("/provider/sendMsgHeaders").queryParam("json", "{\"username\":\"livk\",\"password\":\"livk\"}")
						.contentType(MediaType.APPLICATION_JSON)
						.content(body))
			.andDo(print())
			.andExpect(status().isOk());
	}

}
