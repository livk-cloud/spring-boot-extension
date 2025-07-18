/*
 * Copyright 2021-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * you may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.livk.http.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

/**
 * @author livk
 */
@SpringBootTest
class JavaServiceTest {

	@MockitoBean
	JavaService javaService;

	@Autowired
	JavaService service;

	@BeforeEach
	void setUp() {
		when(javaService.java()).thenReturn(Map.of("java-version", "17", "mockito", "true"));
	}

	@Test
	void java() {
		Map<String, String> result = service.java();

		assertNotEquals(result.get("java-version"), System.getProperty("java.version"));
		assertEquals("17", result.get("java-version"));
		assertTrue(Boolean.parseBoolean(result.get("mockito")));
	}

}
