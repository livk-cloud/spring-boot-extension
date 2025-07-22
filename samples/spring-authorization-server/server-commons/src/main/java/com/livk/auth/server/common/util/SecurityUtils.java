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

package com.livk.auth.server.common.util;

import com.livk.auth.server.common.constant.SecurityConstants;
import com.livk.auth.server.common.core.principal.Oauth2User;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author livk
 */
@UtilityClass
public class SecurityUtils {

	/**
	 * 获取Authentication
	 */
	public Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	/**
	 * 获取用户
	 */
	public Oauth2User getUser(Authentication authentication) {
		Object principal = authentication.getPrincipal();
		if (principal instanceof Oauth2User) {
			return (Oauth2User) principal;
		}
		return null;
	}

	/**
	 * 获取用户
	 */
	public Oauth2User getUser() {
		Authentication authentication = getAuthentication();
		if (authentication == null) {
			return null;
		}
		return getUser(authentication);
	}

	/**
	 * 获取用户角色信息
	 * @return 角色集合
	 */
	public List<Long> getRoles() {
		return getAuthentication().getAuthorities()
			.stream()
			.map(GrantedAuthority::getAuthority)
			.filter(authority -> StringUtils.hasText(authority) && authority.startsWith(SecurityConstants.ROLE))
			.map(authority -> Long.parseLong(authority.replaceFirst(SecurityConstants.ROLE, "")))
			.collect(Collectors.toList());
	}

}
