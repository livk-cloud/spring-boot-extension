/*
 * Copyright 2021-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.livk.elasticsearch;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import co.elastic.clients.elasticsearch.indices.IndexState;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.livk.autoconfigure.elasticsearch.ElasticsearchAutoConfiguration;
import com.livk.context.elasticsearch.annotation.Field;
import com.livk.context.elasticsearch.annotation.Index;
import com.livk.context.elasticsearch.annotation.Type;
import com.livk.context.elasticsearch.entity.Page;
import com.livk.context.elasticsearch.entity.Search;
import com.livk.context.elasticsearch.template.ElasticsearchDocumentTemplate;
import com.livk.context.elasticsearch.template.ElasticsearchIndexTemplate;
import com.livk.context.elasticsearch.template.ElasticsearchSearchTemplate;
import com.livk.testcontainers.DockerImageNames;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ssl.DefaultSslBundleRegistry;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.io.Serializable;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

/**
 * @author laokou
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestConfiguration
@ContextConfiguration(classes = { ElasticsearchAutoConfiguration.class, DefaultSslBundleRegistry.class })
@Testcontainers(disabledWithoutDocker = true, parallel = true)
class ElasticsearchApiTests {

	@Autowired
	private ElasticsearchIndexTemplate elasticsearchIndexTemplate;

	@Autowired
	private ElasticsearchSearchTemplate elasticsearchSearchTemplate;

	@Autowired
	private ElasticsearchDocumentTemplate elasticsearchDocumentTemplate;

	@Container
	@ServiceConnection
	static final ElasticsearchContainer elasticsearch = new ElasticsearchContainer(
			DockerImageNames.elasticsearch("9.1.3"))
		.withPassword("123456789");

	@BeforeAll
	static void beforeAll() throws InterruptedException {
		elasticsearch.start();
		Thread.sleep(Duration.ofSeconds(10L));
	}

	@AfterAll
	static void afterAll() {
		elasticsearch.stop();
	}

	@DynamicPropertySource
	static void configureProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.data.elasticsearch.uris", () -> "https://" + elasticsearch.getHttpHostAddress());
		registry.add("spring.data.elasticsearch.username", () -> "elastic");
		registry.add("spring.data.elasticsearch.password", () -> "123456789");
		registry.add("spring.data.elasticsearch.client-version", () -> "9.1.4");
		registry.add("spring.data.elasticsearch.version", () -> "9.1.3");
		registry.add("spring.data.elasticsearch.connection-timeout", () -> "60s");
		registry.add("spring.data.elasticsearch.socket-timeout", () -> "60s");
	}

	@Test
	void test_asyncApi() throws InterruptedException {
		// 异步创建索引
		Assertions.assertThatNoException()
			.isThrownBy(() -> elasticsearchIndexTemplate
				.asyncCreateIndex("iot_async_plugin_idx_1", "iot_async_plugin_idx", Test1.class,
						Executors.newSingleThreadExecutor())
				.join());
		Thread.sleep(Duration.ofSeconds(1));
		// 异步查看索引
		Map<String, IndexState> map = elasticsearchIndexTemplate.asyncGetIndex(List.of("iot_async_plugin_idx_1"))
			.join()
			.indices();
		Assertions.assertThat(map).hasSize(1);
		Assertions.assertThat(map.get("iot_async_plugin_idx_1")).isNotNull();
		// 异步创建文档
		Assertions.assertThatNoException()
			.isThrownBy(() -> elasticsearchDocumentTemplate
				.asyncCreateDocument("iot_async_plugin_idx_1", "1", new Test1(1L, "我是livk，你们好呀！"),
						Executors.newSingleThreadExecutor())
				.join());
		// 异步搜索文档【精准匹配】
		Query.Builder queryBuilder = new Query.Builder();
		TermQuery.Builder termQueryBuilder = new TermQuery.Builder();
		termQueryBuilder.field("name").value("我是livk，你们好呀！");
		queryBuilder.term(termQueryBuilder.build());
		Search search = new Search(null, 1, 10, queryBuilder.build());
		Page<Test1> page = elasticsearchSearchTemplate
			.asyncSearch(List.of("iot_async_plugin_idx_1"), search, Test1.class)
			.join();
		Assertions.assertThat(page.getTotal()).isEqualTo(1L);
		Test1 test1 = page.getRecords().getFirst();
		Assertions.assertThat(test1.getName()).isEqualTo("我是livk，你们好呀！");
		// 异步查看文档
		Test1 test2 = elasticsearchDocumentTemplate
			.asyncGetDocument("iot_async_plugin_idx_1", "1", Test1.class, Executors.newSingleThreadExecutor())
			.join();
		Assertions.assertThat(test2).isNotNull();
		Assertions.assertThat(test2.getName()).isEqualTo("我是livk，你们好呀！");
		// 异步删除文档
		Assertions.assertThatNoException()
			.isThrownBy(() -> elasticsearchDocumentTemplate
				.asyncDeleteDocument("iot_async_plugin_idx_1", test2.getId().toString(),
						Executors.newSingleThreadExecutor())
				.join());
		// 异步查看文档
		Test1 test3 = elasticsearchDocumentTemplate
			.asyncGetDocument("iot_async_plugin_idx_1", "1", Test1.class, Executors.newSingleThreadExecutor())
			.join();
		Assertions.assertThat(test3).isNull();
		// 异步批量创建文档
		Assertions.assertThatNoException()
			.isThrownBy(() -> elasticsearchDocumentTemplate
				.asyncBulkCreateDocuments("iot_async_plugin_idx_1", List.of(new Test1(2L, "KK")),
						Executors.newSingleThreadExecutor())
				.join());
		// 异步搜索文档【精准匹配】
		Query.Builder queryBuilder2 = new Query.Builder();
		TermQuery.Builder termQueryBuilder2 = new TermQuery.Builder();
		termQueryBuilder2.field("name").value("KK");
		queryBuilder2.term(termQueryBuilder2.build());
		Page<Test1> page2 = elasticsearchSearchTemplate
			.asyncSearch(List.of("iot_async_plugin_idx_1"), new Search(null, 1, 10, queryBuilder2.build()), Test1.class)
			.join();
		Assertions.assertThat(page2.getTotal()).isEqualTo(1L);
		// 异步删除索引
		Assertions.assertThatNoException()
			.isThrownBy(() -> elasticsearchIndexTemplate
				.asyncDeleteIndex(List.of("iot_async_plugin_idx_1"), Executors.newSingleThreadExecutor())
				.join());
		Thread.sleep(Duration.ofSeconds(1));
		// 异步判断索引是否存在
		Assertions
			.assertThat(elasticsearchIndexTemplate
				.asyncExist(List.of("iot_async_plugin_idx_1"), Executors.newSingleThreadExecutor())
				.join())
			.isFalse();
	}

	@Test
	void test_syncApi() throws IOException {
		// 同步创建索引
		Assertions.assertThatNoException()
			.isThrownBy(
					() -> elasticsearchIndexTemplate.createIndex("iot_plugin_idx_1", "iot_plugin_idx", Test1.class));
		// 同步查看索引
		Map<String, IndexState> map = elasticsearchIndexTemplate.getIndex(List.of("iot_plugin_idx_1")).indices();
		Assertions.assertThat(map).hasSize(1);
		Assertions.assertThat(map.get("iot_plugin_idx_1")).isNotNull();
		// 同步创建文档
		Assertions.assertThatNoException()
			.isThrownBy(() -> elasticsearchDocumentTemplate.createDocument("iot_plugin_idx_1", "1",
					new Test1(1L, "我是livk，你们好呀！")));
		// 同步搜索文档【精准匹配】
		Query.Builder queryBuilder = new Query.Builder();
		TermQuery.Builder termQueryBuilder = new TermQuery.Builder();
		termQueryBuilder.field("name").value("我是livk，你们好呀！");
		queryBuilder.term(termQueryBuilder.build());
		Search search = new Search(null, 1, 10, queryBuilder.build());
		Page<Test1> page = elasticsearchSearchTemplate.search(List.of("iot_plugin_idx_1"), search, Test1.class);
		Assertions.assertThat(page.getTotal()).isEqualTo(1L);
		Test1 test1 = page.getRecords().getFirst();
		Assertions.assertThat(test1.getName()).isEqualTo("我是livk，你们好呀！");
		// 同步查看文档
		Test1 test2 = elasticsearchDocumentTemplate.getDocument("iot_plugin_idx_1", "1", Test1.class);
		Assertions.assertThat(test2).isNotNull();
		Assertions.assertThat(test2.getName()).isEqualTo("我是livk，你们好呀！");
		// 同步删除文档
		Assertions.assertThatNoException()
			.isThrownBy(
					() -> elasticsearchDocumentTemplate.deleteDocument("iot_plugin_idx_1", test2.getId().toString()));
		// 同步查看文档
		Test1 test3 = elasticsearchDocumentTemplate.getDocument("iot_plugin_idx_1", "1", Test1.class);
		Assertions.assertThat(test3).isNull();
		// 同步批量创建文档
		Assertions.assertThatNoException()
			.isThrownBy(() -> elasticsearchDocumentTemplate.bulkCreateDocuments("iot_plugin_idx_1",
					List.of(new Test1(2L, "KK"))));
		// 同步搜索文档【精准匹配】
		Query.Builder queryBuilder2 = new Query.Builder();
		TermQuery.Builder termQueryBuilder2 = new TermQuery.Builder();
		termQueryBuilder2.field("name").value("KK");
		queryBuilder2.term(termQueryBuilder2.build());
		Page<Test1> page2 = elasticsearchSearchTemplate.search(List.of("iot_plugin_idx_1"),
				new Search(null, 1, 10, queryBuilder2.build()), Test1.class);
		Assertions.assertThat(page2.getTotal()).isEqualTo(1L);
		// 同步删除索引
		Assertions.assertThatNoException()
			.isThrownBy(() -> elasticsearchIndexTemplate.deleteIndex(List.of("iot_plugin_idx_1")));
		// 同步判断索引是否存在
		Assertions.assertThat(elasticsearchIndexTemplate.exist(List.of("iot_plugin_idx_1"))).isFalse();
	}

	@Index
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	static class Test1 implements Serializable {

		@JsonSerialize(using = ToStringSerializer.class)
		@Field(type = Type.LONG)
		private Long id;

		@Field(type = Type.KEYWORD)
		private String name;

	}

}
