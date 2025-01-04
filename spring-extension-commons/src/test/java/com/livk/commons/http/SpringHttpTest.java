/*
 * Copyright 2021-2024 spring-boot-extension the original author or authors.
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
 */

package com.livk.commons.http;

import com.livk.commons.SpringContextHolder;
import com.livk.commons.http.annotation.EnableHttpClient;
import com.livk.commons.http.annotation.HttpClientType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.reactor.netty.ReactorNettyConfigurations;
import org.springframework.boot.autoconfigure.web.client.RestClientAutoConfiguration;
import org.springframework.boot.autoconfigure.web.client.RestTemplateAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.function.client.WebClientAutoConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.context.runner.WebApplicationContextRunner;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author livk
 */
class SpringHttpTest {

	final WebApplicationContextRunner contextRunner = new WebApplicationContextRunner()
		.withPropertyValues("spring.main.web-application-type=servlet")
		.withUserConfiguration(SpringHttpTest.Config.class)
		.withBean(SpringContextHolder.class, SpringContextHolder::new);

	@Test
	void test() {
		contextRunner.run(context -> {
			RestClient restClient = context.getBean(RestClient.class);
			RestTemplate restTemplate = context.getBean(RestTemplate.class);
			WebClient webClient = context.getBean(WebClient.class);
			assertNotNull(restTemplate);
			assertNotNull(webClient);
			assertNotNull(restClient);
			assertEquals(SpringContextHolder.getBean(RestTemplate.class), restTemplate);
			assertEquals(SpringContextHolder.getBean(WebClient.class), webClient);
			assertEquals(SpringContextHolder.getBean(RestClient.class), restClient);
		});
	}

	@TestConfiguration
	@EnableHttpClient({ HttpClientType.REST_TEMPLATE, HttpClientType.WEB_CLIENT, HttpClientType.REST_CLIENT })
	@ImportAutoConfiguration({ RestClientAutoConfiguration.class, RestTemplateAutoConfiguration.class,
			WebClientAutoConfiguration.class, ReactorNettyConfigurations.ReactorResourceFactoryConfiguration.class })
	static class Config {

	}

}
