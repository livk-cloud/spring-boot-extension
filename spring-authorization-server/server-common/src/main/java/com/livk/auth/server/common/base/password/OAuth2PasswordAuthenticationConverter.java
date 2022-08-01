package com.livk.auth.server.common.base.password;


import com.livk.auth.server.common.base.AbstractAuthenticationConverter;
import com.livk.auth.server.common.constant.OAuth2Constants;
import com.livk.auth.server.common.util.OAuth2EndpointUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Set;

/**
 * <p>
 * OAuth2ResourceOwnerPasswordAuthenticationConverter
 * </p>
 *
 * @author livk
 * @date 2022/7/15
 */
public class OAuth2PasswordAuthenticationConverter implements AbstractAuthenticationConverter<OAuth2PasswordAuthenticationToken> {
    @Override
    public boolean support(String grantType) {
        return OAuth2Constants.GRANT_TYPE_PASSWORD.getValue().equals(grantType);
    }

    @Override
    public void checkParams(MultiValueMap<String, String> parameters) {
        // username (REQUIRED)
        String username = parameters.getFirst(OAuth2ParameterNames.USERNAME);
        if (!StringUtils.hasText(username) || parameters.get(OAuth2ParameterNames.USERNAME).size() != 1) {
            OAuth2EndpointUtils.throwError(OAuth2ErrorCodes.INVALID_REQUEST, OAuth2ParameterNames.USERNAME,
                    OAuth2EndpointUtils.ACCESS_TOKEN_REQUEST_ERROR_URI);
        }

        // password (REQUIRED)
        String password = parameters.getFirst(OAuth2ParameterNames.PASSWORD);
        if (!StringUtils.hasText(password) || parameters.get(OAuth2ParameterNames.PASSWORD).size() != 1) {
            OAuth2EndpointUtils.throwError(OAuth2ErrorCodes.INVALID_REQUEST, OAuth2ParameterNames.PASSWORD,
                    OAuth2EndpointUtils.ACCESS_TOKEN_REQUEST_ERROR_URI);
        }
    }

    @Override
    public OAuth2PasswordAuthenticationToken buildToken(Authentication authentication, Set<String> scopes, Map<String, Object> additionalParameters) {
        return new OAuth2PasswordAuthenticationToken(OAuth2Constants.GRANT_TYPE_PASSWORD, authentication, scopes, additionalParameters);
    }
}
