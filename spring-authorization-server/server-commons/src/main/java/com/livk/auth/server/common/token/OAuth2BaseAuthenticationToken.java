package com.livk.auth.server.common.token;

import lombok.Getter;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationGrantAuthenticationToken;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * <p>自定义授权模式抽象</p>
 *
 * @author livk
 */
@Getter
public abstract class OAuth2BaseAuthenticationToken extends OAuth2AuthorizationGrantAuthenticationToken {
    private final Set<String> scopes;

    public OAuth2BaseAuthenticationToken(AuthorizationGrantType authorizationGrantType,
                                         Authentication clientPrincipal,
                                         @Nullable Set<String> scopes,
                                         @Nullable Map<String, Object> additionalParameters) {
        super(authorizationGrantType, clientPrincipal, additionalParameters);
        this.scopes = scopes == null ? Set.of() : Collections.unmodifiableSet(scopes);
    }
}
