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

package com.livk.http.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.SpringVersion;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * @author livk
 */
@SpringBootTest
class SpringServiceTests {

	@MockitoBean
	SpringService springService;

	@Autowired
	SpringService service;

	@BeforeEach
	void setUp() {
		given(springService.spring()).willReturn(Map.of("spring-version", "1.0.0", "mockito", "true"));
	}

	@Test
	void spring() {
		Map<String, String> result = service.spring();

		assertThat(result.get("spring-version")).isNotEqualTo(SpringVersion.getVersion()).isEqualTo("1.0.0");
		assertThat(result.get("mockito")).asBoolean().isTrue();
	}

}
