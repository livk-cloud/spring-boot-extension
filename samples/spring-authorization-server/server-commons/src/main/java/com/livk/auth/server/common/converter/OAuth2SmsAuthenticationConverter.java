/*
 * Copyright 2021-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.livk.auth.server.common.converter;

import com.livk.auth.server.common.constant.SecurityConstants;
import com.livk.auth.server.common.token.OAuth2SmsAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 短信登录转换器
 * </p>
 *
 * @author kevin
 */
public class OAuth2SmsAuthenticationConverter
		implements OAuth2BaseAuthenticationConverter<OAuth2SmsAuthenticationToken> {

	@Override
	public RequestMatcher support() {
		return request -> SecurityConstants.SMS.equals(request.getParameter(OAuth2ParameterNames.GRANT_TYPE))
				&& StringUtils.hasText(request.getParameter(SecurityConstants.SMS_PARAMETER_NAME))
				&& StringUtils.hasText(request.getParameter(OAuth2ParameterNames.CODE));
	}

	@Override
	public OAuth2SmsAuthenticationToken buildToken(Authentication clientPrincipal, Set<String> requestedScopes,
			Map<String, Object> additionalParameters) {
		return new OAuth2SmsAuthenticationToken(clientPrincipal, requestedScopes, additionalParameters);
	}

}
