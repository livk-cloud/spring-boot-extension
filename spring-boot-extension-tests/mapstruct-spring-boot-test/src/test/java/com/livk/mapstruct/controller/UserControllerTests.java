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
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * @author livk
 */
@AutoConfigureMockMvc
@SpringBootTest
class UserControllerTests {

	@Autowired
	MockMvcTester tester;

	@Test
	void testList() {
		tester.get()
			.uri("/user")
			.assertThat()
			.hasStatusOk()
			.matches(jsonPath("customize[0].username").value("livk1"))
			.matches(jsonPath("customize[1].username").value("livk2"))
			.matches(jsonPath("customize[2].username").value("livk3"))
			.matches(jsonPath("customize[0].type").value(1))
			.matches(jsonPath("customize[1].type").value(2))
			.matches(jsonPath("customize[2].type").value(3))
			.matches(jsonPath("customize[0:2].createTime").exists())
			.matches(jsonPath("spring[0].username").value("livk1"))
			.matches(jsonPath("spring[1].username").value("livk2"))
			.matches(jsonPath("spring[2].username").value("livk3"))
			.matches(jsonPath("spring[0].type").value(1))
			.matches(jsonPath("spring[1].type").value(2))
			.matches(jsonPath("spring[2].type").value(3))
			.matches(jsonPath("spring[0:2].createTime").exists());
	}

	@Test
	void testGetById() {
		for (int i = 1; i <= 3; i++) {
			tester.get()
				.uri("/user/{id}", i)
				.assertThat()
				.hasStatusOk()
				.matches(jsonPath("customize.username").value("livk" + i))
				.matches(jsonPath("customize.type").value(i))
				.matches(jsonPath("customize.createTime").exists())
				.matches(jsonPath("spring.username").value("livk" + i))
				.matches(jsonPath("spring.type").value(i))
				.matches(jsonPath("spring.createTime").exists());
		}
	}

}

// Generated with love by TestMe :) Please report issues and submit feature requests at:
// http://weirddev.com/forum#!/testme
