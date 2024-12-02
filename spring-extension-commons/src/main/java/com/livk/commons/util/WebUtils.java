/*
 * Copyright 2021-2024 spring-boot-extension the original author or authors.
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
 */

package com.livk.commons.util;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.livk.commons.jackson.util.JsonMapperUtils;
import com.livk.commons.web.HttpParameters;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedCaseInsensitiveMap;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * web、servlet相关工具类
 * </p>
 *
 * @author livk
 */
@UtilityClass
public class WebUtils extends org.springframework.web.util.WebUtils {

	/**
	 * 获取当前线程的request
	 * @return http servlet request
	 */
	public HttpServletRequest request() {
		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
		if (requestAttributes instanceof ServletRequestAttributes attributes) {
			return attributes.getRequest();
		}
		if (requestAttributes instanceof NativeWebRequest nativeWebRequest) {
			return nativeWebRequest.getNativeRequest(HttpServletRequest.class);
		}
		throw new IllegalStateException("request not found");
	}

	/**
	 * 获取当前线程的response.
	 * @return http servlet response
	 */
	public HttpServletResponse response() {
		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
		if (requestAttributes instanceof ServletRequestAttributes attributes) {
			return attributes.getResponse();
		}
		if (requestAttributes instanceof NativeWebRequest nativeWebRequest) {
			return nativeWebRequest.getNativeResponse(HttpServletResponse.class);
		}
		throw new IllegalStateException("response not found");
	}

	/**
	 * 将request header转成HttpHeaders
	 * @param request request
	 * @return http headers
	 */
	public HttpHeaders headers(HttpServletRequest request) {
		LinkedCaseInsensitiveMap<List<String>> insensitiveMap = new LinkedCaseInsensitiveMap<>();
		Enumeration<String> headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String headerName = headerNames.nextElement();
			Enumeration<String> headers = request.getHeaders(headerName);
			insensitiveMap.put(headerName, Collections.list(headers));
		}
		return new HttpHeaders(CollectionUtils.toMultiValueMap(insensitiveMap));
	}

	/**
	 * 获取当前request所有的attributes
	 * @param request request
	 * @return attributes
	 */
	public Map<String, Object> attributes(HttpServletRequest request) {
		return BaseStreamUtils.convert(request.getAttributeNames())
			.collect(Collectors.toMap(Function.identity(), request::getAttribute));
	}

	/**
	 * 解析request的param转成MultiValueMap
	 * @param request request
	 * @return MultiValueMap
	 */
	public HttpParameters params(HttpServletRequest request) {
		return new HttpParameters(Maps.transformValues(request.getParameterMap(), Lists::newArrayList));
	}

	/**
	 * 解析request获取真实IP
	 * @param request request
	 * @return ip
	 */
	public String realIp(HttpServletRequest request) {
		String[] ipHeaders = { "X-Forwarded-For", "Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_CLIENT_IP",
				"HTTP_X_FORWARDED_FOR", "X-Real-IP" };
		for (String header : ipHeaders) {
			String headerIp = request.getHeader(header);
			if (headerIp != null && !headerIp.isBlank() && !"unknown".equalsIgnoreCase(headerIp)) {
				// 处理多IP的情况（只取第一个IP）
				Splitter.on(",").splitToList(headerIp).getFirst();
			}
		}
		return request.getRemoteAddr();
	}

	/**
	 * 以JSON的格式写出数据到response
	 * @param response response
	 * @param data 需要写出的数据
	 * @see JsonMapperUtils
	 */
	public void outJson(HttpServletResponse response, Object data) {
		out(response, JsonMapperUtils.writeValueAsString(data), MediaType.APPLICATION_JSON_VALUE);
	}

	/**
	 * 根据response写入返回值
	 * @param response response
	 * @param message 写入的信息
	 * @param contentType contentType {@link MediaType}
	 */
	public void out(HttpServletResponse response, String message, String contentType) {
		response.setContentType(contentType);
		response.setCharacterEncoding("UTF-8");
		try (PrintWriter out = response.getWriter()) {
			out.print(message);
			out.flush();
		}
		catch (IOException exception) {
			throw new ErrorResponseException(HttpStatus.INTERNAL_SERVER_ERROR, exception);
		}
	}

}
