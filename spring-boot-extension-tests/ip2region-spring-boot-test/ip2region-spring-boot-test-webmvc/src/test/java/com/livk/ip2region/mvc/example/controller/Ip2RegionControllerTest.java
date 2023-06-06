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

package com.livk.ip2region.mvc.example.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * <p>
 * Ip2RegionControllerTest
 * </p>
 *
 * @author livk
 */
@SpringBootTest("ip2region.enabled=true")
@AutoConfigureMockMvc
class Ip2RegionControllerTest {

	@Autowired
	MockMvc mockMvc;

	@Test
	void post() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/ip"))
			.andExpect(status().isOk())
			.andDo(print())
			.andExpect(jsonPath("*.ip", "127.0.0.1").exists())
			.andExpect(jsonPath("*.city", "内网IP").exists())
			.andExpect(jsonPath("*.operator", "内网IP").exists());
	}
}
