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

package com.livk.redisearch.webflux.controller;

import com.livk.redisearch.webflux.entity.Student;
import com.livk.testcontainers.DockerImageNames;
import com.redis.testcontainers.RedisStackContainer;
import org.hamcrest.core.IsIterableContaining;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * @author livk
 */
@SpringBootTest
@AutoConfigureWebTestClient
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
	WebTestClient client;

	@Test
	void testList() {
		client.get()
			.uri("/student")
			.exchange()
			.expectStatus()
			.isOk()
			.expectHeader()
			.contentType(MediaType.APPLICATION_JSON)
			.expectBodyList(Student.class)
			.hasSize(10)
			.value(students -> students.stream().map(Student::getName).toList(), IsIterableContaining.hasItem("livk-0"))
			.value(students -> students.stream().map(Student::getName).toList(), IsIterableContaining.hasItem("livk-1"))
			.value(students -> students.stream().map(Student::getName).toList(), IsIterableContaining.hasItem("livk-2"))
			.value(students -> students.stream().map(Student::getName).toList(), IsIterableContaining.hasItem("livk-3"))
			.value(students -> students.stream().map(Student::getName).toList(), IsIterableContaining.hasItem("livk-4"))
			.value(students -> students.stream().map(Student::getName).toList(), IsIterableContaining.hasItem("livk-5"))
			.value(students -> students.stream().map(Student::getName).toList(), IsIterableContaining.hasItem("livk-6"))
			.value(students -> students.stream().map(Student::getName).toList(), IsIterableContaining.hasItem("livk-7"))
			.value(students -> students.stream().map(Student::getName).toList(), IsIterableContaining.hasItem("livk-8"))
			.value(students -> students.stream().map(Student::getName).toList(),
					IsIterableContaining.hasItem("livk-9"));

		client.get()
			.uri(uriBuilder -> uriBuilder.path("/student")
				.queryParam("query", "@class:{1}{2}{3}")
				.build("{", "1班", "}"))
			.exchange()
			.expectStatus()
			.isOk()
			.expectHeader()
			.contentType(MediaType.APPLICATION_JSON)
			.expectBodyList(Student.class)
			.hasSize(1)
			.value(students -> students.stream().map(Student::getName).toList(),
					IsIterableContaining.hasItem("livk-0"));

		client.get()
			.uri(uriBuilder -> uriBuilder.path("/student").queryParam("query", "livk").build())
			.exchange()
			.expectStatus()
			.isOk()
			.expectHeader()
			.contentType(MediaType.APPLICATION_JSON)
			.expectBodyList(Student.class)
			.hasSize(10)
			.value(students -> students.stream().map(Student::getName).toList(), IsIterableContaining.hasItem("livk-0"))
			.value(students -> students.stream().map(Student::getName).toList(), IsIterableContaining.hasItem("livk-1"))
			.value(students -> students.stream().map(Student::getName).toList(), IsIterableContaining.hasItem("livk-2"))
			.value(students -> students.stream().map(Student::getName).toList(), IsIterableContaining.hasItem("livk-3"))
			.value(students -> students.stream().map(Student::getName).toList(), IsIterableContaining.hasItem("livk-4"))
			.value(students -> students.stream().map(Student::getName).toList(), IsIterableContaining.hasItem("livk-5"))
			.value(students -> students.stream().map(Student::getName).toList(), IsIterableContaining.hasItem("livk-6"))
			.value(students -> students.stream().map(Student::getName).toList(), IsIterableContaining.hasItem("livk-7"))
			.value(students -> students.stream().map(Student::getName).toList(), IsIterableContaining.hasItem("livk-8"))
			.value(students -> students.stream().map(Student::getName).toList(),
					IsIterableContaining.hasItem("livk-9"));

		client.get()
			.uri(uriBuilder -> uriBuilder.path("/student").queryParam("query", "女").build())
			.exchange()
			.expectStatus()
			.isOk()
			.expectHeader()
			.contentType(MediaType.APPLICATION_JSON)
			.expectBodyList(Student.class)
			.value(System.out::println);

		client.get()
			.uri(uriBuilder -> uriBuilder.path("/student").queryParam("query", "是一个学生").build())
			.exchange()
			.expectStatus()
			.isOk()
			.expectHeader()
			.contentType(MediaType.APPLICATION_JSON)
			.expectBodyList(Student.class)
			.value(System.out::println);
	}

}
