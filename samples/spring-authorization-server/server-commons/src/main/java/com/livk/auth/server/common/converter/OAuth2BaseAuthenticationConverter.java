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

import com.google.common.collect.Sets;
import com.livk.auth.server.common.constant.SecurityConstants;
import com.livk.auth.server.common.token.OAuth2BaseAuthenticationToken;
import com.livk.commons.util.StreamUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * <p>
 * 自定义模式认证转换器
 * </p>
 *
 * @author livk
 */
public interface OAuth2BaseAuthenticationConverter<T extends OAuth2BaseAuthenticationToken>
		extends AuthenticationConverter {

	RequestMatcher support();

	/**
	 * 构建具体类型的token
	 */
	T buildToken(Authentication clientPrincipal, Set<String> requestedScopes, Map<String, Object> additionalParameters);

	@Override
	default Authentication convert(HttpServletRequest request) {
		if (!support().matches(request)) {
			return null;
		}

		String scope = request.getParameter(OAuth2ParameterNames.SCOPE);
		if (StringUtils.hasText(scope) && request.getParameterValues(OAuth2ParameterNames.SCOPE).length != 1) {
			throw new OAuth2AuthenticationException(new OAuth2Error(OAuth2ErrorCodes.INVALID_REQUEST,
					OAuth2ParameterNames.SCOPE, SecurityConstants.ACCESS_TOKEN_REQUEST_ERROR_URI));
		}

		Set<String> requestedScopes = Collections.emptySet();
		if (StringUtils.hasText(scope)) {
			requestedScopes = Sets.newHashSet(StringUtils.delimitedListToStringArray(scope, " "));
		}

		// 获取当前已经认证的客户端信息
		Authentication clientPrincipal = SecurityContextHolder.getContext().getAuthentication();
		Optional.ofNullable(clientPrincipal)
			.orElseThrow(() -> new OAuth2AuthenticationException(new OAuth2Error(OAuth2ErrorCodes.INVALID_REQUEST,
					OAuth2ErrorCodes.INVALID_CLIENT, SecurityConstants.ACCESS_TOKEN_REQUEST_ERROR_URI)));

		// 扩展信息
		Map<String, Object> additionalParameters = StreamUtils.convert(request.getParameterNames())
			.filter(Predicate.isEqual(OAuth2ParameterNames.GRANT_TYPE)
				.negate()
				.and(Predicate.isEqual(OAuth2ParameterNames.SCOPE).negate()))
			.collect(Collectors.toMap(Function.identity(), request::getParameter));

		// 创建token
		return buildToken(clientPrincipal, requestedScopes, additionalParameters);

	}

}
