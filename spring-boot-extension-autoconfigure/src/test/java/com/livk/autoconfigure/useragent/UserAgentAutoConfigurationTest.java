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

package com.livk.autoconfigure.useragent;

import com.blueconic.browscap.UserAgentParser;
import com.livk.commons.util.ReflectionUtils;
import com.livk.context.useragent.UserAgentHelper;
import com.livk.context.useragent.browscap.BrowscapUserAgentConverter;
import com.livk.context.useragent.reactive.ReactiveUserAgentFilter;
import com.livk.context.useragent.reactive.ReactiveUserAgentResolver;
import com.livk.context.useragent.servlet.UserAgentFilter;
import com.livk.context.useragent.servlet.UserAgentResolver;
import com.livk.context.useragent.yauaa.YauaaUserAgentConverter;
import nl.basjes.parse.useragent.UserAgentAnalyzer;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.WebFluxAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.boot.test.context.runner.ReactiveWebApplicationContextRunner;
import org.springframework.boot.test.context.runner.WebApplicationContextRunner;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.core.ResolvableType;
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
class UserAgentAutoConfigurationTest {

	final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
		.withConfiguration(AutoConfigurations.of(UserAgentAutoConfiguration.class));

	@Test
	void test() {
		this.contextRunner.run((context) -> {
			assertThat(context).hasSingleBean(UserAgentHelper.class);
		});
	}

	@Test
	void testBrowscap() {
		this.contextRunner.run((context) -> {
			assertThat(context).hasSingleBean(UserAgentParser.class);
			assertThat(context).hasSingleBean(BrowscapUserAgentConverter.class);
		});
	}

	@Test
	void testYauaa() {
		this.contextRunner.run((context) -> {
			assertThat(context).hasSingleBean(UserAgentAnalyzer.class);
			assertThat(context).hasSingleBean(YauaaUserAgentConverter.class);
		});
	}

	@Test
	void testServlet() {
		WebApplicationContextRunner runner = new WebApplicationContextRunner()
			.withConfiguration(AutoConfigurations.of(UserAgentAutoConfiguration.class));

		runner.withUserConfiguration(WebMvcConfig.class).run(context -> {
			RequestMappingHandlerAdapter adapter = context.getBean(RequestMappingHandlerAdapter.class);
			assertThat(adapter.getArgumentResolvers()).isNotNull().hasAtLeastOneElementOfType(UserAgentResolver.class);

			ResolvableType resolvableType = ResolvableType.forClassWithGenerics(FilterRegistrationBean.class,
					UserAgentFilter.class);
			FilterRegistrationBean<UserAgentFilter> filterRegistrationBean = context
				.<FilterRegistrationBean<UserAgentFilter>>getBeanProvider(resolvableType)
				.getIfAvailable();
			assertNotNull(filterRegistrationBean);
			assertNotNull(filterRegistrationBean.getFilter());
		});
	}

	@Test
	@SuppressWarnings("unchecked")
	void testReactive() {
		ReactiveWebApplicationContextRunner runner = new ReactiveWebApplicationContextRunner()
			.withConfiguration(AutoConfigurations.of(UserAgentAutoConfiguration.class));

		runner.withUserConfiguration(WebFluxConfig.class).run(context -> {
			org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerAdapter adapter = context
				.getBean(org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerAdapter.class);

			Field customResolversField = ReflectionUtils.findField(ArgumentResolverConfigurer.class, "customResolvers");
			assertNotNull(customResolversField);
			List<HandlerMethodArgumentResolver> customResolvers = (List<HandlerMethodArgumentResolver>) ReflectionUtils
				.getDeclaredFieldValue(customResolversField, adapter.getArgumentResolverConfigurer());
			assertThat(customResolvers).isNotNull().hasAtLeastOneElementOfType(ReactiveUserAgentResolver.class);

			assertThat(context).hasSingleBean(ReactiveUserAgentFilter.class);
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
