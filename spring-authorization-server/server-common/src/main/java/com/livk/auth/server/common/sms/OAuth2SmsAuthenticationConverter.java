package com.livk.auth.server.common.sms;


import com.livk.auth.server.common.base.AbstractAuthenticationConverter;
import com.livk.auth.server.common.constant.OAuth2Constants;
import com.livk.auth.server.common.util.OAuth2EndpointUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
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
            OAuth2EndpointUtils.throwError(OAuth2ErrorCodes.INVALID_REQUEST, OAuth2Constants.SMS_PARAMETER_NAME,
                    OAuth2EndpointUtils.ACCESS_TOKEN_REQUEST_ERROR_URI);
        }
    }

    @Override
    public OAuth2SmsAuthenticationToken buildToken(Authentication authentication, Set<String> scopes, Map<String, Object> additionalParameters) {
        return new OAuth2SmsAuthenticationToken(new AuthorizationGrantType(OAuth2Constants.SMS),
                authentication, scopes, additionalParameters);
    }
}
