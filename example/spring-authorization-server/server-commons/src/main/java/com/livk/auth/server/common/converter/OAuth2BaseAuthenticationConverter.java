package com.livk.auth.server.common.converter;


import com.google.common.collect.Sets;
import com.livk.auth.server.common.constant.SecurityConstants;
import com.livk.auth.server.common.token.OAuth2BaseAuthenticationToken;
import com.livk.commons.web.util.WebUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>自定义模式认证转换器</p>
 *
 * @author livk
 */
public interface OAuth2BaseAuthenticationConverter<T extends OAuth2BaseAuthenticationToken> extends AuthenticationConverter {

    /**
     * 是否支持此convert
     *
     * @param grantType 授权类型
     */
    boolean support(String grantType);

    /**
     * 校验参数
     *
     * @param request 请求
     */
    default void checkParams(HttpServletRequest request) {

    }

    /**
     * 构建具体类型的token
     */
    T buildToken(Authentication clientPrincipal, Set<String> requestedScopes, Map<String, Object> additionalParameters);

    @Override
    default Authentication convert(HttpServletRequest request) {

        // grant_type (REQUIRED)
        String grantType = request.getParameter(OAuth2ParameterNames.GRANT_TYPE);
        if (!support(grantType)) {
            return null;
        }

        MultiValueMap<String, String> parameters = WebUtils.params(request);
        // scope (OPTIONAL)
        String scope = parameters.getFirst(OAuth2ParameterNames.SCOPE);
        if (StringUtils.hasText(scope) && parameters.get(OAuth2ParameterNames.SCOPE).size() != 1) {
            throw new OAuth2AuthenticationException(new OAuth2Error(OAuth2ErrorCodes.INVALID_REQUEST, OAuth2ParameterNames.SCOPE, SecurityConstants.ACCESS_TOKEN_REQUEST_ERROR_URI));
        }

        Set<String> requestedScopes = Collections.emptySet();
        if (StringUtils.hasText(scope)) {
            requestedScopes = Sets.newHashSet(StringUtils.delimitedListToStringArray(scope, " "));
        }

        // 校验个性化参数
        checkParams(request);

        // 获取当前已经认证的客户端信息
        Authentication clientPrincipal = SecurityContextHolder.getContext().getAuthentication();
        Optional.ofNullable(clientPrincipal).orElseThrow(() ->
                new OAuth2AuthenticationException(new OAuth2Error(OAuth2ErrorCodes.INVALID_REQUEST, OAuth2ErrorCodes.INVALID_CLIENT, SecurityConstants.ACCESS_TOKEN_REQUEST_ERROR_URI)));

        // 扩展信息
        Map<String, Object> additionalParameters = parameters.entrySet().stream()
                .filter(e -> !e.getKey().equals(OAuth2ParameterNames.GRANT_TYPE) && !e.getKey().equals(OAuth2ParameterNames.SCOPE))
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().get(0)));

        // 创建token
        return buildToken(clientPrincipal, requestedScopes, additionalParameters);

    }

}
