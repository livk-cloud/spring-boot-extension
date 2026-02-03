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

package com.livk.mybatisplugins.controller;

import com.livk.mybatisplugins.entity.User;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * @author livk
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest({ "spring.datasource.driver-class-name=org.h2.Driver", "spring.datasource.url=jdbc:h2:mem:test",
		"spring.sql.init.platform=h2", "spring.sql.init.mode=embedded" })
@AutoConfigureMockMvc
class UserControllerTests {

	@Autowired
	MockMvcTester tester;

	final Integer id = 10;

	final ObjectMapper mapper = JsonMapper.builder().build();

	@Order(3)
	@Test
	void testGetById() {
		tester.get()
			.uri("/user/{id}", id)
			.assertThat()
			.hasStatusOk()
			.matches(jsonPath("id").value(id))
			.matches(jsonPath("insertTime").isNotEmpty())
			.matches(jsonPath("updateTime").isNotEmpty());
	}

	@Order(2)
	@Test
	void testUpdateById() {
		User user = new User();
		user.setUsername("livk https");
		tester.put()
			.uri("/user/{id}", id)
			.contentType(MediaType.APPLICATION_JSON)
			.content(mapper.writeValueAsString(user))
			.assertThat()
			.hasStatusOk()
			.bodyText()
			.isEqualTo("true");
	}

	@Order(1)
	@Test
	void testSave() {
		User user = new User();
		user.setId(id);
		user.setUsername("livk");
		tester.post()
			.uri("/user")
			.contentType(MediaType.APPLICATION_JSON)
			.content(mapper.writeValueAsString(user))
			.assertThat()
			.hasStatusOk()
			.bodyText()
			.isEqualTo("true");
	}

	@Order(5)
	@Test
	void testDeleteById() {
		tester.delete().uri("/user/{id}", id).assertThat().hasStatusOk().bodyText().isEqualTo("true");
	}

	@Order(4)
	@Test
	void testList() {
		tester.get()
			.uri("/user")
			.assertThat()
			.hasStatusOk()
			.matches(jsonPath("list.[*].id").value(id))
			.matches(jsonPath("pageNum").value(1))
			.matches(jsonPath("pageSize").value(10));
	}

}

// Generated with love by TestMe :) Please report issues and submit feature requests at:
// http://weirddev.com/forum#!/testme
