package com.livk.auth.server.common.token;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

import java.util.Map;
import java.util.Set;

/**
 * @author livk
 */
public class OAuth2SmsAuthenticationToken extends OAuth2BaseAuthenticationToken {

    public OAuth2SmsAuthenticationToken(AuthorizationGrantType authorizationGrantType,
                                        Authentication clientPrincipal,
                                        Set<String> scopes,
                                        Map<String, Object> additionalParameters) {
        super(authorizationGrantType, clientPrincipal, scopes, additionalParameters);
    }

}
