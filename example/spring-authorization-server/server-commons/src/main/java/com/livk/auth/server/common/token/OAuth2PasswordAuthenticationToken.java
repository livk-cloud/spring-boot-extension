package com.livk.auth.server.common.token;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

import java.util.Map;
import java.util.Set;

/**
 * <p>密码授权token信息</p>
 *
 * @author livk
 */
public class OAuth2PasswordAuthenticationToken extends OAuth2BaseAuthenticationToken {

    public OAuth2PasswordAuthenticationToken(AuthorizationGrantType authorizationGrantType,
                                             Authentication clientPrincipal,
                                             Set<String> scopes,
                                             Map<String, Object> additionalParameters) {
        super(authorizationGrantType, clientPrincipal, scopes, additionalParameters);
    }
}
