package com.livk.browscap.example.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Map;

/**
 * <p>
 * UserAgentControllerTest
 * </p>
 *
 * @author livk
 * @date 2022/10/6
 */
@SpringBootTest
@AutoConfigureWebTestClient
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
                        .path("/user-agent").build())
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Map.class).value(System.out::println);
    }
}
