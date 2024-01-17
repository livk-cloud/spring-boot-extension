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

package com.livk.crypto.webflux.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.livk.commons.jackson.util.JsonNodeUtils;
import com.livk.crypto.CryptoType;
import com.livk.crypto.support.PbeSecurity;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Locale;
import java.util.Map;

/**
 * @author livk
 */
@SpringBootTest
@AutoConfigureWebTestClient
class InfoWebFluxControllerTest {

	@Autowired
	WebTestClient client;

	@Autowired
	PbeSecurity pbeSecurity;

	@Test
	void infoGet() {
		String encodingStr = pbeSecurity.print(654321L, Locale.CHINA);
		String encoding = CryptoType.PBE.wrapper(encodingStr);
		client.get()
			.uri(uriBuilder -> uriBuilder.path("/info").queryParam("id", encoding).build())
			.header("id", encoding)
			.exchange()
			.expectStatus()
			.isOk()
			.expectHeader()
			.contentType(MediaType.APPLICATION_JSON)
			.expectBody(JsonNode.class)
			.value(jsonNode -> JsonNodeUtils.findNode(jsonNode, "id.paramId").asText(), Is.is(encoding))
			.value(jsonNode -> JsonNodeUtils.findNode(jsonNode, "id.headerId").asText(), Is.is(encoding));
	}

	@Test
	void infoPost() {
		String encodingStr = pbeSecurity.print(654321L, Locale.CHINA);
		String encoding = CryptoType.PBE.wrapper(encodingStr);
		Map<String, String> body = Map.of("headerId", encoding, "paramId", encoding);
		client.post()
			.uri("/info")
			.bodyValue(body)
			.accept(MediaType.APPLICATION_JSON)
			.exchange()
			.expectStatus()
			.isOk()
			.expectHeader()
			.contentType(MediaType.APPLICATION_JSON)
			.expectBody(JsonNode.class)
			.value(jsonNode -> JsonNodeUtils.findNode(jsonNode, "body.paramId").asText(), Is.is(encoding))
			.value(jsonNode -> JsonNodeUtils.findNode(jsonNode, "body.headerId").asText(), Is.is(encoding));
	}

}
