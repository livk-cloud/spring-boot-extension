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

package com.livk.spring;

import com.livk.starter01.AnnoTest;
import com.livk.starter01.LivkDemo;
import com.livk.starter01.LivkTestDemo;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * <p>
 * LivkTest
 * </p>
 *
 * @author livk
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LivkTest {

	private final LivkDemo livkDemo;

	private final LivkTestDemo livkTestDemo;

	private final AnnoTest annoTest;

	private final WebClient webClient;

	private final RestTemplate restTemplate;

	@Value("${spring.github.username}")
	public String username;

	@PostConstruct
	public void show() {
		livkDemo.show();
		livkTestDemo.show();
		annoTest.show();
		log.info(username);
		log.info("restTemplate:{}", restTemplate);
		log.info("webClient:{}", webClient);
	}

}
