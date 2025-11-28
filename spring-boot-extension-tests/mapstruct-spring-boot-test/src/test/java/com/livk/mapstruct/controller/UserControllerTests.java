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

package com.livk.mapstruct.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author livk
 */
@AutoConfigureMockMvc
@SpringBootTest
class UserControllerTests {

	@Autowired
	MockMvc mockMvc;

	@Test
	void testList() throws Exception {
		mockMvc.perform(get("/user"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("customize[0].username").value("livk1"))
			.andExpect(jsonPath("customize[1].username").value("livk2"))
			.andExpect(jsonPath("customize[2].username").value("livk3"))
			.andExpect(jsonPath("customize[0].type").value(1))
			.andExpect(jsonPath("customize[1].type").value(2))
			.andExpect(jsonPath("customize[2].type").value(3))
			.andExpect(jsonPath("customize[0:2].createTime").exists())
			.andExpect(jsonPath("spring[0].username").value("livk1"))
			.andExpect(jsonPath("spring[1].username").value("livk2"))
			.andExpect(jsonPath("spring[2].username").value("livk3"))
			.andExpect(jsonPath("spring[0].type").value(1))
			.andExpect(jsonPath("spring[1].type").value(2))
			.andExpect(jsonPath("spring[2].type").value(3))
			.andExpect(jsonPath("spring[0:2].createTime").exists());
	}

	@Test
	void testGetById() throws Exception {
		for (int i = 1; i <= 3; i++) {
			mockMvc.perform(get("/user/{id}", i))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("customize.username").value("livk" + i))
				.andExpect(jsonPath("customize.type").value(i))
				.andExpect(jsonPath("customize.createTime").exists())
				.andExpect(jsonPath("spring.username").value("livk" + i))
				.andExpect(jsonPath("spring.type").value(i))
				.andExpect(jsonPath("spring.createTime").exists());
		}
	}

}

// Generated with love by TestMe :) Please report issues and submit feature requests at:
// http://weirddev.com/forum#!/testme
