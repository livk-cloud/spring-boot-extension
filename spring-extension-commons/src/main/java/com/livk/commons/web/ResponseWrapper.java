/*
 * Copyright 2021-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * you may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.livk.commons.web;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StreamUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.charset.Charset;

/**
 * <p>
 * 可用于重写返回值 基于HttpServletResponse
 * </p>
 *
 * @author livk
 * @see org.springframework.web.util.ContentCachingResponseWrapper
 */
public class ResponseWrapper extends HttpServletResponseWrapper {

	private final ByteArrayOutputStream buffer = new ByteArrayOutputStream();

	private final ServletOutputStream outputStream = new WrapperOutputStream(this.buffer);

	private PrintWriter writer;

	private final Charset characterEncoding;

	/**
	 * 创建ResponseWrapper
	 * @param response the response
	 */
	public ResponseWrapper(HttpServletResponse response) {
		super(response);
		this.characterEncoding = initCharacterEncoding(response);
	}

	private Charset initCharacterEncoding(HttpServletResponse response) {
		String characterEncoding = response.getCharacterEncoding();
		if (characterEncoding == null) {
			return Charset.defaultCharset();
		}
		return Charset.forName(characterEncoding, Charset.defaultCharset());
	}

	@Override
	public ServletOutputStream getOutputStream() {
		return this.outputStream;
	}

	@Override
	public PrintWriter getWriter() throws UnsupportedEncodingException {
		if (this.writer == null) {
			Writer targetWriter = new OutputStreamWriter(this.buffer, getCharacterEncoding());
			this.writer = new PrintWriter(targetWriter);
		}
		return writer;
	}

	@Override
	public void reset() {
		this.buffer.reset();
	}

	@Override
	public String getCharacterEncoding() {
		return this.characterEncoding.name();
	}

	/**
	 * 根据默认编码获取response body数据
	 * @return String
	 * @see characterEncoding
	 */
	public String getContentAsString() {
		return getContentAsString(this.characterEncoding);
	}

	/**
	 * 根据编码获取response body数据
	 * @param charset 编码格式
	 * @return String
	 */
	public String getContentAsString(Charset charset) {
		return this.buffer.toString(charset);
	}

	/**
	 * 获取response body
	 * @return the byte []
	 */
	public byte[] getContentAsByteArray() {
		return this.buffer.toByteArray();
	}

	/**
	 * 修改response body
	 * @param bytes byte[]
	 */
	public void replaceBody(byte[] bytes) throws IOException {
		this.buffer.reset();
		StreamUtils.copy(bytes, this.buffer);
	}

	/**
	 * 修改response body
	 * @param content string
	 */
	public void replaceBody(String content) throws IOException {
		replaceBody(content, this.characterEncoding);
	}

	/**
	 * 修改response body
	 * @param content string
	 * @param charset charset
	 */
	public void replaceBody(String content, Charset charset) throws IOException {
		replaceBody(content.getBytes(charset));
	}

	@RequiredArgsConstructor
	private static class WrapperOutputStream extends ServletOutputStream {

		private final ByteArrayOutputStream outputStream;

		@Override
		public void write(int b) {
			this.outputStream.write(b);
		}

		@Override
		public void flush() throws IOException {
			this.outputStream.flush();
		}

		@Override
		public void setWriteListener(WriteListener writeListener) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean isReady() {
			return true;
		}

		@Override
		public String toString() {
			return this.outputStream.toString();
		}

	}

}
