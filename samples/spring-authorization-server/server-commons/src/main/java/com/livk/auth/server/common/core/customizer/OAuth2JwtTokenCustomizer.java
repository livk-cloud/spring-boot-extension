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

package com.livk.auth.server.common.core.customizer;

import com.livk.auth.server.common.constant.SecurityConstants;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

/**
 * <p>
 * JWT token 输出增强
 * </p>
 *
 * @author livk
 */
public class OAuth2JwtTokenCustomizer implements OAuth2TokenCustomizer<JwtEncodingContext> {

	/**
	 * Customize the OAuth 2.0 Token attributes.
	 * @param context the context containing the OAuth 2.0 Token attributes
	 */
	@Override
	public void customize(JwtEncodingContext context) {
		JwtClaimsSet.Builder claims = context.getClaims();
		claims.claim(OAuth2ParameterNames.GRANT_TYPE, context.getAuthorizationGrantType().getValue());
		claims.claim(OAuth2ParameterNames.CLIENT_ID, context.getAuthorizationGrant().getName());

		// 客户端模式不返回具体用户信息
		if (AuthorizationGrantType.CLIENT_CREDENTIALS.equals(context.getAuthorizationGrantType())) {
			return;
		}

		// Oauth2User oauth2User = (Oauth2User) context.getPrincipal().getPrincipal();
		claims.claim(SecurityConstants.DETAILS_USER, context.getPrincipal().getPrincipal());
	}

}
