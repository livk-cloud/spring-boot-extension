package com.livk.graphql.mybatis.controller;

import com.livk.graphql.mybatis.mapper.AuthorMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.graphql.test.tester.HttpGraphQlTester;
import org.springframework.graphql.test.tester.WebGraphQlTester;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * <p>
 * AuthorControllerTest
 * </p>
 *
 * @author livk
 * @date 2022/11/25
 */
@SpringBootTest
@AutoConfigureWebTestClient
class AuthorControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    AuthorMapper authorMapper;

    @Value("${spring.graphql.path:/graphql}")
    String graphqlPath;

    WebGraphQlTester tester;

    @BeforeEach
    public void init() {
        WebTestClient.Builder builder = webTestClient.mutate().baseUrl(graphqlPath);
        tester = HttpGraphQlTester.builder(builder).build();
    }

    @Test
    void createAuthor() {
        authorMapper.clear();
        //language=GraphQL
        String document = """
                mutation{
                    createAuthor(dto: {
                        idCardNo: "341234567891234567",
                        name: "汪云飞",
                        age: 38
                    })
                }""";
        Boolean result = tester.document(document)
                .execute()
                .path("createAuthor")
                .entity(Boolean.class)
                .get();
        assertTrue(result);
        //language=GraphQL
        String d2 = """
                mutation{
                    createAuthor(dto: {
                        idCardNo: "341234567891234568",
                        name: "罗伯特C.马丁",
                        age: 70 }
                    )
                }""";
        Boolean result2 = tester.document(d2)
                .execute()
                .path("createAuthor")
                .entity(Boolean.class)
                .get();
        assertTrue(result2);
    }
}
