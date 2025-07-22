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

package com.livk.autoconfigure.useragent;

import com.livk.context.useragent.UserAgentConverter;
import com.livk.context.useragent.UserAgentHelper;
import com.livk.context.useragent.reactive.ReactiveUserAgentFilter;
import com.livk.context.useragent.reactive.ReactiveUserAgentResolver;
import com.livk.context.useragent.servlet.UserAgentFilter;
import com.livk.context.useragent.servlet.UserAgentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
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
@ConditionalOnBean(UserAgentConverter.class)
public class UserAgentConfiguration {

	/**
	 * User agent helper user agent helper.
	 * @return the user agent helper
	 */
	@Bean
	public UserAgentHelper userAgentHelper() {
		return new UserAgentHelper();
	}

	/**
	 * The type User agent mvc auto configuration.
	 */
	@AutoConfiguration
	@RequiredArgsConstructor
	@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
	public static class UserAgentMvcAutoConfiguration implements WebMvcConfigurer {

		private final UserAgentHelper userAgentHelper;

		/**
		 * Filter registration bean filter registration bean.
		 * @return the filter registration bean
		 */
		@Bean
		public FilterRegistrationBean<UserAgentFilter> filterRegistrationBean() {
			FilterRegistrationBean<UserAgentFilter> registrationBean = new FilterRegistrationBean<>();
			registrationBean.setFilter(new UserAgentFilter(userAgentHelper));
			registrationBean.addUrlPatterns("/*");
			registrationBean.setName("userAgentFilter");
			registrationBean.setOrder(1);
			return registrationBean;
		}

		@Override
		public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
			resolvers.add(new UserAgentResolver(userAgentHelper));
		}

	}

	/**
	 * The type User agent reactive auto configuration.
	 */
	@AutoConfiguration
	@RequiredArgsConstructor
	@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
	public static class UserAgentReactiveAutoConfiguration implements WebFluxConfigurer {

		private final UserAgentHelper userAgentHelper;

		/**
		 * Reactive user agent filter reactive user agent filter.
		 * @return the reactive user agent filter
		 */
		@Bean
		public ReactiveUserAgentFilter reactiveUserAgentFilter() {
			return new ReactiveUserAgentFilter(userAgentHelper);
		}

		@Override
		public void configureArgumentResolvers(ArgumentResolverConfigurer configurer) {
			configurer.addCustomResolver(new ReactiveUserAgentResolver(userAgentHelper));
		}

	}

}
