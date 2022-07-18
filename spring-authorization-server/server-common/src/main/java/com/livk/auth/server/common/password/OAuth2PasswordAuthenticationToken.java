package com.livk.auth.server.common.password;


import com.livk.auth.server.common.base.AuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

import java.util.Map;
import java.util.Set;

/**
 * <p>
 * OAuth2PasswordAuthenticationToken
 * </p>
 *
 * @author livk
 * @date 2022/7/15
 */
public class OAuth2PasswordAuthenticationToken extends AuthenticationToken {
    public OAuth2PasswordAuthenticationToken(AuthorizationGrantType authorizationGrantType, Authentication authentication,
                                             Set<String> scopes, Map<String, Object> additionalParameters) {
        super(authorizationGrantType, authentication, scopes, additionalParameters);
    }
}
