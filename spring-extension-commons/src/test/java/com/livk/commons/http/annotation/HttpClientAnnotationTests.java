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

package com.livk.commons.http.annotation;

import com.livk.commons.http.HttpClientImportSelector;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
class HttpClientAnnotationTests {

	@Test
	void enableHttpClientMetadata() {
		assertThat(EnableHttpClient.class.getAnnotation(Retention.class).value()).isEqualTo(RetentionPolicy.RUNTIME);
		assertThat(EnableHttpClient.class.getAnnotation(Target.class).value()).containsExactly(ElementType.TYPE);
		assertThat(EnableHttpClient.class.getAnnotation(Import.class).value())
			.containsExactly(HttpClientImportSelector.class);
	}

	@Test
	void shortcutAnnotationsDeclareClientType() {
		assertThat(EnableWebClient.class.getAnnotation(EnableHttpClient.class).value())
			.containsExactly(HttpClientType.WEB_CLIENT);
		assertThat(EnableRestClient.class.getAnnotation(EnableHttpClient.class).value())
			.containsExactly(HttpClientType.REST_CLIENT);
	}

	@Test
	void httpClientTypeReturnsAnnotationType() {
		assertThat(HttpClientType.WEB_CLIENT.annotationType()).isEqualTo(EnableWebClient.class);
		assertThat(HttpClientType.REST_CLIENT.annotationType()).isEqualTo(EnableRestClient.class);
	}

}
