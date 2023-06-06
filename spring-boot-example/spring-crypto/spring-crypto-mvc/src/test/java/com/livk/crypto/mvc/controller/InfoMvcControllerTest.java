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

package com.livk.crypto.mvc.controller;

import com.livk.commons.jackson.util.JsonMapperUtils;
import com.livk.crypto.CryptoType;
import com.livk.crypto.support.AesSecurity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Locale;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author livk
 */
@SpringBootTest
@AutoConfigureMockMvc
class InfoMvcControllerTest {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	AesSecurity aesSecurity;

	@Test
	void infoGet() throws Exception {
		String encoding = aesSecurity.print(123456L, Locale.CHINA);
		encoding = CryptoType.AES.wrapper(encoding);
		mockMvc.perform(get("/info/{id}", encoding)
				.param("id", encoding))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("id.paramId", encoding).exists())
			.andExpect(jsonPath("id.variableId", encoding).exists());
	}

	@Test
	void infoPost() throws Exception {
		String encoding = aesSecurity.print(123456L, Locale.CHINA);
		encoding = CryptoType.AES.wrapper(encoding);
		String json = JsonMapperUtils.writeValueAsString(Map.of("variableId", encoding, "paramId", encoding));
		mockMvc.perform(post("/info")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("body.paramId", encoding).exists())
			.andExpect(jsonPath("body.variableId", encoding).exists());
	}
}
