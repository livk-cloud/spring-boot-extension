package com.livk.doc.webflux.example.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.livk.commons.jackson.JsonNodeUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author livk
 */
@SpringBootTest
@AutoConfigureWebTestClient
class DocControllerTest {
    @Autowired
    WebTestClient client;

    @Test
    public void test() {
        client.get()
                .uri("/v3/api-docs")
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON)
                .expectBody(JsonNode.class)
                .value(jsonNode -> {
                    assertEquals("3.0.1", JsonNodeUtils.findNode(jsonNode, "openapi").asText());
                });
    }
}
