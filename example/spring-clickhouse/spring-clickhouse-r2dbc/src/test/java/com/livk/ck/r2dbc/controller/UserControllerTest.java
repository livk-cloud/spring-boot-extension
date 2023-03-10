package com.livk.ck.r2dbc.controller;

import com.livk.ck.r2dbc.entity.User;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * <p>
 * UserControllerTest
 * </p>
 *
 * @author livk
 */
@SpringBootTest
@AutoConfigureWebTestClient(timeout = "15000")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserControllerTest {
    @Autowired
    WebTestClient client;

    @Order(2)
    @Test
    void testUsers() {
        client.get()
                .uri("/user")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(User.class)
                .value(users -> assertNotEquals(0, users.size()));
    }

    @Order(1)
    @Test
    void testSave() {
        User user = new User().setId(Integer.MAX_VALUE)
                .setAppId("appId")
                .setVersion("version")
                .setRegTime(new Date());
        client.post()
                .uri("/user")
                .bodyValue(user)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Order(3)
    @Test
    void testDelete() {
        client.delete()
                .uri("/user/{id}", Integer.MAX_VALUE)
                .exchange()
                .expectStatus()
                .isOk();
    }
}
