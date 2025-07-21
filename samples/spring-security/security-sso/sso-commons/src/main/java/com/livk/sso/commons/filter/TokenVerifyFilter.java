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

package com.livk.sso.commons.filter;

import com.livk.commons.util.HttpServletUtils;
import com.livk.sso.commons.entity.Payload;
import com.livk.sso.commons.entity.User;
import com.livk.sso.commons.util.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;
import java.util.Map;

/**
 * @author livk
 */
public class TokenVerifyFilter extends BasicAuthenticationFilter {

	public TokenVerifyFilter(AuthenticationManager authenticationManager) {
		super(authenticationManager);
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (authorization != null && authorization.startsWith("Bearer ")) {
			String token = authorization.replaceFirst("Bearer ", "");
			Payload payload = JwtUtils.parse(token);
			User user = payload.getUserInfo();
			if (user != null) {
				UsernamePasswordAuthenticationToken authenticated = UsernamePasswordAuthenticationToken
					.authenticated(user, "", user.getAuthorities());
				SecurityContextHolder.getContext().setAuthentication(authenticated);
				chain.doFilter(request, response);
			}
			else {
				Map<String, Object> map = Map.of("code", HttpServletResponse.SC_FORBIDDEN, "msg", "缺少用户信息");
				HttpServletUtils.outJson(response, map);
			}
		}
		else {
			Map<String, Object> map = Map.of("code", HttpServletResponse.SC_FORBIDDEN, "msg", "请登录！");
			HttpServletUtils.outJson(response, map);
		}
	}

}
