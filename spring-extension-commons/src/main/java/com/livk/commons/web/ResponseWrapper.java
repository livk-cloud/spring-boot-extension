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
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import lombok.SneakyThrows;
import org.springframework.lang.NonNull;

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

	private final ByteArrayOutputStream buffer;

	private final ServletOutputStream out;

	private final PrintWriter writer;

	/**
	 * 创建ResponseWrapper
	 *
	 * @param response the response
	 * @throws IOException the io exception
	 */
	public ResponseWrapper(HttpServletResponse response) throws IOException {
		super(response);
		buffer = new ByteArrayOutputStream();
		out = new WrapperOutputStream(buffer);
		writer = new PrintWriter(new OutputStreamWriter(buffer, this.getCharacterEncoding()));
	}

	@Override
	public ServletOutputStream getOutputStream() {
		return out;
	}

	@Override
	public PrintWriter getWriter() {
		return writer;
	}

	@Override
	public void flushBuffer() throws IOException {
		out.flush();
		writer.flush();
	}

	@Override
	public void reset() {
		buffer.reset();
	}

	/**
	 * 获取response body
	 *
	 * @return the byte []
	 * @throws IOException the io exception
	 */
	public byte[] getResponseData() throws IOException {
		this.flushBuffer();
		return buffer.toByteArray();
	}

	private static class WrapperOutputStream extends ServletOutputStream {

		private final ByteArrayOutputStream stream;

		/**
		 * 创建WrapperOutputStream
		 *
		 * @param stream the stream
		 */
		public WrapperOutputStream(ByteArrayOutputStream stream) {
			this.stream = stream;
		}

		@Override
		public void write(int b) {
			stream.write(b);
		}

		@Override
		public void write(@NonNull byte[] b) {
			stream.writeBytes(b);
		}

		@SneakyThrows
		@Override
		public void setWriteListener(WriteListener writeListener) {
			writeListener.onWritePossible();
		}

		@Override
		public boolean isReady() {
			return false;
		}

	}

}
