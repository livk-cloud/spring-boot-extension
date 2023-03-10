package com.livk.netty.client.controller;

import com.livk.commons.jackson.JacksonUtils;
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
                .bodyValue(JacksonUtils.writeValueAsString(body))
                .exchange()
                .expectStatus()
                .isOk();
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme
