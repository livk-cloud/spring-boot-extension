package com.livk.auth.server.common.base;


import com.livk.auth.server.common.constant.OAuth2Constants;
import com.livk.auth.server.common.exception.ScopeException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.context.ProviderContextHolder;
import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;


/**
 * <p>
 * OAuth2BaseAuthenticationProvider
 * </p>
 *
 * @author livk
 * @date 2022/7/18
 */
public abstract class OAuth2BaseAuthenticationProvider<T extends AuthenticationToken>
        implements AuthenticationProvider {

    private static final String ERROR_URI = "https://datatracker.ietf.org/doc/html/rfc6749#section-4.1.2.1";

    private final OAuth2AuthorizationService authorizationService;

    private final OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator;

    private final AuthenticationManager authenticationManager;

    public OAuth2BaseAuthenticationProvider(AuthenticationManager authenticationManager,
                                            OAuth2AuthorizationService authorizationService,
                                            OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator) {
        Assert.notNull(authorizationService, "authorizationService cannot be null");
        Assert.notNull(tokenGenerator, "tokenGenerator cannot be null");
        this.authenticationManager = authenticationManager;
        this.authorizationService = authorizationService;
        this.tokenGenerator = tokenGenerator;
    }

    public abstract UsernamePasswordAuthenticationToken buildToken(Map<String, Object> reqParameters);

    public abstract void checkClient(RegisteredClient registeredClient);

    @SuppressWarnings("unchecked")
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        T t = (T) authentication;
        OAuth2ClientAuthenticationToken clientPrincipal = getAuthenticatedClientElseThrowInvalidClient(t);
        RegisteredClient registeredClient = clientPrincipal.getRegisteredClient();
        Assert.notNull(registeredClient, "registeredClient not be null");
        checkClient(registeredClient);
        Set<String> authorizedScopes;
        // Default to configured scopes
        if (!CollectionUtils.isEmpty(t.getScopes())) {
            for (String requestedScope : t.getScopes()) {
                if (!registeredClient.getScopes().contains(requestedScope)) {
                    throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_SCOPE);
                }
            }
            authorizedScopes = new LinkedHashSet<>(t.getScopes());
        } else {
            throw new ScopeException(OAuth2Constants.SCOPE_IS_EMPTY);
        }
        Map<String, Object> parameters = t.getAdditionalParameters();

        try {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = buildToken(parameters);
            Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

            DefaultOAuth2TokenContext.builder()
                    .registeredClient(registeredClient)
                    .principal(authenticate)
                    .providerContext(ProviderContextHolder.getProviderContext())
                    .authorizedScopes(authorizedScopes)
                    .authorizationGrantType(AuthorizationGrantType.PASSWORD)
                    .authorizationGrant(t);

            OAuth2Authorization.Builder builder = OAuth2Authorization.withRegisteredClient(registeredClient)
                    .principalName(authenticate.getName())
                    .authorizationGrantType(AuthorizationGrantType.PASSWORD)
                    .attribute(OAuth2Authorization.AUTHORIZED_SCOPE_ATTRIBUTE_NAME, authorizedScopes);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
}
