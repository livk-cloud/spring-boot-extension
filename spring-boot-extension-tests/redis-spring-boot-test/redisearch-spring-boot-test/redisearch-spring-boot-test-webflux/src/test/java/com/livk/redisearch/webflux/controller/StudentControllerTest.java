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

package com.livk.redisearch.webflux.controller;

import com.livk.redisearch.webflux.entity.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertLinesMatch;

/**
 * @author livk
 */
@SpringBootTest
@AutoConfigureWebTestClient
class StudentControllerTest {

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
			.value(students -> {
				List<String> result = students.stream().map(Student::getName).toList();
				List<String> list = List.of("livk-0", "livk-1", "livk-2", "livk-3", "livk-4", "livk-5", "livk-6",
						"livk-7", "livk-8", "livk-9");
				assertLinesMatch(list, result);
			});

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
			.value(students -> {
				List<String> result = students.stream().map(Student::getName).toList();
				List<String> list = List.of("livk-0");
				assertLinesMatch(list, result);
			});

		client.get()
			.uri(uriBuilder -> uriBuilder.path("/student").queryParam("query", "livk").build())
			.exchange()
			.expectStatus()
			.isOk()
			.expectHeader()
			.contentType(MediaType.APPLICATION_JSON)
			.expectBodyList(Student.class)
			.value(students -> {
				List<String> result = students.stream().map(Student::getName).toList();
				List<String> list = List.of("livk-0", "livk-1", "livk-2", "livk-3", "livk-4", "livk-5", "livk-6",
						"livk-7", "livk-8", "livk-9");
				assertLinesMatch(list, result);
			});

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
