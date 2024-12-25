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

package com.livk.autoconfigure.fastexcel;

import com.livk.commons.util.ReflectionUtils;
import com.livk.context.fastexcel.resolver.ExcelMethodArgumentResolver;
import com.livk.context.fastexcel.resolver.ExcelMethodReturnValueHandler;
import com.livk.context.fastexcel.resolver.ReactiveExcelMethodArgumentResolver;
import com.livk.context.fastexcel.resolver.ReactiveExcelMethodReturnValueHandler;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.WebFluxAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.context.runner.ReactiveWebApplicationContextRunner;
import org.springframework.boot.test.context.runner.WebApplicationContextRunner;
import org.springframework.web.reactive.result.method.HandlerMethodArgumentResolver;
import org.springframework.web.reactive.result.method.annotation.ArgumentResolverConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.lang.reflect.Field;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author livk
 */
class FastExcelAutoConfigurationTest {

	@Test
	void testServlet() {
		WebApplicationContextRunner contextRunner = new WebApplicationContextRunner()
			.withConfiguration(AutoConfigurations.of(FastExcelAutoConfiguration.class));

		contextRunner.withUserConfiguration(WebMvcConfig.class).run(context -> {
			RequestMappingHandlerAdapter adapter = context.getBean(RequestMappingHandlerAdapter.class);
			assertThat(adapter.getArgumentResolvers()).isNotNull()
				.hasAtLeastOneElementOfType(ExcelMethodArgumentResolver.class);
			assertThat(adapter.getReturnValueHandlers()).isNotNull()
				.hasAtLeastOneElementOfType(ExcelMethodReturnValueHandler.class);
		});
	}

	@Test
	@SuppressWarnings("unchecked")
	void testReactive() {
		ReactiveWebApplicationContextRunner contextRunner = new ReactiveWebApplicationContextRunner()
			.withConfiguration(AutoConfigurations.of(FastExcelAutoConfiguration.class));

		contextRunner.withUserConfiguration(WebFluxConfig.class).run(context -> {
			org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerAdapter adapter = context
				.getBean(org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerAdapter.class);

			Field customResolversField = ReflectionUtils.findField(ArgumentResolverConfigurer.class, "customResolvers");
			assertNotNull(customResolversField);
			List<HandlerMethodArgumentResolver> customResolvers = (List<HandlerMethodArgumentResolver>) ReflectionUtils
				.getDeclaredFieldValue(customResolversField, adapter.getArgumentResolverConfigurer());
			assertThat(customResolvers).isNotNull()
				.hasAtLeastOneElementOfType(ReactiveExcelMethodArgumentResolver.class);

			assertThat(context).hasSingleBean(ReactiveExcelMethodReturnValueHandler.class);
		});
	}

	@TestConfiguration
	@ImportAutoConfiguration(WebMvcAutoConfiguration.class)
	static class WebMvcConfig {

	}

	@TestConfiguration
	@ImportAutoConfiguration(WebFluxAutoConfiguration.class)
	static class WebFluxConfig {

	}

}
