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

package com.livk.redisearch.mvc.controller;

import com.livk.testcontainers.DockerImageNames;
import com.redis.testcontainers.RedisStackContainer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * <p>
 * StudentControllerTest
 * </p>
 *
 * @author livk
 */
@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers(disabledWithoutDocker = true, parallel = true)
class StudentControllerTest {

	@Container
	@ServiceConnection
	static final RedisStackContainer redisStack = new RedisStackContainer(DockerImageNames.redisStack());

	@DynamicPropertySource
	static void redisProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.redisearch.host", redisStack::getHost);
		registry.add("spring.redisearch.port", redisStack::getFirstMappedPort);
	}

	@Autowired
	MockMvc mockMvc;

	@Test
	void testList() throws Exception {
		mockMvc.perform(get("/student"))
			.andExpect(status().isOk())
			.andDo(print())
			.andExpect(jsonPath("[0].name").value("livk-0"))
			.andExpect(jsonPath("[1].name").value("livk-1"))
			.andExpect(jsonPath("[2].name").value("livk-2"))
			.andExpect(jsonPath("[3].name").value("livk-3"))
			.andExpect(jsonPath("[4].name").value("livk-4"))
			.andExpect(jsonPath("[5].name").value("livk-5"))
			.andExpect(jsonPath("[6].name").value("livk-6"))
			.andExpect(jsonPath("[7].name").value("livk-7"))
			.andExpect(jsonPath("[8].name").value("livk-8"))
			.andExpect(jsonPath("[9].name").value("livk-9"));

		mockMvc.perform(get("/student").param("query", "@class:{1班}"))
			.andExpect(status().isOk())
			.andDo(print())
			.andExpect(jsonPath("[0].name").value("livk-0"));

		mockMvc.perform(get("/student").param("query", "livk"))
			.andExpect(status().isOk())
			.andDo(print())
			.andExpect(jsonPath("[0].name").value("livk-0"))
			.andExpect(jsonPath("[1].name").value("livk-1"))
			.andExpect(jsonPath("[2].name").value("livk-2"))
			.andExpect(jsonPath("[3].name").value("livk-3"))
			.andExpect(jsonPath("[4].name").value("livk-4"))
			.andExpect(jsonPath("[5].name").value("livk-5"))
			.andExpect(jsonPath("[6].name").value("livk-6"))
			.andExpect(jsonPath("[7].name").value("livk-7"))
			.andExpect(jsonPath("[8].name").value("livk-8"))
			.andExpect(jsonPath("[9].name").value("livk-9"));

		mockMvc.perform(get("/student").param("query", "女")).andExpect(status().isOk()).andDo(print());

		mockMvc.perform(get("/student").param("query", "是一个学生")).andExpect(status().isOk()).andDo(print());
	}

}
