/*
 * Copyright 2021-2024 spring-boot-extension the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *       https://www.apache.org/licenses/LICENSE-2.0
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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * @author livk
 */
class HttpClientImportSelectorTest {

	final HttpClientImportSelector importSelector = new HttpClientImportSelector();

	@Test
	void test() {
		{
			String[] imports = importSelector.selectImports(AnnotationMetadata.introspect(AllConfig.class));
			@SuppressWarnings("deprecation")
			String[] result = new String[] { WebClientConfiguration.class.getName(),
					RestClientConfiguration.class.getName(), RestTemplateConfiguration.class.getName() };
			assertArrayEquals(result, imports);
		}

		{
			String[] imports = importSelector.selectImports(AnnotationMetadata.introspect(TwoConfig.class));
			String[] result = new String[] { WebClientConfiguration.class.getName(),
					RestClientConfiguration.class.getName() };
			assertArrayEquals(result, imports);
		}

		{
			String[] imports = importSelector.selectImports(AnnotationMetadata.introspect(OneConfig.class));
			String[] result = new String[] { WebClientConfiguration.class.getName() };
			assertArrayEquals(result, imports);
		}
	}

	@SuppressWarnings("deprecation")
	@EnableHttpClient({ HttpClientType.WEB_CLIENT, HttpClientType.REST_CLIENT, HttpClientType.REST_TEMPLATE })
	static class AllConfig {

	}

	@EnableHttpClient({ HttpClientType.WEB_CLIENT, HttpClientType.REST_CLIENT })
	static class TwoConfig {

	}

	@EnableHttpClient({ HttpClientType.WEB_CLIENT })
	static class OneConfig {

	}

}
