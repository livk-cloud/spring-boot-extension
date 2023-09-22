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

package com.livk.pulsar.producer.controller;

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
 * <p>
 * MessageControllerTest
 * </p>
 *
 * @author livk
 */
@SpringBootTest({
	"spring.pulsar.client.service-url=pulsar://livk.com:6650",
	"spring.pulsar.consumer.topics=livk-topic",
	"spring.pulsar.consumer.subscription.name=consumer"
})
@AutoConfigureMockMvc
class MessageControllerTest {
	@Autowired
	MockMvc mockMvc;


	@Test
	void testSend() throws Exception {
		Map<String, String> map = Map.of("username", "livk", "password", "123456");
		mockMvc.perform(post("/producer")
				.contentType(MediaType.APPLICATION_JSON)
				.content(JsonMapperUtils.writeValueAsString(map)))
			.andExpect(status().isOk())
			.andDo(print());
	}
}

