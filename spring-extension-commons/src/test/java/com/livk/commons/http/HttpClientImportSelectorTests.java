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
	void testSelectImportsForTwoConfig() {
		String[] imports = importSelector.selectImports(AnnotationMetadata.introspect(TwoConfig.class));
		String[] expected = new String[] { WebClientConfiguration.class.getName(),
				RestClientConfiguration.class.getName() };
		assertThat(imports).containsExactly(expected);
	}

	@Test
	void testSelectImportsForOneConfig() {
		String[] imports = importSelector.selectImports(AnnotationMetadata.introspect(OneConfig.class));
		String[] expected = new String[] { WebClientConfiguration.class.getName() };
		assertThat(imports).containsExactly(expected);
	}

	@EnableHttpClient({ HttpClientType.WEB_CLIENT, HttpClientType.REST_CLIENT })
	static class TwoConfig {

	}

	@EnableHttpClient({ HttpClientType.WEB_CLIENT })
	static class OneConfig {

	}

}
