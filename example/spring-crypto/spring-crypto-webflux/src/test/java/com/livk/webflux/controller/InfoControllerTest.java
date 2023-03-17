package com.livk.webflux.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.livk.commons.jackson.JsonNodeUtils;
import com.livk.crypto.support.AesSecurity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Locale;
import java.util.Map;

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
        Map<String, String> body = Map.of("variableId", encoding, "paramId", encoding);
        client.post()
                .uri(uriBuilder -> uriBuilder.path("/info/{id}")
                        .queryParam("id", encoding)
                        .build(encoding))
                .bodyValue(body)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON)
                .expectBody(JsonNode.class)
                .value(jsonNode -> {
                    assertEquals(encoding, JsonNodeUtils.findNode(jsonNode, "id.paramId").asText());
                    assertEquals(encoding, JsonNodeUtils.findNode(jsonNode, "id.variableId").asText());
                    assertEquals(encoding, JsonNodeUtils.findNode(jsonNode, "body.paramId").asText());
                    assertEquals(encoding, JsonNodeUtils.findNode(jsonNode, "body.variableId").asText());
                });
    }
}
