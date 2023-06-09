/*
 * Copyright 2021 spring-boot-extension the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.livk.autoconfigure.useragent;

import com.livk.autoconfigure.useragent.reactive.ReactiveUserAgentFilter;
import com.livk.autoconfigure.useragent.reactive.ReactiveUserAgentResolver;
import com.livk.autoconfigure.useragent.servlet.UserAgentFilter;
import com.livk.autoconfigure.useragent.servlet.UserAgentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.result.method.annotation.ArgumentResolverConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * The type User agent auto configuration.
 */
@AutoConfiguration
public class UserAgentAutoConfiguration {

	/**
	 * The type User agent mvc auto configuration.
	 */
	@AutoConfiguration
	@RequiredArgsConstructor
	@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
	public static class UserAgentMvcAutoConfiguration implements WebMvcConfigurer {


		/**
		 * Filter registration bean filter registration bean.
		 *
		 * @return the filter registration bean
		 */
		@Bean
		public FilterRegistrationBean<UserAgentFilter> filterRegistrationBean() {
			FilterRegistrationBean<UserAgentFilter> registrationBean = new FilterRegistrationBean<>();
			registrationBean.setFilter(new UserAgentFilter());
			registrationBean.addUrlPatterns("/*");
			registrationBean.setName("userAgentFilter");
			registrationBean.setOrder(1);
			return registrationBean;
		}

		@Override
		public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
			resolvers.add(new UserAgentResolver());
		}
	}

	/**
	 * The type User agent reactive auto configuration.
	 */
	@AutoConfiguration
	@RequiredArgsConstructor
	@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
	public static class UserAgentReactiveAutoConfiguration implements WebFluxConfigurer {


		/**
		 * Reactive user agent filter reactive user agent filter.
		 *
		 * @return the reactive user agent filter
		 */
		@Bean
		public ReactiveUserAgentFilter reactiveUserAgentFilter() {
			return new ReactiveUserAgentFilter();
		}

		@Override
		public void configureArgumentResolvers(ArgumentResolverConfigurer configurer) {
			configurer.addCustomResolver(new ReactiveUserAgentResolver());
		}
	}
}
