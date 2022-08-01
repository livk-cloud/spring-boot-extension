package com.livk.auth.server.common.base;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.util.Assert;

import java.util.*;

/**
 * <p>
 * AuthenticationToken
 * </p>
 *
 * @author livk
 * @date 2022/7/15
 */
@Getter
public abstract class OAuth2BaseAuthenticationToken extends AbstractAuthenticationToken {

    private final AuthorizationGrantType authorizationGrantType;

    private final Authentication authentication;

    private final Set<String> scopes;

    private final Map<String, Object> additionalParameters;

    public OAuth2BaseAuthenticationToken(AuthorizationGrantType authorizationGrantType, Authentication authentication,
                                         Set<String> scopes, Map<String, Object> additionalParameters) {
        super(Collections.emptyList());
        Assert.notNull(authorizationGrantType, "authorizationGrantType cannot be null");
        Assert.notNull(authentication, "authentication cannot be null");
        this.authorizationGrantType = authorizationGrantType;
        this.authentication = authentication;
        this.scopes = Collections.unmodifiableSet(scopes != null ? new HashSet<>(scopes) : Collections.emptySet());
        this.additionalParameters = Collections.unmodifiableMap(
                additionalParameters != null ? new HashMap<>(additionalParameters) : Collections.emptyMap());
    }

    @Override
    public Object getCredentials() {
        return "";
    }

    @Override
    public Object getPrincipal() {
        return authentication;
    }
}
