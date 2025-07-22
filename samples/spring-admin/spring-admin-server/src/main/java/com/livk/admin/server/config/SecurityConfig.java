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

package com.livk.admin.server.config;

import de.codecentric.boot.admin.server.config.AdminServerProperties;
import jakarta.servlet.DispatcherType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;

import java.util.UUID;

/**
 * @author livk
 */
@Configuration
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http, AdminServerProperties adminServer)
			throws Exception {
		SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
		successHandler.setTargetUrlParameter("redirectTo");
		successHandler.setDefaultTargetUrl(adminServer.path("/"));
		return http
			.authorizeHttpRequests(registry -> registry.requestMatchers(adminServer.path("/assets/**"))
				.permitAll()
				.requestMatchers(adminServer.path("/variables.css"))
				.permitAll()
				.requestMatchers(adminServer.path("/actuator/info"))
				.permitAll()
				.requestMatchers(adminServer.path("/actuator/health"))
				.permitAll()
				.requestMatchers(adminServer.path("/login"))
				.permitAll()
				.dispatcherTypeMatchers(DispatcherType.ASYNC)
				.permitAll()
				.anyRequest()
				.authenticated())
			.formLogin(configurer -> configurer.loginPage(adminServer.path("/login")).successHandler(successHandler))
			.httpBasic(Customizer.withDefaults())
			.csrf(configurer -> configurer.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
				.ignoringRequestMatchers(
						PathPatternRequestMatcher.withDefaults()
							.matcher(HttpMethod.POST, adminServer.path("/instances")),
						PathPatternRequestMatcher.withDefaults()
							.matcher(HttpMethod.DELETE, adminServer.path("/instances/**")),
						PathPatternRequestMatcher.withDefaults().matcher(adminServer.path("/actuator/**"))))
			.rememberMe(configurer -> configurer.key(UUID.randomUUID().toString()).tokenValiditySeconds(1209600))
			.build();
	}

}
