package com.livk.auth.server.common.token;

import lombok.Getter;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * <p>自定义授权模式抽象</p>
 *
 * @author livk
 */
@Getter
public abstract class OAuth2BaseAuthenticationToken extends AbstractAuthenticationToken {

    private final AuthorizationGrantType authorizationGrantType;

    private final Authentication clientPrincipal;

    private final Set<String> scopes;

    private final Map<String, Object> additionalParameters;

    public OAuth2BaseAuthenticationToken(AuthorizationGrantType authorizationGrantType,
                                         Authentication clientPrincipal,
                                         @Nullable Set<String> scopes,
                                         @Nullable Map<String, Object> additionalParameters) {
        super(Collections.emptyList());
        Assert.notNull(authorizationGrantType, "authorizationGrantType cannot be null");
        Assert.notNull(clientPrincipal, "clientPrincipal cannot be null");
        this.authorizationGrantType = authorizationGrantType;
        this.clientPrincipal = clientPrincipal;
        this.scopes = scopes == null ? Set.of() : Collections.unmodifiableSet(scopes);
        this.additionalParameters = additionalParameters == null ? Map.of() : Collections.unmodifiableMap(additionalParameters);
    }

    /**
     * 扩展模式一般不需要密码
     */
    @Override
    public Object getCredentials() {
        return "";
    }

    /**
     * 获取用户名
     */
    @Override
    public Object getPrincipal() {
        return this.clientPrincipal;
    }

}
