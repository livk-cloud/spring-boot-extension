/*
 * Copyright 2021 spring-boot-extension the original author or authors.
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
 *
 */

package com.livk.crypto.webflux.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.livk.commons.jackson.JsonNodeUtils;
import com.livk.crypto.CryptoType;
import com.livk.crypto.support.PbeSecurity;
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
class InfoWebFluxControllerTest {

    @Autowired
    WebTestClient client;

    @Autowired
    PbeSecurity pbeSecurity;

    @Test
    void infoGet() {
        String encodingStr = pbeSecurity.print(123456L, Locale.CHINA);
        String encoding = CryptoType.PBE.wrapper(encodingStr);
        Map<String, String> body = Map.of("variableId", encoding, "paramId", encoding);
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
                    assertEquals(encoding, JsonNodeUtils.findNode(jsonNode, "id.paramId").asText());
                    assertEquals(encoding, JsonNodeUtils.findNode(jsonNode, "id.variableId").asText());
                });
    }

    @Test
    void infoPost() {
        String encodingStr = pbeSecurity.print(123456L, Locale.CHINA);
        String encoding = CryptoType.PBE.wrapper(encodingStr);
        Map<String, String> body = Map.of("variableId", encoding, "paramId", encoding);
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
                .value(jsonNode -> {
                    assertEquals(encoding, JsonNodeUtils.findNode(jsonNode, "body.paramId").asText());
                    assertEquals(encoding, JsonNodeUtils.findNode(jsonNode, "body.variableId").asText());
                });
    }
}
