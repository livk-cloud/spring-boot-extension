package com.livk.webflux.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.livk.crypto.support.AesSecurity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author livk
 */
@SpringBootTest
@AutoConfigureWebTestClient
class InfoControllerTest {

    @Autowired
    WebTestClient client;

    @Test
    void info() {
        AesSecurity security = new AesSecurity();
        String encoding = security.print(123456L, Locale.CHINA);
        client.get()
                .uri(uriBuilder -> uriBuilder.path("/info/{id}")
                        .queryParam("id", encoding)
                        .build(encoding))
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON)
                .expectBody(JsonNode.class)
                .value(jsonNode -> {
                    assertEquals(encoding, jsonNode.get("paramId").asText());
                    assertEquals(encoding, jsonNode.get("idStr").asText());
                });
    }
}
