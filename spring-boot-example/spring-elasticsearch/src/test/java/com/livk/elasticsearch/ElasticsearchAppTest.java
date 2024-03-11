package com.livk.elasticsearch;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.livk.elasticsearch.annotation.*;
import com.livk.elasticsearch.model.CreateIndex;
import com.livk.elasticsearch.model.DeleteIndex;
import com.livk.elasticsearch.template.ElasticsearchTemplate;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class ElasticsearchAppTest {

	private final ElasticsearchClient elasticsearchClient;

	private final ElasticsearchTemplate elasticsearchTemplate;

	ElasticsearchAppTest(ElasticsearchClient elasticsearchClient, ElasticsearchTemplate elasticsearchTemplate) {
		this.elasticsearchClient = elasticsearchClient;
		this.elasticsearchTemplate = elasticsearchTemplate;
	}

	@Test
	void contextLoads() {
		assertNotNull(elasticsearchClient);
	}

	@Test
	void testCreateIndexApi() {
		CreateIndex<Resource> createIndex = new CreateIndex<>("livk_res_1", "livk_res", Resource.class);
		CreateIndex<Project> createIndex2 = new CreateIndex<>("livk_pro_1", "livk_pro", Project.class);
		elasticsearchTemplate.createIndex(createIndex);
		elasticsearchTemplate.createIndex(createIndex2);
	}

	@Test
	void testDeleteIndexApi() {
		DeleteIndex deleteIndex = new DeleteIndex("livk_res_1", "livk_res");
		DeleteIndex deleteIndex2 = new DeleteIndex("livk_pro_1", "livk_pro");
		elasticsearchTemplate.deleteIndex(deleteIndex);
		elasticsearchTemplate.deleteIndex(deleteIndex2);
	}

	@Data
	@Index(analysis = @Analysis(
			filters = { @Filter(name = "livk_pinyin",
					options = { @Option(key = "type", value = "pinyin"),
							@Option(key = "keep_full_pinyin", value = "false"),
							@Option(key = "keep_joined_full_pinyin", value = "true"),
							@Option(key = "keep_original", value = "true"),
							@Option(key = "limit_first_letter_length", value = "16"),
							@Option(key = "remove_duplicated_term", value = "true"),
							@Option(key = "none_chinese_pinyin_tokenize", value = "false") }), },
			analyzers = {
					@Analyzer(name = "ik_pinyin", args = @Args(filter = "livk_pinyin", tokenizer = "ik_max_word")) }))
	static class Resource {

		@Field(type = Type.TEXT, searchAnalyzer = "ik_smart", analyzer = "ik_pinyin")
		private String name;

	}

	@Data
	@Index
	static class Project {

		@JsonSerialize(using = ToStringSerializer.class)
		@Field(type = Type.LONG)
		private Long businessKey;

	}

}
