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

import lombok.Data;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collections;
import java.util.Set;

/**
 * @author laokou
 */
@Data
@Component
@ConfigurationProperties("spring.data.elasticsearch")
public class SpringElasticsearchProperties {

	private Set<String> endpoints = Collections.singleton("localhost:9200");

	private String username;

	private String password;

	private String proxy;

	private Duration connectionTimeout = Duration.ofSeconds(15L);

	private Duration socketTimeout = Duration.ofSeconds(30L);

	private boolean socketKeepAlive = false;

	private String pathPrefix;

	private boolean useSsl = false;

	private String version = "9.1.3";

	private String clientVersion = "9.1.4";

	private final ElasticsearchProperties.Restclient restclient = new ElasticsearchProperties.Restclient();

}
