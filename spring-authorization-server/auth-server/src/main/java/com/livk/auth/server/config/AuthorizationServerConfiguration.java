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
import com.livk.auth.server.common.util.JwkUtils;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.DelegatingOAuth2TokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.JwtGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2RefreshTokenGenerator;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.security.oauth2.server.authorization.web.authentication.DelegatingAuthenticationConverter;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2ClientCredentialsAuthenticationConverter;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2RefreshTokenAuthenticationConverter;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.List;

/**
 * <p>
 * AuthorizationServerConfiguration
 * </p>
 *
 * @author livk
 * @date 2022/7/18
 */
@Configuration
public class AuthorizationServerConfiguration {

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http,
                                                                      OAuth2AuthorizationService authorizationService,
                                                                      PasswordEncoder passwordEncoder) throws Exception {
        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer = new OAuth2AuthorizationServerConfigurer();
        OAuth2AuthorizationServerConfigurer configurer = authorizationServerConfigurer.tokenEndpoint(tokenEndpoint ->
                        tokenEndpoint.accessTokenRequestConverter(accessTokenRequestConverter())
                                .accessTokenResponseHandler(new AuthenticationSuccessEventHandler())
                                .errorResponseHandler(new AuthenticationFailureEventHandler()))
                .authorizationEndpoint(authorizationEndpoint ->
                        authorizationEndpoint.consentPage(SecurityConstants.CUSTOM_CONSENT_PAGE_URI));
        http.apply(configurer);
        RequestMatcher endpointsMatcher = authorizationServerConfigurer.getEndpointsMatcher();

        DefaultSecurityFilterChain securityFilterChain = http.requestMatcher(endpointsMatcher)
                .authorizeRequests(authorizeRequests -> authorizeRequests.anyRequest().authenticated())
                .apply(configurer.authorizationService(authorizationService))
                .and()
                .apply(new FormIdentityLoginConfigurer())
                .and()
                .build();

        addCustomOAuth2GrantAuthenticationProvider(http, passwordEncoder);
        return securityFilterChain;
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        RSAKey rsaKey = JwkUtils.generateRsa();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
    }

    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    @Bean
    public JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
        return new NimbusJwtEncoder(jwkSource);
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings(@Value("${server.port}") Integer port) {
        return AuthorizationServerSettings.builder()
                .tokenEndpoint("/oauth2/token")
                .authorizationEndpoint("/oauth2/authorize")
                .tokenIntrospectionEndpoint("/oauth2/introspect")
                .jwkSetEndpoint("/oauth2/jwks")
                .issuer("http://localhost:" + port)
                .build();
    }

    @Bean
    public OAuth2TokenGenerator<OAuth2Token> oAuth2TokenGenerator(JwtEncoder jwtEncoder) {
        JwtGenerator generator = new JwtGenerator(jwtEncoder);
        generator.setJwtCustomizer(new OAuth2JwtTokenCustomizer());
        return new DelegatingOAuth2TokenGenerator(generator, new OAuth2RefreshTokenGenerator());
    }

    private AuthenticationConverter accessTokenRequestConverter() {
        return new DelegatingAuthenticationConverter(List.of(
                new OAuth2ClientCredentialsAuthenticationConverter(),
                new OAuth2RefreshTokenAuthenticationConverter(),
                new OAuth2PasswordAuthenticationConverter(),
                new OAuth2SmsAuthenticationConverter()));
    }

    @SuppressWarnings("unchecked")
    private void addCustomOAuth2GrantAuthenticationProvider(HttpSecurity http, PasswordEncoder passwordEncoder) {
        OAuth2TokenGenerator<Jwt> tokenGenerator = (OAuth2TokenGenerator<Jwt>) http.getSharedObject(OAuth2TokenGenerator.class);

        AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);

        OAuth2AuthorizationService authorizationService = http.getSharedObject(OAuth2AuthorizationService.class);

        OAuth2PasswordAuthenticationProvider resourceOwnerPasswordAuthenticationProvider = new OAuth2PasswordAuthenticationProvider(
                authenticationManager, authorizationService, tokenGenerator);

        OAuth2SmsAuthenticationProvider resourceOwnerSmsAuthenticationProvider = new OAuth2SmsAuthenticationProvider(
                authenticationManager, authorizationService, tokenGenerator);

        // 处理 UsernamePasswordAuthenticationToken
        http.authenticationProvider(new UserDetailsAuthenticationProvider(passwordEncoder));

        // 处理 OAuth2ResourceOwnerPasswordAuthenticationToken s
        http.authenticationProvider(resourceOwnerPasswordAuthenticationProvider);

        // 处理 OAuth2ResourceOwnerSmsAuthenticationToken
        http.authenticationProvider(resourceOwnerSmsAuthenticationProvider);
    }
}
