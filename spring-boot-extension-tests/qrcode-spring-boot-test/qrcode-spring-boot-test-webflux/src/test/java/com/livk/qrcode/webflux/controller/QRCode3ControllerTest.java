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

package com.livk.qrcode.webflux.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * @author livk
 */
@SpringBootTest
@AutoConfigureWebTestClient(timeout = "15000")
class QRCode3ControllerTest {

	@Autowired
	WebTestClient client;

	@Test
	void textCode() {
		MultipartBodyBuilder builder = new MultipartBodyBuilder();
		builder.part("file", new ClassPathResource("qrCode.png")).filename("file");

		client.post()
			.uri("/qrcode3")
			.bodyValue(builder.build())
			.exchange()
			.expectStatus()
			.isOk()
			.expectBody(String.class)
			.isEqualTo("QrCode");
	}

}
