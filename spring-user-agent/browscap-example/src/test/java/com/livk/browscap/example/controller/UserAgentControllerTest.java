package com.livk.browscap.example.controller;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * <p>
 * UserAgentControllerTest
 * </p>
 *
 * @author livk
 */
@SpringBootTest
@AutoConfigureWebTestClient(timeout = "15000")
class UserAgentControllerTest {

    @Autowired
    WebTestClient client;
    @Value("${server.port:8080}")
    private int port;

    @Test
    void getTest() {
        client.get()
                .uri(uriBuilder -> uriBuilder.host("localhost")
                        .port(port)
                        .path("/user-agent")
                        .build())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON)
                .expectBody(JsonNode.class)
                .value(System.out::println);
    }
}
