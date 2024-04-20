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

package com.livk.rocket.producer.controller;

import com.livk.commons.jackson.util.JsonMapperUtils;
import com.livk.rocket.constant.RocketConstant;
import com.livk.rocket.dto.RocketDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author laokou
 * @author livk
 */
@Disabled
@SpringBootTest
@AutoConfigureMockMvc
class RocketProducerTest {

	@Autowired
	MockMvc mockMvc;

	RocketDTO dto = new RocketDTO();

	@BeforeEach
	void init() {
		String msg = "Java第一，老寇无敌。千秋万代，一统江湖。";
		dto.setBody(msg);
	}

	@Test
	void sendMessage() throws Exception {
		mockMvc
			.perform(
					post("/api/send/{topic}", RocketConstant.LIVK_MESSAGE_TOPIC).contentType(MediaType.APPLICATION_JSON)
						.content(JsonMapperUtils.writeValueAsString(dto)))
			.andExpect(status().isOk())
			.andDo(print());
	}

	@Test
	void sendAsyncMessage() throws Exception {
		mockMvc
			.perform(post("/api/sendAsync/{topic}", RocketConstant.LIVK_MESSAGE_TOPIC)
				.contentType(MediaType.APPLICATION_JSON)
				.content(JsonMapperUtils.writeValueAsString(dto)))
			.andExpect(status().isOk())
			.andDo(print());
	}

	@Test
	void sendOneMessage() throws Exception {
		mockMvc
			.perform(post("/api/sendOne/{topic}", RocketConstant.LIVK_MESSAGE_TOPIC)
				.contentType(MediaType.APPLICATION_JSON)
				.content(JsonMapperUtils.writeValueAsString(dto)))
			.andExpect(status().isOk())
			.andDo(print());
	}

	@Test
	void sendTransactionMessage() throws Exception {
		mockMvc
			.perform(post("/api/sendTransaction/{topic}", RocketConstant.LIVK_MESSAGE_TOPIC)
				.contentType(MediaType.APPLICATION_JSON)
				.content(JsonMapperUtils.writeValueAsString(dto)))
			.andExpect(status().isOk())
			.andDo(print());
	}

	@Test
	void sendDelay() throws Exception {
		mockMvc
			.perform(post("/api/sendDelay/{topic}", RocketConstant.LIVK_MESSAGE_TOPIC)
				.contentType(MediaType.APPLICATION_JSON)
				.content(JsonMapperUtils.writeValueAsString(dto)))
			.andExpect(status().isOk())
			.andDo(print());
	}

	@Test
	void sendMessageOrderly() throws Exception {
		for (int i = 1; i <= 6; i++) {
			RocketDTO rocketDTO = new RocketDTO();
			rocketDTO.setBody("同步顺序消息" + i);
			mockMvc
				.perform(post("/api/sendOrderly/{topic}", RocketConstant.LIVK_MESSAGE_ORDERLY_TOPIC)
					.contentType(MediaType.APPLICATION_JSON)
					.content(JsonMapperUtils.writeValueAsString(rocketDTO)))
				.andExpect(status().isOk())
				.andDo(print());
		}
	}

	@Test
	void sendAsyncMessageOrderly() throws Exception {
		for (int i = 1; i <= 6; i++) {
			RocketDTO rocketDTO = new RocketDTO();
			rocketDTO.setBody("异步顺序消息" + i);
			mockMvc
				.perform(post("/api/sendAsyncOrderly/{topic}", RocketConstant.LIVK_MESSAGE_ORDERLY_TOPIC)
					.contentType(MediaType.APPLICATION_JSON)
					.content(JsonMapperUtils.writeValueAsString(rocketDTO)))
				.andExpect(status().isOk())
				.andDo(print());
		}
	}

	@Test
	void sendOneMessageOrderly() throws Exception {
		for (int i = 1; i <= 6; i++) {
			RocketDTO rocketDTO = new RocketDTO();
			rocketDTO.setBody("单向顺序消息" + i);
			mockMvc
				.perform(post("/api/sendOneOrderly/{topic}", RocketConstant.LIVK_MESSAGE_ORDERLY_TOPIC)
					.contentType(MediaType.APPLICATION_JSON)
					.content(JsonMapperUtils.writeValueAsString(rocketDTO)))
				.andExpect(status().isOk())
				.andDo(print());
		}
	}

}
