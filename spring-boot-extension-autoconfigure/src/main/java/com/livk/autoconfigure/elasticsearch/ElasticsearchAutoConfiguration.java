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

package com.livk.autoconfigure.elasticsearch;

import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.transport.rest5_client.Rest5ClientTransport;
import com.livk.context.elasticsearch.template.ElasticsearchDocumentTemplate;
import com.livk.context.elasticsearch.template.ElasticsearchIndexTemplate;
import com.livk.context.elasticsearch.template.ElasticsearchSearchTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

/**
 * @author spring project
 * @author laokou
 */
@Slf4j
@AutoConfiguration
@RequiredArgsConstructor
@Import({ ElasticsearchRest5ClientConfiguration.Rest5ClientBuilderConfig.class,
		ElasticsearchRest5ClientConfiguration.Rest5ClientConfig.class,
		ElasticsearchRest5ClientConfiguration.ElasticsearchTransportConfig.class,
		ElasticsearchRest5ClientConfiguration.JacksonJsonpMapperConfig.class,
		ElasticsearchRest5ClientConfiguration.Rest5ClientOptionsConfig.class })
@ConditionalOnClass(SpringElasticsearchProperties.class)
@EnableConfigurationProperties(SpringElasticsearchProperties.class)
public class ElasticsearchAutoConfiguration {

	@Bean(name = "elasticsearchClient", destroyMethod = "close")
	@ConditionalOnMissingBean(ElasticsearchClient.class)
	ElasticsearchClient elasticsearchClient(Rest5ClientTransport rest5ClientTransport) {
		return new ElasticsearchClient(rest5ClientTransport);
	}

	@Bean(name = "elasticsearchAsyncClient", destroyMethod = "close")
	@ConditionalOnMissingBean(ElasticsearchAsyncClient.class)
	ElasticsearchAsyncClient elasticsearchAsyncClient(Rest5ClientTransport rest5ClientTransport) {
		return new ElasticsearchAsyncClient(rest5ClientTransport);
	}

	@Bean(name = "elasticsearchDocumentTemplate")
	@ConditionalOnMissingBean(ElasticsearchDocumentTemplate.class)
	@ConditionalOnClass({ ElasticsearchAsyncClient.class, ElasticsearchClient.class })
	ElasticsearchDocumentTemplate elasticsearchDocumentTemplate(ElasticsearchClient elasticsearchClient,
			ElasticsearchAsyncClient elasticsearchAsyncClient) {
		return new ElasticsearchDocumentTemplate(elasticsearchClient, elasticsearchAsyncClient);
	}

	@Bean(name = "elasticsearchIndexTemplate")
	@ConditionalOnMissingBean(ElasticsearchIndexTemplate.class)
	@ConditionalOnClass({ ElasticsearchAsyncClient.class, ElasticsearchClient.class })
	ElasticsearchIndexTemplate elasticsearchIndexTemplate(ElasticsearchClient elasticsearchClient,
			ElasticsearchAsyncClient elasticsearchAsyncClient) {
		return new ElasticsearchIndexTemplate(elasticsearchClient, elasticsearchAsyncClient);
	}

	@Bean(name = "elasticsearchSearchTemplate")
	@ConditionalOnMissingBean(ElasticsearchSearchTemplate.class)
	@ConditionalOnClass({ ElasticsearchAsyncClient.class, ElasticsearchClient.class })
	ElasticsearchSearchTemplate elasticsearchSearchTemplate(ElasticsearchClient elasticsearchClient,
			ElasticsearchAsyncClient elasticsearchAsyncClient) {
		return new ElasticsearchSearchTemplate(elasticsearchClient, elasticsearchAsyncClient);
	}

}
