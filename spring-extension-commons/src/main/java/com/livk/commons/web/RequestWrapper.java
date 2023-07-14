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

package com.livk.commons.web;

import com.livk.commons.collect.util.StreamUtils;
import com.livk.commons.util.ObjectUtils;
import com.livk.commons.web.util.WebUtils;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import lombok.SneakyThrows;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * The type Request wrapper.
 *
 * @author livk
 */
public class RequestWrapper extends HttpServletRequestWrapper {

	private final HttpHeaders headers = new HttpHeaders();

	private final Map<String, String[]> parameter = new LinkedHashMap<>(16);

	private String body;

	private boolean bodyReviseStatus = false;

	/**
	 * Instantiates a new Request wrapper.
	 *
	 * @param request the request
	 */
	public RequestWrapper(HttpServletRequest request) {
		super(request);
		headers.putAll(WebUtils.headers(request));
		parameter.putAll(request.getParameterMap());
	}

	/**
	 * Sets body.
	 *
	 * @param body the body
	 */
	public void setBody(String body) {
		bodyReviseStatus = true;
		this.body = body;
	}

	/**
	 * Add header.
	 *
	 * @param name  the name
	 * @param value the value
	 */
	public void addHeader(String name, String value) {
		headers.add(name, value);
	}

	/**
	 * Put parameter.
	 *
	 * @param name   the name
	 * @param values the values
	 */
	public void putParameter(String name, String[] values) {
		parameter.merge(name, values, StreamUtils::concatDistinct);
	}

	/**
	 * Put parameter.
	 *
	 * @param name  the name
	 * @param value the value
	 */
	public void putParameter(String name, String value) {
		putParameter(name, new String[]{value});
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		return bodyReviseStatus ? new RequestServletInputStream(getRequest(), body) : super.getInputStream();
	}

	@SneakyThrows
	@Override
	public int getContentLength() {
		return bodyReviseStatus ? body.getBytes(getRequest().getCharacterEncoding()).length : super.getContentLength();
	}

	@SneakyThrows
	@Override
	public long getContentLengthLong() {
		return bodyReviseStatus ? body.getBytes(getRequest().getCharacterEncoding()).length : super.getContentLengthLong();
	}

	@Override
	public String getContentType() {
		return bodyReviseStatus ? MediaType.APPLICATION_JSON_VALUE : super.getContentType();
	}

	@Override
	public String getParameter(String name) {
		String[] values = getParameterValues(name);
		return ObjectUtils.isEmpty(values) ? null : values[0];
	}

	@Override
	public Map<String, String[]> getParameterMap() {
		return parameter;
	}

	@Override
	public Enumeration<String> getParameterNames() {
		return Collections.enumeration(parameter.keySet());
	}

	@Override
	public String[] getParameterValues(String name) {
		return parameter.get(name);
	}

	@Override
	public String getHeader(String name) {
		if (HttpHeaders.CONTENT_TYPE.equalsIgnoreCase(name) && bodyReviseStatus) {
			return getContentType();
		}
		return headers.getFirst(name);
	}

	@Override
	public Enumeration<String> getHeaderNames() {
		Set<String> headerNames = headers.keySet();
		if (bodyReviseStatus) {
			headerNames.add(HttpHeaders.CONTENT_TYPE);
		}
		return Collections.enumeration(headers.keySet());
	}

	@Override
	public Enumeration<String> getHeaders(String name) {
		Set<String> headerValues = new HashSet<>();
		if (HttpHeaders.CONTENT_TYPE.equalsIgnoreCase(name) && bodyReviseStatus) {
			headerValues.add(getContentType());
		}
		List<String> list = headers.get(name);
		if (!CollectionUtils.isEmpty(list)) {
			headerValues.addAll(list);
		}
		return Collections.enumeration(headerValues);
	}

	private static class RequestServletInputStream extends ServletInputStream {

		private final InputStream in;

		/**
		 * Instantiates a new Request servlet input stream.
		 *
		 * @param request the request
		 * @param json    the json
		 * @throws UnsupportedEncodingException the unsupported encoding exception
		 */
		public RequestServletInputStream(ServletRequest request, String json) throws UnsupportedEncodingException {
			in = new ByteArrayInputStream(json.getBytes(request.getCharacterEncoding()));
		}

		@Override
		public boolean isFinished() {
			return false;
		}

		@Override
		public boolean isReady() {
			return false;
		}

		@Override
		public void setReadListener(ReadListener listener) {
			try {
				listener.onDataAvailable();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		public int read() throws IOException {
			return in.read();
		}

	}
}
