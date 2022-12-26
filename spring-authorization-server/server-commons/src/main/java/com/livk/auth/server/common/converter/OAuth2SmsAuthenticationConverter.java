package com.livk.auth.server.common.converter;

import com.livk.auth.server.common.constant.SecurityConstants;
import com.livk.auth.server.common.token.OAuth2SmsAuthenticationToken;
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
 * <p>短信登录转换器</p>
 *
 * @author kevin
 */
public class OAuth2SmsAuthenticationConverter implements OAuth2BaseAuthenticationConverter<OAuth2SmsAuthenticationToken> {

    /**
     * 是否支持此convert
     *
     * @param grantType 授权类型
     * @return
     */
    @Override
    public boolean support(String grantType) {
        return SecurityConstants.SMS.equals(grantType);
    }

    @Override
    public OAuth2SmsAuthenticationToken buildToken(Authentication clientPrincipal, Set<String> requestedScopes, Map<String, Object> additionalParameters) {
        return new OAuth2SmsAuthenticationToken(new AuthorizationGrantType(SecurityConstants.SMS), clientPrincipal, requestedScopes, additionalParameters);
    }

    /**
     * 校验扩展参数 验证码模式手机号必须不为空
     *
     * @param request 参数列表
     */
    @Override
    public void checkParams(HttpServletRequest request) {
        MultiValueMap<String, String> parameters = WebUtils.params(request);
        // mobile (REQUIRED)
        String mobile = parameters.getFirst(SecurityConstants.SMS_PARAMETER_NAME);
        if (!StringUtils.hasText(mobile) || parameters.get(SecurityConstants.SMS_PARAMETER_NAME).size() != 1) {
            throw new OAuth2AuthenticationException(new OAuth2Error(OAuth2ErrorCodes.INVALID_REQUEST, SecurityConstants.SMS_PARAMETER_NAME, SecurityConstants.ACCESS_TOKEN_REQUEST_ERROR_URI));
        }

        // code (REQUIRED)
        String code = parameters.getFirst(OAuth2ParameterNames.CODE);
        if (!StringUtils.hasText(code) || parameters.get(OAuth2ParameterNames.CODE).size() != 1) {
            throw new OAuth2AuthenticationException(new OAuth2Error(OAuth2ErrorCodes.INVALID_REQUEST, OAuth2ParameterNames.CODE, SecurityConstants.ACCESS_TOKEN_REQUEST_ERROR_URI));
        }
    }

}
