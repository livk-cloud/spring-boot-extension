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

package com.livk.spring;

import com.livk.commons.http.annotation.EnableHttpClient;
import com.livk.commons.http.annotation.HttpClientType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author livk
 */
@Slf4j
@EnableHttpClient({ HttpClientType.WEB_CLIENT, HttpClientType.REST_CLIENT })
@SpringBootApplication
public class App {

	public static void main(String[] args) {
		System.setProperty("server.port", "9099");
		SpringApplication.run(App.class, args);
	}

	@Bean
	public ApplicationRunner applicationRunner(WebClient webClient, RestClient restClient) {

		return args -> {
			log.info("webClient:{}", webClient);
			log.info("restClient:{}", restClient);
		};
	}

}
