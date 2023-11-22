package com.livk.spring.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.livk.commons.jackson.util.JsonNodeUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebFlux;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author livk
 */
@WebFluxTest
@AutoConfigureWebFlux
@AutoConfigureWebTestClient
class GreetingControllerTest {

	@Autowired
	WebTestClient client;

	@Test
	void greeting() {
		client.get()
			.uri("/greeting")
			.exchange()
			.expectStatus()
			.isOk()
			.expectHeader()
			.contentType(MediaType.APPLICATION_JSON)
			.expectBody(JsonNode.class)
			.value(jsonNode -> {
				assertEquals("hello,World!", JsonNodeUtils.findNode(jsonNode, "content").asText());
			});
	}

}
