package com.livk.graphql.mybatis.controller;

import com.livk.graphql.mybatis.mapper.BookMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.graphql.test.tester.HttpGraphQlTester;
import org.springframework.graphql.test.tester.WebGraphQlTester;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * <p>
 * BookControllerTest
 * </p>
 *
 * @author livk
 * @date 2022/11/25
 */
@SpringBootTest
@AutoConfigureWebTestClient
class BookControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @Value("${spring.graphql.path:/graphql}")
    String graphqlPath;

    @Autowired
    BookMapper bookMapper;

    WebGraphQlTester tester;

    @BeforeEach
    public void init() {
        WebTestClient.Builder builder = webTestClient.mutate().baseUrl(graphqlPath);
        tester = HttpGraphQlTester.builder(builder).build();
    }

    @Test
    @SuppressWarnings("rawtypes")
    void list() {
        //language=GraphQL
        String document = """
                query{
                    bookList {
                        title
                        author {
                            name
                        }
                    }
                }""";
        List<Map> result = tester.document(document)
                .execute()
                .path("bookList")
                .entityList(Map.class)
                .get();
        assertNotNull(result);

        //language=GraphQL
        String d1 = """
                query{
                  bookList {
                    isbn
                    title
                    author {
                      name
                      age
                    }
                  }
                }""";
        List<Map> r1 = tester.document(d1)
                .execute()
                .path("bookList")
                .entityList(Map.class)
                .get();
        assertNotNull(r1);
    }

    @Test
    @SuppressWarnings("rawtypes")
    void bookByIsbn() {
        //language=GraphQL
        String document = """
                query{
                    bookByIsbn(isbn: "9787121377921"){
                        isbn
                        title
                        author {
                            name
                            age
                        }
                    }
                }""";
        Map result = tester.document(document)
                .execute()
                .path("bookByIsbn")
                .entity(Map.class)
                .get();
        assertNotNull(result);
    }

    @Test
    void createBook() {
        bookMapper.clear();
        //language=GraphQL
        String d1 = """
                mutation{
                    createBook(dto: {
                        isbn: "9787121282089",
                        title: "JavaEE开发的颠覆者：Spring Boot实战",
                        pages: 524,
                        authorIdCardNo: "341234567891234567"
                    } )
                }""";
        //language=GraphQL
        String d2 = """
                mutation{
                    createBook(dto: {
                        isbn: "9787121377921",
                        title: "从企业级开发到云原生微服务:Spring Boot实战",
                        pages: 504,
                        authorIdCardNo: "341234567891234567"
                    } )
                }""";
        //language=GraphQL
        String d3 = """
                mutation{
                    createBook(dto: {
                        isbn: "9787121347962",
                        title: "架构整洁之道",
                        pages: 348,
                        authorIdCardNo: "341234567891234568"
                    } )
                }""";

        Boolean r1 = tester.document(d1)
                .execute()
                .path("createBook")
                .entity(Boolean.class)
                .get();
        assertTrue(r1);

        Boolean r2 = tester.document(d2)
                .execute()
                .path("createBook")
                .entity(Boolean.class)
                .get();
        assertTrue(r2);

        Boolean r3 = tester.document(d3)
                .execute()
                .path("createBook")
                .entity(Boolean.class)
                .get();
        assertTrue(r3);
    }
}
