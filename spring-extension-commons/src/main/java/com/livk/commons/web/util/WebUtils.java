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

package com.livk.commons.web.util;

import com.google.common.collect.Lists;
import com.livk.commons.collect.BaseStreamUtils;
import com.livk.commons.jackson.util.JsonMapperUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.util.*;
import org.springframework.web.ErrorResponseException;
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
 * WebUtils
 * </p>
 *
 * @author livk
 */
@UtilityClass
public class WebUtils extends org.springframework.web.util.WebUtils {

	private static final String UNKNOWN = "unknown";

	private static final String HTTP_IP_SPLIT = ",";

	/**
	 * Servlet request attributes servlet request attributes.
	 *
	 * @return the servlet request attributes
	 */
	public ServletRequestAttributes servletRequestAttributes() {
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
		Assert.notNull(servletRequestAttributes, "attributes not null!");
		return servletRequestAttributes;
	}

	/**
	 * Request http servlet request.
	 *
	 * @return the http servlet request
	 */
	public HttpServletRequest request() {
		return servletRequestAttributes().getRequest();
	}

	/**
	 * Response http servlet response.
	 *
	 * @return the http servlet response
	 */
	public HttpServletResponse response() {
		return servletRequestAttributes().getResponse();
	}

	/**
	 * Session http session.
	 *
	 * @param request the request
	 * @return the http session
	 */
	public HttpSession session(HttpServletRequest request) {
		return request.getSession();
	}

	/**
	 * Headers http headers.
	 *
	 * @param request the request
	 * @return the http headers
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
	 * Attributes map.
	 *
	 * @param request the request
	 * @return the map
	 */
	public Map<String, Object> attributes(HttpServletRequest request) {
		return BaseStreamUtils.convert(request.getHeaderNames())
			.collect(Collectors.toMap(Function.identity(), request::getAttribute));
	}

	/**
	 * Param map map.
	 *
	 * @param request   the request
	 * @param delimiter the delimiter
	 * @return the map
	 */
	public Map<String, String> paramMap(HttpServletRequest request, CharSequence delimiter) {
		return request.getParameterMap()
			.entrySet()
			.stream()
			.collect(Collectors.toMap(Map.Entry::getKey,
				entry -> String.join(delimiter, entry.getValue())));
	}

	/**
	 * Params multi value map.
	 *
	 * @param request the request
	 * @return the multi value map
	 */
	public MultiValueMap<String, String> params(HttpServletRequest request) {
		Map<String, List<String>> map = request.getParameterMap()
			.entrySet()
			.stream()
			.collect(Collectors.toMap(Map.Entry::getKey,
				entry -> Lists.newArrayList(entry.getValue())));
		return new LinkedMultiValueMap<>(map);
	}

	/**
	 * Real ip string.
	 *
	 * @param request the request
	 * @return the string
	 */
	public String realIp(HttpServletRequest request) {
		// 这个一般是Nginx反向代理设置的参数
		String ip = request.getHeader("X-Real-IP");
		if (ObjectUtils.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
			ip = request.getHeader("X-Forwarded-For");
		}
		if (ObjectUtils.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ObjectUtils.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ObjectUtils.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		// 处理多IP的情况（只取第一个IP）
		return ip != null && ip.contains(HTTP_IP_SPLIT) ? ip.split(HTTP_IP_SPLIT)[0] : ip;
	}

	/**
	 * Out.
	 *
	 * @param response the response
	 * @param data     the data
	 */
	public void outJson(HttpServletResponse response, Object data) {
		out(response, JsonMapperUtils.writeValueAsString(data), MediaType.APPLICATION_JSON_VALUE);
	}

	/**
	 * 根据response写入返回值
	 *
	 * @param response    response
	 * @param message     写入的信息
	 * @param contentType contentType {@link MediaType}
	 */
	public void out(HttpServletResponse response, String message, String contentType) {
		response.setContentType(contentType);
		response.setCharacterEncoding("UTF-8");
		try (PrintWriter out = response.getWriter()) {
			out.print(message);
			out.flush();
		} catch (IOException exception) {
			throw new ErrorResponseException(HttpStatus.INTERNAL_SERVER_ERROR, exception);
		}
	}
}
