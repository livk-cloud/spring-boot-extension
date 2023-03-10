package com.livk.redisearch.webflux.controller;

import com.livk.redisearch.webflux.entity.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertLinesMatch;

/**
 * @author livk
 */
@SpringBootTest
@AutoConfigureWebTestClient
class StudentControllerTest {
    @Autowired
    WebTestClient client;


    @Test
    void testList() {
        client.get()
                .uri("/student")
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Student.class)
                .value(students -> {
                    List<String> result = students.stream()
                            .map(Student::getName).toList();
                    List<String> list = List.of("livk-0", "livk-1", "livk-2", "livk-3",
                            "livk-4", "livk-5", "livk-6", "livk-7", "livk-8", "livk-9");
                    assertLinesMatch(list, result);
                });

        client.get()
                .uri(uriBuilder -> uriBuilder.path("/student")
                        .queryParam("query", "@class:{1}{2}{3}")
                        .build("{", "1班", "}"))
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Student.class)
                .value(students -> {
                    List<String> result = students.stream()
                            .map(Student::getName).toList();
                    List<String> list = List.of("livk-0");
                    assertLinesMatch(list, result);
                });

        client.get()
                .uri(uriBuilder -> uriBuilder.path("/student")
                        .queryParam("query", "livk")
                        .build())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Student.class)
                .value(students -> {
                    List<String> result = students.stream()
                            .map(Student::getName).toList();
                    List<String> list = List.of("livk-0", "livk-1", "livk-2", "livk-3",
                            "livk-4", "livk-5", "livk-6", "livk-7", "livk-8", "livk-9");
                    assertLinesMatch(list, result);
                });

        client.get()
                .uri(uriBuilder -> uriBuilder.path("/student")
                        .queryParam("query", "女")
                        .build())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Student.class)
                .value(System.out::println);

        client.get()
                .uri(uriBuilder -> uriBuilder.path("/student")
                        .queryParam("query", "是一个学生")
                        .build())
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Student.class)
                .value(System.out::println);
    }
}
