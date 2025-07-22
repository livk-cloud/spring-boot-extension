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

package com.livk.commons.http;

import com.livk.commons.http.support.OkHttpClientHttpRequestFactory;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.web.client.RestClientAutoConfiguration;
import org.springframework.boot.test.context.runner.WebApplicationContextRunner;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
class RestClientConfigurationTests {

	final WebApplicationContextRunner contextRunner = new WebApplicationContextRunner()
		.withPropertyValues("spring.main.web-application-type=servlet")
		.withConfiguration(AutoConfigurations.of(RestClientConfiguration.class, RestClientAutoConfiguration.class));

	@Test
	void test() {
		contextRunner.run(context -> {
			assertThat(context).hasSingleBean(RestClient.class);
			assertThat(context).hasBean("restClientCustomizer");
			assertThat(context).hasSingleBean(OkHttpClientHttpRequestFactory.class);
		});
	}

}
