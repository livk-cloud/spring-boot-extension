package com.livk.graphql.mybatis.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.graphql.test.tester.HttpGraphQlTester;
import org.springframework.graphql.test.tester.WebGraphQlTester;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * <p>
 * GreetingControllerTest
 * </p>
 *
 * @author livk
 */
@SpringBootTest
@AutoConfigureWebTestClient(timeout = "15000")
class GreetingControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @Value("${spring.graphql.path:/graphql}")
    String graphqlPath;

    WebGraphQlTester tester;

    @BeforeEach
    public void init() {
        WebTestClient.Builder builder = webTestClient.mutate().baseUrl(graphqlPath);
        tester = HttpGraphQlTester.builder(builder).build();
    }

    @Test
    @SuppressWarnings("rawtypes")
    void greetings() {
        //language=GraphQL
        String document = """
                subscription {
                  greetings
                }""";
        Map result = tester.document(document)
                .execute()
                .path("upstreamPublisher")
                .entity(Map.class)
                .get();
        assertNotNull(result);
    }
}
