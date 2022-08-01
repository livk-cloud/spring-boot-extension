package com.livk.auth.server.common.base.sms;


import com.livk.auth.server.common.base.AbstractAuthenticationConverter;
import com.livk.auth.server.common.constant.OAuth2Constants;
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
 * <p>
 * OAuth2SmsAuthenticationConverter
 * </p>
 *
 * @author livk
 * @date 2022/7/15
 */
public class OAuth2SmsAuthenticationConverter implements AbstractAuthenticationConverter<OAuth2SmsAuthenticationToken> {
    @Override
    public boolean support(String grantType) {
        return OAuth2Constants.SMS.equals(grantType);
    }

    @Override
    public void checkParams(MultiValueMap<String, String> parameters) {
        String phone = parameters.getFirst(OAuth2Constants.SMS_PARAMETER_NAME);
        if (!StringUtils.hasText(phone) || parameters.get(OAuth2Constants.SMS_PARAMETER_NAME).size() != 1) {
            throw new OAuth2AuthenticationException(new OAuth2Error(OAuth2ErrorCodes.INVALID_REQUEST, OAuth2Constants.SMS_PARAMETER_NAME, ACCESS_TOKEN_REQUEST_ERROR_URI));
        }

        // code (REQUIRED)
        String code = parameters.getFirst(OAuth2ParameterNames.CODE);
        if (!StringUtils.hasText(code) || parameters.get(OAuth2ParameterNames.CODE).size() != 1) {
            throw new OAuth2AuthenticationException(new OAuth2Error(OAuth2ErrorCodes.INVALID_REQUEST, OAuth2ParameterNames.CODE, ACCESS_TOKEN_REQUEST_ERROR_URI));
        }
    }

    @Override
    public OAuth2SmsAuthenticationToken buildToken(Authentication authentication, Set<String> scopes, Map<String, Object> additionalParameters) {
        return new OAuth2SmsAuthenticationToken(OAuth2Constants.GRANT_TYPE_SMS,
                authentication, scopes, additionalParameters);
    }
}
