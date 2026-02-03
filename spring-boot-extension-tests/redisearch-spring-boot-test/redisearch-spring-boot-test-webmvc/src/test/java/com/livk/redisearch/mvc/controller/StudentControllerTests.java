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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * @author livk
 */
@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers(disabledWithoutDocker = true, parallel = true)
class StudentControllerTests {

	@Container
	@ServiceConnection
	static final RedisStackContainer redisStack = new RedisStackContainer(DockerImageNames.redisStack());

	@DynamicPropertySource
	static void redisProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.redisearch.host", redisStack::getHost);
		registry.add("spring.redisearch.port", redisStack::getFirstMappedPort);
	}

	@Autowired
	MockMvcTester tester;

	@Test
	void testList() {
		tester.get()
			.uri("/student")
			.assertThat()
			.hasStatusOk()
			.matches(jsonPath("[0].name").value("livk-0"))
			.matches(jsonPath("[1].name").value("livk-1"))
			.matches(jsonPath("[2].name").value("livk-2"))
			.matches(jsonPath("[3].name").value("livk-3"))
			.matches(jsonPath("[4].name").value("livk-4"))
			.matches(jsonPath("[5].name").value("livk-5"))
			.matches(jsonPath("[6].name").value("livk-6"))
			.matches(jsonPath("[7].name").value("livk-7"))
			.matches(jsonPath("[8].name").value("livk-8"))
			.matches(jsonPath("[9].name").value("livk-9"));

		tester.get()
			.uri("/student")
			.param("query", "@class:{1班}")
			.assertThat()
			.hasStatusOk()
			.matches(jsonPath("[0].name").value("livk-0"));

		tester.get()
			.uri("/student")
			.param("query", "livk")
			.assertThat()
			.hasStatusOk()
			.matches(jsonPath("[0].name").value("livk-0"))
			.matches(jsonPath("[1].name").value("livk-1"))
			.matches(jsonPath("[2].name").value("livk-2"))
			.matches(jsonPath("[3].name").value("livk-3"))
			.matches(jsonPath("[4].name").value("livk-4"))
			.matches(jsonPath("[5].name").value("livk-5"))
			.matches(jsonPath("[6].name").value("livk-6"))
			.matches(jsonPath("[7].name").value("livk-7"))
			.matches(jsonPath("[8].name").value("livk-8"))
			.matches(jsonPath("[9].name").value("livk-9"));

		tester.get().uri("/student").param("query", "女").assertThat().hasStatusOk();

		tester.get().uri("/student").param("query", "是一个学生").assertThat().hasStatusOk();
	}

}
