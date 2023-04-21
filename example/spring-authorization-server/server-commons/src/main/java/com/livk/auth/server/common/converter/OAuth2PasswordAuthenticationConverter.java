/*
 * Copyright 2021 spring-boot-extension the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.livk.auth.server.common.converter;


import com.livk.auth.server.common.constant.SecurityConstants;
import com.livk.auth.server.common.token.OAuth2PasswordAuthenticationToken;
import com.livk.commons.web.util.WebUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
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
        return new OAuth2PasswordAuthenticationToken(SecurityConstants.AUTHORIZATION_GRANT_TYPE_PASSWORD, clientPrincipal, requestedScopes, additionalParameters);
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
