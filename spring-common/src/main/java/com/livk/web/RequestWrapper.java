package com.livk.web;

import com.livk.util.ObjectUtils;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import lombok.SneakyThrows;
import org.springframework.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.NoSuchElementException;

/**
 * <p>
 * RequestWrapper
 * </p>
 *
 * @author livk
 * @date 2022/1/25
 */
public class RequestWrapper extends HttpServletRequestWrapper {

	private final HttpServletRequest request;

	private final String requestJson;

	private final String contentType;

	public RequestWrapper(HttpServletRequest request, String json) {
		this(request, json, null);
	}

	public RequestWrapper(HttpServletRequest request, String json, String contentType) {
		super(request);
		this.request = request;
		this.requestJson = json;
		this.contentType = contentType;
	}

	@Override
	public ServletInputStream getInputStream() throws UnsupportedEncodingException {
		return new RequestServletInputStream(request, requestJson);
	}

	@SneakyThrows
	@Override
	public int getContentLength() {
		return requestJson.getBytes(request.getCharacterEncoding()).length;
	}

	@SneakyThrows
	@Override
	public long getContentLengthLong() {
		return requestJson.getBytes(request.getCharacterEncoding()).length;
	}

	@Override
	public String getContentType() {
		return StringUtils.hasText(contentType) ? contentType : request.getContentType();
	}

	@Override
	public Enumeration<String> getHeaders(String name) {
		if (ObjectUtils.allChecked(StringUtils::hasText, name, contentType) && name.equalsIgnoreCase("Content-Type")) {
			return new HeaderEnumeration(contentType);
		}
		return super.getHeaders(name);
	}

	private static class RequestServletInputStream extends ServletInputStream {

		private final InputStream in;

		public RequestServletInputStream(HttpServletRequest request, String json) throws UnsupportedEncodingException {
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
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		public int read() throws IOException {
			return in.read();
		}

	}

	private static class HeaderEnumeration implements Enumeration<String> {

		private final String contentType;

		private boolean hasMoreElements = false;

		public HeaderEnumeration(String contentType) {
			this.contentType = contentType;
		}

		@Override
		public boolean hasMoreElements() {
			return !hasMoreElements;
		}

		@Override
		public String nextElement() {
			if (hasMoreElements) {
				throw new NoSuchElementException();
			}
			else {
				hasMoreElements = true;
				return contentType;
			}
		}

	}

}
