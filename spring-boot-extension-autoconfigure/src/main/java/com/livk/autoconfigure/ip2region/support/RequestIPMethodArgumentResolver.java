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

package com.livk.autoconfigure.ip2region.support;

import com.livk.autoconfigure.ip2region.annotation.RequestIp;
import com.livk.autoconfigure.ip2region.doamin.IpInfo;
import com.livk.commons.web.util.WebUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * <p>
 * RequestIPMethodArgumentResolver
 * </p>
 *
 * @author livk
 */
@RequiredArgsConstructor
public class RequestIPMethodArgumentResolver implements HandlerMethodArgumentResolver {

	private final Ip2RegionSearch search;

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(RequestIp.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, @NonNull NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
		if (parameter.getParameterType().isAssignableFrom(IpInfo.class)) {
			return RequestIpContextHolder.computeIfAbsent(() -> parseIp(webRequest));
		}
		throw new RuntimeException("param not support " + parameter.getParameterType());
	}

	private IpInfo parseIp(NativeWebRequest webRequest) {
		HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
		Assert.notNull(request, "request not be null");
		String ip = WebUtils.realIp(request);
		return search.searchAsInfo(ip);
	}
}
