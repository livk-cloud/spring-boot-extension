/*
 * Copyright 2021 spring-boot-extension the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.livk.autoconfigure.ip2region.filter;

import com.livk.autoconfigure.ip2region.support.Ip2RegionSearch;
import com.livk.autoconfigure.ip2region.support.RequestIpContextHolder;
import com.livk.commons.util.WebUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * @author livk
 */
@RequiredArgsConstructor
public class RequestIpFilter extends OncePerRequestFilter {

	private final Ip2RegionSearch search;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		String ip = WebUtils.realIp(request);
		RequestIpContextHolder.set(search.searchAsInfo(ip));
		filterChain.doFilter(request, response);
		RequestIpContextHolder.remove();
	}
}
