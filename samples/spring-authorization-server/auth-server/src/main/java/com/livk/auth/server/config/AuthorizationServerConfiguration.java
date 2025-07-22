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

package com.livk.auth.server.config;

import com.livk.auth.server.common.constant.SecurityConstants;
import com.livk.auth.server.common.converter.OAuth2PasswordAuthenticationConverter;
import com.livk.auth.server.common.converter.OAuth2SmsAuthenticationConverter;
import com.livk.auth.server.common.core.FormIdentityLoginConfigurer;
import com.livk.auth.server.common.core.UserDetailsAuthenticationProvider;
import com.livk.auth.server.common.core.customizer.OAuth2JwtTokenCustomizer;
import com.livk.auth.server.common.handler.AuthenticationFailureEventHandler;
import com.livk.auth.server.common.handler.AuthenticationSuccessEventHandler;
import com.livk.auth.server.common.provider.OAuth2PasswordAuthenticationProvider;
import com.livk.auth.server.common.provider.OAuth2SmsAuthenticationProvider;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.token.DelegatingOAuth2TokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.JwtGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2RefreshTokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2AuthorizationCodeAuthenticationConverter;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2ClientCredentialsAuthenticationConverter;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2RefreshTokenAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.DelegatingAuthenticationConverter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 * @author livk
 */
@Configuration
public class AuthorizationServerConfiguration {

	@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE)
	public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http,
			OAuth2AuthorizationService authorizationService, OAuth2TokenGenerator<OAuth2Token> oAuth2TokenGenerator,
			UserDetailsAuthenticationProvider userDetailsAuthenticationProvider) throws Exception {
		OAuth2AuthorizationServerConfigurer authorizationServerConfigurer = new OAuth2AuthorizationServerConfigurer();

		AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManagerBuilder.class).build();

		OAuth2PasswordAuthenticationProvider passwordAuthenticationProvider = new OAuth2PasswordAuthenticationProvider(
				authenticationManager, authorizationService, oAuth2TokenGenerator);

		OAuth2SmsAuthenticationProvider smsAuthenticationProvider = new OAuth2SmsAuthenticationProvider(
				authenticationManager, authorizationService, oAuth2TokenGenerator);

		http.with(authorizationServerConfigurer, configurer -> configurer
			.tokenEndpoint((tokenEndpoint) -> tokenEndpoint.accessTokenRequestConverter(accessTokenRequestConverter())
				.authenticationProvider(passwordAuthenticationProvider)
				.authenticationProvider(smsAuthenticationProvider)
				.accessTokenResponseHandler(new AuthenticationSuccessEventHandler())
				.errorResponseHandler(new AuthenticationFailureEventHandler()))
			.authorizationEndpoint(authorizationEndpoint -> authorizationEndpoint
				.consentPage(SecurityConstants.CUSTOM_CONSENT_PAGE_URI))
			.authorizationService(authorizationService))
			.with(new FormIdentityLoginConfigurer(), Customizer.withDefaults());

		RequestMatcher endpointsMatcher = authorizationServerConfigurer.getEndpointsMatcher();
		HttpSessionSecurityContextRepository httpSessionSecurityContextRepository = new HttpSessionSecurityContextRepository();

		return http.securityMatcher(endpointsMatcher)
			.authenticationManager(authenticationManager)
			.securityContext(contextConfigurer -> contextConfigurer
				.securityContextRepository(httpSessionSecurityContextRepository))
			.authorizeHttpRequests(registry -> registry.requestMatchers("/auth/**", "/actuator/**", "/css/**", "/error")
				.permitAll()
				.anyRequest()
				.authenticated())
			.csrf(csrfConfigurer -> csrfConfigurer.ignoringRequestMatchers(endpointsMatcher))
			.authenticationProvider(userDetailsAuthenticationProvider)
			.build();
	}

	@Bean
	public DaoAuthenticationProvider daoAuthenticationProvider(PasswordEncoder passwordEncoder,
			UserDetailsService userDetailsService) {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
		provider.setPasswordEncoder(passwordEncoder);
		return provider;
	}

	@Bean
	public OAuth2TokenGenerator<OAuth2Token> oAuth2TokenGenerator(JWKSource<SecurityContext> jwkSource) {
		JwtEncoder jwtEncoder = new NimbusJwtEncoder(jwkSource);
		JwtGenerator generator = new JwtGenerator(jwtEncoder);
		generator.setJwtCustomizer(new OAuth2JwtTokenCustomizer());
		return new DelegatingOAuth2TokenGenerator(generator, new OAuth2RefreshTokenGenerator());
	}

	private AuthenticationConverter accessTokenRequestConverter() {
		return new DelegatingAuthenticationConverter(new OAuth2AuthorizationCodeAuthenticationConverter(),
				new OAuth2ClientCredentialsAuthenticationConverter(), new OAuth2RefreshTokenAuthenticationConverter(),
				new OAuth2PasswordAuthenticationConverter(), new OAuth2SmsAuthenticationConverter());
	}

}
