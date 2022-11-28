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
import org.springframework.security.oauth2.core.OAuth2Token;
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
                                                                      OAuth2TokenGenerator<OAuth2Token> oAuth2TokenGenerator,
                                                                      UserDetailsAuthenticationProvider userDetailsAuthenticationProvider) throws Exception {
        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer = new OAuth2AuthorizationServerConfigurer();
        http.apply(authorizationServerConfigurer.tokenEndpoint((tokenEndpoint) -> {// 个性化认证授权端点
                    tokenEndpoint.accessTokenRequestConverter(accessTokenRequestConverter()) // 注入自定义的授权认证Converter
                            .accessTokenResponseHandler(new AuthenticationSuccessEventHandler()) // 登录成功处理器
                            .errorResponseHandler(new AuthenticationFailureEventHandler());// 登录失败处理器
                }).clientAuthentication(oAuth2ClientAuthenticationConfigurer -> // 个性化客户端认证
                        oAuth2ClientAuthenticationConfigurer.errorResponseHandler(new AuthenticationFailureEventHandler()))// 处理客户端认证异常
                .authorizationEndpoint(authorizationEndpoint -> authorizationEndpoint// 授权码端点个性化confirm页面
                        .consentPage(SecurityConstants.CUSTOM_CONSENT_PAGE_URI)));

        DefaultSecurityFilterChain chain = http.securityMatcher(authorizationServerConfigurer.getEndpointsMatcher())
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests.anyRequest().authenticated())
                .apply(authorizationServerConfigurer.authorizationService(authorizationService)
                        .authorizationServerSettings(AuthorizationServerSettings.builder().build()))
                .and()
                .apply(new FormIdentityLoginConfigurer())
                .and()
                .build();

        AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);

        OAuth2PasswordAuthenticationProvider resourceOwnerPasswordAuthenticationProvider = new OAuth2PasswordAuthenticationProvider(
                authenticationManager, authorizationService, oAuth2TokenGenerator);

        OAuth2SmsAuthenticationProvider resourceOwnerSmsAuthenticationProvider = new OAuth2SmsAuthenticationProvider(
                authenticationManager, authorizationService, oAuth2TokenGenerator);
        // 处理 UsernamePasswordAuthenticationToken
        http.authenticationProvider(userDetailsAuthenticationProvider)
                // 处理 OAuth2ResourceOwnerPasswordAuthenticationToken s
                .authenticationProvider(resourceOwnerPasswordAuthenticationProvider)
                // 处理 OAuth2ResourceOwnerSmsAuthenticationToken
                .authenticationProvider(resourceOwnerSmsAuthenticationProvider);
        return chain;
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
}
