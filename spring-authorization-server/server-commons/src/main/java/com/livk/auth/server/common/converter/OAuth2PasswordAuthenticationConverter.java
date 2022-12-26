package com.livk.auth.server.common.converter;


import com.livk.auth.server.common.constant.SecurityConstants;
import com.livk.auth.server.common.token.OAuth2PasswordAuthenticationToken;
import com.livk.commons.util.WebUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Set;

/**
 * <p>密码认证转换器</p>
 *
 * @author livk
 */
public class OAuth2PasswordAuthenticationConverter implements OAuth2BaseAuthenticationConverter<OAuth2PasswordAuthenticationToken> {

    /**
     * 支持密码模式
     *
     * @param grantType 授权类型
     */
    @Override
    public boolean support(String grantType) {
        return SecurityConstants.PASSWORD.equals(grantType);
    }

    @Override
    public OAuth2PasswordAuthenticationToken buildToken(Authentication clientPrincipal, Set<String> requestedScopes, Map<String, Object> additionalParameters) {
        return new OAuth2PasswordAuthenticationToken(new AuthorizationGrantType(SecurityConstants.PASSWORD), clientPrincipal, requestedScopes, additionalParameters);
    }

    /**
     * 校验扩展参数 密码模式密码必须不为空
     *
     * @param request 参数列表
     */
    @Override
    public void checkParams(HttpServletRequest request) {
        MultiValueMap<String, String> parameters = WebUtils.params(request);
        // username (REQUIRED)
        String username = parameters.getFirst(OAuth2ParameterNames.USERNAME);
        if (!StringUtils.hasText(username) || parameters.get(OAuth2ParameterNames.USERNAME).size() != 1) {
            throw new OAuth2AuthenticationException(new OAuth2Error(OAuth2ErrorCodes.INVALID_REQUEST, OAuth2ParameterNames.USERNAME, SecurityConstants.ACCESS_TOKEN_REQUEST_ERROR_URI));
        }

        // password (REQUIRED)
        String password = parameters.getFirst(OAuth2ParameterNames.PASSWORD);
        if (!StringUtils.hasText(password) || parameters.get(OAuth2ParameterNames.PASSWORD).size() != 1) {
            throw new OAuth2AuthenticationException(new OAuth2Error(OAuth2ErrorCodes.INVALID_REQUEST, OAuth2ParameterNames.PASSWORD, SecurityConstants.ACCESS_TOKEN_REQUEST_ERROR_URI));
        }
    }

}
