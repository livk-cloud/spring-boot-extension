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

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StreamUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 * <p>
 * 可用于重写返回值 基于HttpServletResponse
 * </p>
 *
 * @author livk
 */
public class ResponseWrapper extends HttpServletResponseWrapper {

	private final ByteArrayOutputStream buffer = new ByteArrayOutputStream();

	/**
	 * 创建ResponseWrapper
	 * @param response the response
	 */
	public ResponseWrapper(HttpServletResponse response) {
		super(response);
	}

	@Override
	public ServletOutputStream getOutputStream() {
		return new WrapperOutputStream(super.getResponse(), buffer);
	}

	@Override
	public PrintWriter getWriter() {
		return new PrintWriter(new OutputStreamWriter(buffer));
	}

	@Override
	public void reset() {
		buffer.reset();
	}

	/**
	 * 获取response body
	 * @return the byte []
	 */
	public byte[] getResponseData() {
		return buffer.toByteArray();
	}

	/**
	 * 修改response body
	 * @param bytes byte[]
	 */
	public void setResponseData(byte[] bytes) throws IOException {
		buffer.reset();
		StreamUtils.copy(bytes, buffer);
	}

	@RequiredArgsConstructor
	private static class WrapperOutputStream extends ServletOutputStream {

		private final ServletResponse response;

		private final ByteArrayOutputStream outputStream;

		@Override
		public void write(int b) {
			outputStream.write(b);
		}

		@Override
		public void flush() throws IOException {
			if (!this.response.isCommitted()) {
				byte[] body = this.outputStream.toByteArray();
				ServletOutputStream stream = this.response.getOutputStream();
				stream.write(body);
				stream.flush();
			}
		}

		@Override
		public void setWriteListener(WriteListener writeListener) {
			throw new UnsupportedOperationException("setWriteListener");
		}

		@Override
		public boolean isReady() {
			return false;
		}

		@Override
		public String toString() {
			return this.outputStream.toString();
		}

	}

}
