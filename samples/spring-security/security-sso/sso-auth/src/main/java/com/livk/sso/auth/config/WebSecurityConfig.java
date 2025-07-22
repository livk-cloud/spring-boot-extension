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

package com.livk.sso.auth.config;

import com.livk.sso.auth.handler.CustomAccessDeniedHandler;
import com.livk.sso.auth.handler.CustomAuthenticationFailureHandler;
import com.livk.sso.auth.handler.CustomAuthenticationSuccessHandler;
import com.livk.sso.auth.handler.CustomLogoutSuccessHandler;
import com.livk.sso.auth.support.CustomAuthenticationEntryPoint;
import com.livk.sso.commons.filter.TokenVerifyFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

/**
 * @author livk
 */
@Configuration
public class WebSecurityConfig {

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManagerBuilder.class).build();

		TokenLoginFilter tokenLoginFilter = new TokenLoginFilter(authenticationManager);
		tokenLoginFilter.setAuthenticationSuccessHandler(new CustomAuthenticationSuccessHandler());
		tokenLoginFilter.setAuthenticationFailureHandler(new CustomAuthenticationFailureHandler());

		TokenVerifyFilter tokenVerifyFilter = new TokenVerifyFilter(authenticationManager);
		return http.csrf(AbstractHttpConfigurer::disable)
			.authenticationManager(authenticationManager)
			.authorizeHttpRequests(registry -> registry.requestMatchers("/user/query")
				.hasAnyRole("ADMIN")
				.anyRequest()
				.authenticated())
			.addFilterBefore(tokenLoginFilter, UsernamePasswordAuthenticationFilter.class)
			.addFilterAfter(tokenVerifyFilter, BasicAuthenticationFilter.class)
			.exceptionHandling(configurer -> configurer.accessDeniedHandler(new CustomAccessDeniedHandler())
				.authenticationEntryPoint(new CustomAuthenticationEntryPoint()))
			.logout(configurer -> configurer.logoutUrl("/oauth2/logout")
				.logoutSuccessHandler(new CustomLogoutSuccessHandler()))
			.sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.build();
	}

}
