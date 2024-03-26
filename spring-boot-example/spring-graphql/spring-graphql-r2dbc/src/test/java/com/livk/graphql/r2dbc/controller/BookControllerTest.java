/*
 * Copyright 2021-2024 spring-boot-extension the original author or authors.
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
 */

package com.livk.graphql.r2dbc.controller;

import com.livk.graphql.r2dbc.entity.Author;
import com.livk.graphql.r2dbc.entity.Book;
import com.livk.graphql.r2dbc.repository.AuthorRepository;
import com.livk.graphql.r2dbc.repository.BookRepository;
import java.util.List;
import java.util.Map;

import com.livk.testcontainers.containers.PostgresqlContainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.graphql.test.tester.HttpGraphQlTester;
import org.springframework.graphql.test.tester.WebGraphQlTester;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * <p>
 * BookControllerTest
 * </p>
 *
 * @author livk
 */
@SpringBootTest
@AutoConfigureWebTestClient(timeout = "15000")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Testcontainers(disabledWithoutDocker = true)
class BookControllerTest {

	@Container
	@ServiceConnection
	static PostgresqlContainer postgresql = new PostgresqlContainer().withEnv("POSTGRES_PASSWORD", "123456")
		.withDatabaseName("graphql");

	@DynamicPropertySource
	static void properties(DynamicPropertyRegistry registry) {
		registry.add("spring.r2dbc.username", postgresql::getUsername);
		registry.add("spring.r2dbc.password", postgresql::getPassword);
		registry.add("spring.r2dbc.url", () -> "r2dbc:postgres://" + postgresql.getHost() + ":"
				+ postgresql.getMappedPort(5432) + "/" + postgresql.getDatabaseName());
	}

	@Autowired
	WebTestClient webTestClient;

	@Value("${spring.graphql.path:/graphql}")
	String graphqlPath;

	@Autowired
	BookRepository bookRepository;

	@Autowired
	AuthorRepository authorRepository;

	WebGraphQlTester tester;

	@BeforeEach
	public void init() {
		WebTestClient.Builder builder = webTestClient.mutate().baseUrl(graphqlPath);
		tester = HttpGraphQlTester.builder(builder).build();
	}

	@Order(3)
	@Test
	@SuppressWarnings("rawtypes")
	void list() {
		// language=GraphQL
		String document = """
				query{
				    bookList {
				        title
				        author {
				            name
				        }
				    }
				}""";
		List<Map> result = tester.document(document).execute().path("bookList").entityList(Map.class).get();
		assertNotNull(result);

		// language=GraphQL
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
		List<Map> r1 = tester.document(d1).execute().path("bookList").entityList(Map.class).get();
		assertNotNull(r1);
	}

	@Order(4)
	@Test
	@SuppressWarnings("rawtypes")
	void bookByIsbn() {
		// language=GraphQL
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
		Map result = tester.document(document).execute().path("bookByIsbn").entity(Map.class).get();
		assertNotNull(result);
	}

	@Order(2)
	@Test
	void createBook() {
		bookRepository.deleteAll().subscribe();
		// language=GraphQL
		String d1 = """
				mutation{
				    createBook(dto: {
				        isbn: "9787121282089",
				        title: "JavaEE开发的颠覆者：Spring Boot实战",
				        pages: 524,
				        authorIdCardNo: "341234567891234567"
				    } ){ title pages }
				}""";
		// language=GraphQL
		String d2 = """
				mutation{
				    createBook(dto: {
				        isbn: "9787121377921",
				        title: "从企业级开发到云原生微服务:Spring Boot实战",
				        pages: 504,
				        authorIdCardNo: "341234567891234567"
				    } ){ title pages }
				}""";
		// language=GraphQL
		String d3 = """
				mutation{
				    createBook(dto: {
				        isbn: "9787121347962",
				        title: "架构整洁之道",
				        pages: 348,
				        authorIdCardNo: "341234567891234568"
				    } ){ title pages }
				}""";

		Book r1 = tester.document(d1).execute().path("createBook").entity(Book.class).get();
		assertNotNull(r1);

		Book r2 = tester.document(d2).execute().path("createBook").entity(Book.class).get();
		assertNotNull(r2);

		Book r3 = tester.document(d3).execute().path("createBook").entity(Book.class).get();
		assertNotNull(r3);
	}

	@Order(1)
	@Test
	void createAuthor() {
		authorRepository.deleteAll().subscribe();
		// language=GraphQL
		String document = """
				mutation{
				    createAuthor(dto: {
				        idCardNo: "341234567891234567",
				        name: "汪云飞",
				        age: 38
				    }){name age}
				}""";
		Author result = tester.document(document).execute().path("createAuthor").entity(Author.class).get();
		assertNotNull(result);
		// language=GraphQL
		String d2 = """
				mutation{
				    createAuthor(dto: {
				        idCardNo: "341234567891234568",
				        name: "罗伯特C.马丁",
				        age: 70
				    }){name age}
				}""";
		Author result2 = tester.document(d2).execute().path("createAuthor").entity(Author.class).get();
		assertNotNull(result2);
	}

}
