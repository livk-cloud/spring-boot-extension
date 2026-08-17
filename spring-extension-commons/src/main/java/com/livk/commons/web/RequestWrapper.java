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

package com.livk.commons.web;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.livk.commons.util.HttpServletUtils;
import org.springframework.util.ObjectUtils;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StreamUtils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Wrapper for HttpServletRequest allowing body, header and parameter modifications.
 *
 * @author livk
 * @see org.springframework.web.util.ContentCachingRequestWrapper
 */
public class RequestWrapper extends HttpServletRequestWrapper {

	private final HttpHeaders headers = new HttpHeaders();

	private final HttpParameters parameter = new HttpParameters();

	private byte[] body;

	private boolean bodyReviseStatus = false;

	private String contentType;

	/**
	 * Constructs a RequestWrapper from a request.
	 * @param request the request
	 */
	public RequestWrapper(HttpServletRequest request) {
		super(request);
		this.headers.putAll(HttpServletUtils.headers(request));
		this.parameter.putAll(HttpServletUtils.params(request));
		this.contentType = request.getContentType();
	}

	/**
	 * Sets the request body.
	 * @param body the body
	 */
	public void body(byte[] body) {
		body(body, MediaType.APPLICATION_JSON_VALUE);
	}

	public void body(byte[] body, String contentType) {
		this.bodyReviseStatus = true;
		this.body = body;
		this.contentType = contentType;
	}

	/**
	 * 添加http header.
	 * @param name the name
	 * @param values the values
	 */
	public void addHeader(String name, String[] values) {
		addHeader(name, Lists.newArrayList(values));
	}

	/**
	 * 添加http header.
	 * @param name the name
	 * @param values the values
	 */
	public void addHeader(String name, List<String> values) {
		this.headers.addAll(name, values);
	}

	/**
	 * Adds an HTTP header.
	 * @param name the name
	 * @param value the value
	 */
	public void addHeader(String name, String value) {
		this.headers.add(name, value);
	}

	/**
	 * 添加http parameter.
	 * @param name the name
	 * @param values the values
	 */
	public void addParameter(String name, String[] values) {
		addParameter(name, Lists.newArrayList(values));
	}

	/**
	 * 添加http parameter.
	 * @param name the name
	 * @param values the values
	 */
	public void addParameter(String name, List<String> values) {
		this.parameter.addAll(name, values);
	}

	/**
	 * 添加http parameter.
	 * @param name the name
	 * @param value the value
	 */
	public void addParameter(String name, String value) {
		this.parameter.add(name, value);
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		if (ObjectUtils.isEmpty(this.body)) {
			this.body = StreamUtils.copyToByteArray(super.getInputStream());
		}
		return new ByteArrayServletInputStream(this.body);
	}

	@Override
	public String getCharacterEncoding() {
		String enc = super.getCharacterEncoding();
		return enc != null ? enc : "UTF-8";
	}

	@Override
	public BufferedReader getReader() throws IOException {
		if (ObjectUtils.isEmpty(this.body)) {
			this.body = StreamUtils.copyToByteArray(super.getInputStream());
		}
		return new BufferedReader(new ByteArrayReader(this.body, Charset.forName(getCharacterEncoding())));
	}

	@Override
	public int getContentLength() {
		return this.bodyReviseStatus && !ObjectUtils.isEmpty(this.body) ? this.body.length : super.getContentLength();
	}

	@Override
	public long getContentLengthLong() {
		return this.bodyReviseStatus && !ObjectUtils.isEmpty(this.body) ? this.body.length
				: super.getContentLengthLong();
	}

	@Override
	public String getContentType() {
		return this.contentType;
	}

	@Override
	public String getParameter(String name) {
		String[] values = getParameterValues(name);
		return ObjectUtils.isEmpty(values) ? null : values[0];
	}

	@Override
	public Map<String, String[]> getParameterMap() {
		return Maps.transformValues(this.parameter, parameterValues -> parameterValues.toArray(String[]::new));
	}

	@Override
	public Enumeration<String> getParameterNames() {
		return Collections.enumeration(this.parameter.keySet());
	}

	@Override
	public String[] getParameterValues(String name) {
		return getParameterMap().get(name);
	}

	@Override
	public String getHeader(String name) {
		if (HttpHeaders.CONTENT_TYPE.equalsIgnoreCase(name) && this.bodyReviseStatus) {
			return getContentType();
		}
		return this.headers.getFirst(name);
	}

	@Override
	public Enumeration<String> getHeaderNames() {
		Set<String> headerNames = new HashSet<>(this.headers.headerNames());
		if (this.bodyReviseStatus) {
			headerNames.add(HttpHeaders.CONTENT_TYPE);
		}
		return Collections.enumeration(headerNames);
	}

	@Override
	public Enumeration<String> getHeaders(String name) {
		Set<String> headerValues = new HashSet<>();
		if (HttpHeaders.CONTENT_TYPE.equalsIgnoreCase(name) && this.bodyReviseStatus) {
			headerValues.add(getContentType());
		}
		List<String> list = this.headers.get(name);
		if (!CollectionUtils.isEmpty(list)) {
			headerValues.addAll(list);
		}
		return Collections.enumeration(headerValues);
	}

	public byte[] getContentAsByteArray() throws IOException {
		if (ObjectUtils.isEmpty(this.body)) {
			this.body = StreamUtils.copyToByteArray(super.getInputStream());
		}
		return Arrays.copyOf(this.body, this.body.length);
	}

	public String getContentAsString() throws IOException {
		Charset charset = Charset.forName(this.getCharacterEncoding());
		if (ObjectUtils.isEmpty(this.body)) {
			return StreamUtils.copyToString(super.getInputStream(), charset);
		}
		return new String(this.body, charset);
	}

	private static class ByteArrayReader extends InputStreamReader {

		/**
		 * Constructs a ByteArrayReader.
		 * @param bytes the bytes
		 * @param charset the charset
		 */
		ByteArrayReader(byte[] bytes, Charset charset) {
			super(new ByteArrayInputStream(bytes), charset);
		}

	}

	private static class ByteArrayServletInputStream extends ServletInputStream {

		private final ByteArrayInputStream in;

		/**
		 * Constructs a ByteArrayServletInputStream.
		 * @param body the json
		 */
		ByteArrayServletInputStream(byte[] body) {
			this.in = new ByteArrayInputStream(body);
		}

		@Override
		public boolean isFinished() {
			return this.in.available() == 0;
		}

		@Override
		public boolean isReady() {
			return true;
		}

		@Override
		public void setReadListener(ReadListener listener) {
			throw new UnsupportedOperationException();
		}

		@Override
		public int read() throws IOException {
			return this.in.read();
		}

	}

}
