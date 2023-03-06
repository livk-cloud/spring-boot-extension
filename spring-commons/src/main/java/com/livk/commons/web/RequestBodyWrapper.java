package com.livk.commons.web;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

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
 */
public final class RequestBodyWrapper extends HttpServletRequestWrapper {

    @Setter
    @Getter
    private String body;

    /**
     * Instantiates a new Request body wrapper.
     *
     * @param request the request
     */
    public RequestBodyWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public ServletInputStream getInputStream() throws UnsupportedEncodingException {
        return new RequestServletInputStream(getRequest(), body);
    }

    @SneakyThrows
    @Override
    public int getContentLength() {
        return body.getBytes(getRequest().getCharacterEncoding()).length;
    }

    @SneakyThrows
    @Override
    public long getContentLengthLong() {
        return body.getBytes(getRequest().getCharacterEncoding()).length;
    }

    @Override
    public String getContentType() {
        return MediaType.APPLICATION_JSON_VALUE;
    }

    @Override
    public Enumeration<String> getHeaders(String name) {
        if (HttpHeaders.CONTENT_TYPE.equalsIgnoreCase(name)) {
            return new HeaderEnumeration(getContentType());
        }
        return super.getHeaders(name);
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

    private static class HeaderEnumeration implements Enumeration<String> {

        private final String contentType;

        private boolean hasMoreElements = false;

        /**
         * Instantiates a new Header enumeration.
         *
         * @param contentType the content type
         */
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
            } else {
                hasMoreElements = true;
                return contentType;
            }
        }

    }

}
