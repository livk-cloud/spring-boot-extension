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

import com.livk.commons.http.annotation.EnableHttpClient;
import com.livk.commons.http.annotation.HttpClientType;
import org.junit.jupiter.api.Test;
import org.springframework.core.type.AnnotationMetadata;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
class HttpClientImportSelectorTests {

	final HttpClientImportSelector importSelector = new HttpClientImportSelector();

	@Test
	void selectImportsWithWebClientOnly() {
		String[] imports = importSelector.selectImports(AnnotationMetadata.introspect(WebClientOnlyConfig.class));
		assertThat(imports).containsExactly(WebClientConfiguration.class.getName());
	}

	@Test
	void selectImportsWithRestClientOnly() {
		String[] imports = importSelector.selectImports(AnnotationMetadata.introspect(RestClientOnlyConfig.class));
		assertThat(imports).containsExactly(RestClientConfiguration.class.getName());
	}

	@Test
	void selectImportsWithBothClientsPreservesOrder() {
		String[] imports = importSelector.selectImports(AnnotationMetadata.introspect(BothClientsConfig.class));
		assertThat(imports).containsExactly(WebClientConfiguration.class.getName(),
				RestClientConfiguration.class.getName());
	}

	@Test
	void selectImportsWithReversedOrderPreservesAnnotationOrder() {
		String[] imports = importSelector.selectImports(AnnotationMetadata.introspect(ReversedOrderConfig.class));
		assertThat(imports).containsExactly(RestClientConfiguration.class.getName(),
				WebClientConfiguration.class.getName());
	}

	@EnableHttpClient(HttpClientType.WEB_CLIENT)
	static class WebClientOnlyConfig {

	}

	@EnableHttpClient(HttpClientType.REST_CLIENT)
	static class RestClientOnlyConfig {

	}

	@EnableHttpClient({ HttpClientType.WEB_CLIENT, HttpClientType.REST_CLIENT })
	static class BothClientsConfig {

	}

	@EnableHttpClient({ HttpClientType.REST_CLIENT, HttpClientType.WEB_CLIENT })
	static class ReversedOrderConfig {

	}

}
