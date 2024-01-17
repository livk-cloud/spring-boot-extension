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

package com.livk.commons.web;

import com.livk.commons.util.BaseStreamUtils;
import com.livk.commons.util.ObjectUtils;
import com.livk.commons.util.WebUtils;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StreamUtils;

import java.io.*;
import java.util.*;

/**
 * Request包装器
 * <p>
 * 用于修改body、添加header、添加param
 *
 * @author livk
 */
public class RequestWrapper extends HttpServletRequestWrapper {

	private final HttpHeaders headers = new HttpHeaders();

	private final Map<String, String[]> parameter = new LinkedHashMap<>(16);

	private byte[] body;

	private boolean bodyReviseStatus = false;

	/**
	 * 构建一个RequestWrapper
	 * @param request the request
	 * @throws IOException the io exception
	 */
	public RequestWrapper(HttpServletRequest request) throws IOException {
		super(request);
		headers.putAll(WebUtils.headers(request));
		parameter.putAll(request.getParameterMap());
		body = StreamUtils.copyToByteArray(request.getInputStream());
	}

	/**
	 * 设置request body
	 * @param body the body
	 */
	public void setBody(byte[] body) {
		bodyReviseStatus = true;
		this.body = body;
	}

	/**
	 * 添加http header
	 * @param name the name
	 * @param value the value
	 */
	public void addHeader(String name, String value) {
		headers.add(name, value);
	}

	/**
	 * 添加http parameter.
	 * @param name the name
	 * @param values the values
	 */
	public void putParameter(String name, String[] values) {
		parameter.merge(name, values, BaseStreamUtils::concatDistinct);
	}

	/**
	 * 添加http parameter.
	 * @param name the name
	 * @param value the value
	 */
	public void putParameter(String name, String value) {
		putParameter(name, new String[] { value });
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		return new ByteArrayServletInputStream(body);
	}

	@Override
	public BufferedReader getReader() {
		return new BufferedReader(new ByteArrayReader(body));
	}

	@Override
	public int getContentLength() {
		return bodyReviseStatus ? body.length : super.getContentLength();
	}

	@Override
	public long getContentLengthLong() {
		return bodyReviseStatus ? body.length : super.getContentLengthLong();
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

	private static class ByteArrayReader extends InputStreamReader {

		/**
		 * 创建ByteArrayReader
		 * @param bytes the bytes
		 */
		public ByteArrayReader(byte[] bytes) {
			super(new ByteArrayInputStream(bytes));
		}

	}

	private static class ByteArrayServletInputStream extends ServletInputStream {

		private final InputStream in;

		/**
		 * 创建ByteArrayServletInputStream
		 * @param body the json
		 */
		public ByteArrayServletInputStream(byte[] body) {
			in = new ByteArrayInputStream(body);
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
			throw new UnsupportedOperationException();
		}

		@Override
		public int read() throws IOException {
			return in.read();
		}

	}

}
