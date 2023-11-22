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

package com.livk.commons.http;

import com.livk.commons.http.annotation.EnableHttpClient;
import com.livk.commons.http.annotation.HttpClientType;
import com.livk.commons.spring.context.SpringContextHolder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author livk
 */
@SpringBootTest("spring.main.web-application-type=servlet")
@EnableHttpClient({ HttpClientType.REST_TEMPLATE, HttpClientType.WEB_CLIENT })
public class SpringHttpTest {

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	WebClient webClient;

	@Test
	public void test() {
		assertNotNull(restTemplate);
		assertNotNull(webClient);
		assertEquals(SpringContextHolder.getBean(RestTemplate.class), restTemplate);
		assertEquals(SpringContextHolder.getBean(WebClient.class), webClient);
	}

}
