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

package com.livk.netty.client.controller;

import com.livk.commons.jackson.JsonMapperUtils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Map;

/**
 * @author livk
 */
@Disabled("需要netty server服务器")
@SpringBootTest
@AutoConfigureWebTestClient
class MseControllerTest {

	@Autowired
	WebTestClient client;

	@Test
	void testSend() {
		Map<String, String> body = Map.of("username", "root", "password", "root");
		client.post()
			.uri("/msg")
			.contentType(MediaType.APPLICATION_JSON)
			.bodyValue(JsonMapperUtils.writeValueAsString(body))
			.exchange()
			.expectStatus()
			.isOk();
	}

}

// Generated with love by TestMe :) Please report issues and submit feature requests at:
// http://weirddev.com/forum#!/testme
