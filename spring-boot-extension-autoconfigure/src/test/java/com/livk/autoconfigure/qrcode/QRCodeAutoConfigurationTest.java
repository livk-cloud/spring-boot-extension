/*
 * Copyright 2021-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * you may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.livk.autoconfigure.qrcode;

import com.livk.context.qrcode.resolver.QrCodeMethodReturnValueHandler;
import com.livk.context.qrcode.resolver.ReactiveQrCodeMethodReturnValueHandler;
import com.livk.context.qrcode.support.GoogleQrCodeManager;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.web.reactive.WebFluxAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.boot.test.context.runner.ReactiveWebApplicationContextRunner;
import org.springframework.boot.test.context.runner.WebApplicationContextRunner;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
class QRCodeAutoConfigurationTest {

	@Test
	void googleQRCodeManager() {
		ApplicationContextRunner contextRunner = new ApplicationContextRunner()
			.withBean(Jackson2ObjectMapperBuilder.class, Jackson2ObjectMapperBuilder::new)
			.withConfiguration(AutoConfigurations.of(QRCodeAutoConfiguration.class));

		contextRunner.run((context) -> assertThat(context).hasSingleBean(GoogleQrCodeManager.class));
	}

	@Test
	void testServlet() {
		WebApplicationContextRunner contextRunner = new WebApplicationContextRunner()
			.withBean(Jackson2ObjectMapperBuilder.class, Jackson2ObjectMapperBuilder::new)
			.withConfiguration(AutoConfigurations.of(WebMvcAutoConfiguration.class, QRCodeAutoConfiguration.class));

		contextRunner.run(context -> {
			RequestMappingHandlerAdapter adapter = context.getBean(RequestMappingHandlerAdapter.class);
			assertThat(adapter.getReturnValueHandlers()).isNotNull()
				.hasAtLeastOneElementOfType(QrCodeMethodReturnValueHandler.class);
		});
	}

	@Test
	void testReactive() {
		ReactiveWebApplicationContextRunner contextRunner = new ReactiveWebApplicationContextRunner()
			.withBean(Jackson2ObjectMapperBuilder.class, Jackson2ObjectMapperBuilder::new)
			.withConfiguration(AutoConfigurations.of(WebFluxAutoConfiguration.class, QRCodeAutoConfiguration.class));

		contextRunner.run(context -> assertThat(context).hasSingleBean(ReactiveQrCodeMethodReturnValueHandler.class));
	}

}
