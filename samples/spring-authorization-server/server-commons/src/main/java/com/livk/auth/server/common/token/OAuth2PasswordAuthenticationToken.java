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

package com.livk.auth.server.common.token;

import com.livk.auth.server.common.constant.SecurityConstants;
import org.springframework.security.core.Authentication;

import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 密码授权token信息
 * </p>
 *
 * @author livk
 */
public class OAuth2PasswordAuthenticationToken extends OAuth2BaseAuthenticationToken {

	public OAuth2PasswordAuthenticationToken(Authentication clientPrincipal, Set<String> scopes,
			Map<String, Object> additionalParameters) {
		super(SecurityConstants.GRANT_TYPE_PASSWORD, clientPrincipal, scopes, additionalParameters);
	}

}
