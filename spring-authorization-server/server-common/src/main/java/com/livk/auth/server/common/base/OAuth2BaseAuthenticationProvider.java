package com.livk.auth.server.common.base;


import com.livk.auth.server.common.constant.OAuth2Constants;
import com.livk.auth.server.common.constant.OAuth2ErrorCodesExpand;
import com.livk.auth.server.common.exception.BadCaptchaException;
import com.livk.auth.server.common.util.MessageSourceUtils;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.context.ProviderContextHolder;
import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.security.Principal;
import java.time.Instant;
import java.util.*;


/**
 * <p>
 * OAuth2BaseAuthenticationProvider
 * </p>
 *
 * @author livk
 * @date 2022/7/18
 */
@Slf4j
public abstract class OAuth2BaseAuthenticationProvider<T extends OAuth2BaseAuthenticationToken>
        implements AuthenticationProvider {

    private static final String ERROR_URI = "https://datatracker.ietf.org/doc/html/rfc6749#section-4.1.2.1";

    private final OAuth2AuthorizationService authorizationService;

    private final OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator;

    private final AuthenticationManager authenticationManager;

    private final MessageSourceAccessor messages;

    @Setter
    private String refreshTokenGenerator;

    public OAuth2BaseAuthenticationProvider(AuthenticationManager authenticationManager,
                                            OAuth2AuthorizationService authorizationService,
                                            OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator) {
        Assert.notNull(authorizationService, "authorizationService cannot be null");
        Assert.notNull(tokenGenerator, "tokenGenerator cannot be null");
        this.authenticationManager = authenticationManager;
        this.authorizationService = authorizationService;
        this.tokenGenerator = tokenGenerator;
        this.messages = new MessageSourceAccessor(MessageSourceUtils.get(), Locale.CHINA);
    }

    public abstract UsernamePasswordAuthenticationToken buildToken(Map<String, Object> reqParameters);

    public abstract void checkClient(RegisteredClient registeredClient);

    @SuppressWarnings("unchecked")
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        T baseAuthentication = (T) authentication;
        OAuth2ClientAuthenticationToken clientPrincipal = getAuthenticatedClientElseThrowInvalidClient(baseAuthentication);
        RegisteredClient registeredClient = clientPrincipal.getRegisteredClient();
        Assert.notNull(registeredClient, "registeredClient not be null");
        checkClient(registeredClient);
        Set<String> authorizedScopes = registeredClient.getScopes();
        // Default to configured scopes
        if (!CollectionUtils.isEmpty(baseAuthentication.getScopes())) {
            for (String requestedScope : baseAuthentication.getScopes()) {
                if (!registeredClient.getScopes().contains(requestedScope)) {
                    throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_SCOPE);
                }
            }
            authorizedScopes = new LinkedHashSet<>(baseAuthentication.getScopes());
        }

        Map<String, Object> parameters = baseAuthentication.getAdditionalParameters();

        try {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = buildToken(parameters);
            Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

            DefaultOAuth2TokenContext.Builder tokenContextBuilder = DefaultOAuth2TokenContext.builder()
                    .registeredClient(registeredClient)
                    .principal(authenticate)
                    .providerContext(ProviderContextHolder.getProviderContext())
                    .authorizedScopes(authorizedScopes)
                    .authorizationGrantType(OAuth2Constants.GRANT_TYPE_PASSWORD)
                    .authorizationGrant(baseAuthentication);

            OAuth2TokenContext tokenContext = tokenContextBuilder.tokenType(OAuth2TokenType.ACCESS_TOKEN).build();

            OAuth2Token generatedAccessToken = Optional.ofNullable(this.tokenGenerator.generate(tokenContext)).orElseThrow(() ->
                    new OAuth2AuthenticationException(new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR, "The token generator failed to generate the access token.", ERROR_URI)));

            OAuth2AccessToken accessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER,
                    generatedAccessToken.getTokenValue(), generatedAccessToken.getIssuedAt(),
                    generatedAccessToken.getExpiresAt(), tokenContext.getAuthorizedScopes());


            OAuth2Authorization.Builder authorizationBuilder = OAuth2Authorization.withRegisteredClient(registeredClient)
                    .principalName(authenticate.getName())
                    .authorizationGrantType(baseAuthentication.getAuthorizationGrantType())
                    .attribute(Principal.class.getName(), authenticate)
                    .attribute(OAuth2Authorization.AUTHORIZED_SCOPE_ATTRIBUTE_NAME, authorizedScopes);

            if (generatedAccessToken instanceof ClaimAccessor) {
                authorizationBuilder.token(accessToken, metadata -> {
                    metadata.put(OAuth2Authorization.Token.CLAIMS_METADATA_NAME, ((ClaimAccessor) generatedAccessToken).getClaims());
                    metadata.put(OAuth2Authorization.Token.INVALIDATED_METADATA_NAME, false);
                });
            } else {
                authorizationBuilder.accessToken(accessToken);
            }

            OAuth2RefreshToken refreshToken = null;
            if (registeredClient.getAuthorizationGrantTypes().contains(AuthorizationGrantType.REFRESH_TOKEN) &&
                !clientPrincipal.getClientAuthenticationMethod().equals(ClientAuthenticationMethod.NONE)) {
                if (this.refreshTokenGenerator != null) {
                    Instant now = Instant.now();
                    Instant expires = now.plus(registeredClient.getTokenSettings().getRefreshTokenTimeToLive());
                    refreshToken = new OAuth2RefreshToken(this.refreshTokenGenerator, now, expires);
                } else {
                    tokenContext = tokenContextBuilder.tokenType(OAuth2TokenType.REFRESH_TOKEN).build();
                    OAuth2Token generateRefreshToken = this.tokenGenerator.generate(tokenContext);
                    if (!(generateRefreshToken instanceof OAuth2RefreshToken)) {
                        throw new OAuth2AuthenticationException(new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR,
                                "The token generator failed to generate the refresh token.", ERROR_URI));
                    }
                    refreshToken = (OAuth2RefreshToken) generateRefreshToken;
                }
                authorizationBuilder.refreshToken(refreshToken);
            }

            OAuth2Authorization auth2Authorization = authorizationBuilder.build();
            this.authorizationService.save(auth2Authorization);
            return new OAuth2AccessTokenAuthenticationToken(registeredClient, clientPrincipal, accessToken,
                    refreshToken, Objects.requireNonNull(auth2Authorization.getAccessToken().getClaims()));

        } catch (Exception e) {
            log.error("problem in authenticate", e);
            throw oAuth2AuthenticationException(authentication, (AuthenticationException) e);
        }
    }

    private OAuth2ClientAuthenticationToken getAuthenticatedClientElseThrowInvalidClient(
            Authentication authentication) {

        OAuth2ClientAuthenticationToken clientPrincipal = null;

        if (OAuth2ClientAuthenticationToken.class.isAssignableFrom(authentication.getPrincipal().getClass())) {
            clientPrincipal = (OAuth2ClientAuthenticationToken) authentication.getPrincipal();
        }

        if (clientPrincipal != null && clientPrincipal.isAuthenticated()) {
            return clientPrincipal;
        }

        throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_CLIENT);
    }

    private OAuth2AuthenticationException oAuth2AuthenticationException(Authentication authentication,
                                                                        AuthenticationException authenticationException) {
        if (authenticationException instanceof UsernameNotFoundException) {
            return new OAuth2AuthenticationException(new OAuth2Error(OAuth2ErrorCodesExpand.USERNAME_NOT_FOUND,
                    this.messages.getMessage("JdbcDaoImpl.notFound", new Object[]{authentication.getName()}, "Username {0} not found"), ""));
        }
        if (authenticationException instanceof BadCaptchaException) {
            return new OAuth2AuthenticationException(
                    new OAuth2Error(OAuth2ErrorCodesExpand.BAD_CAPTCHA, this.messages.getMessage(
                            "AbstractUserDetailsAuthenticationProvider.badCaptcha", "Bad captcha"), ""));
        }
        if (authenticationException instanceof BadCredentialsException) {
            return new OAuth2AuthenticationException(
                    new OAuth2Error(OAuth2ErrorCodesExpand.BAD_CREDENTIALS, this.messages.getMessage(
                            "AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"), ""));
        }
        if (authenticationException instanceof LockedException) {
            return new OAuth2AuthenticationException(new OAuth2Error(OAuth2ErrorCodesExpand.USER_LOCKED, this.messages
                    .getMessage("AbstractUserDetailsAuthenticationProvider.locked", "User account is locked"), ""));
        }
        if (authenticationException instanceof DisabledException) {
            return new OAuth2AuthenticationException(new OAuth2Error(OAuth2ErrorCodesExpand.USER_DISABLE,
                    this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.disabled", "User is disabled"),
                    ""));
        }
        if (authenticationException instanceof AccountExpiredException) {
            return new OAuth2AuthenticationException(new OAuth2Error(OAuth2ErrorCodesExpand.USER_EXPIRED, this.messages
                    .getMessage("AbstractUserDetailsAuthenticationProvider.expired", "User account has expired"), ""));
        }
        if (authenticationException instanceof CredentialsExpiredException) {
            return new OAuth2AuthenticationException(new OAuth2Error(OAuth2ErrorCodesExpand.CREDENTIALS_EXPIRED,
                    this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.credentialsExpired",
                            "User credentials have expired"),
                    ""));
        }
        return new OAuth2AuthenticationException(OAuth2ErrorCodesExpand.UN_KNOW_LOGIN_ERROR);
    }
}
