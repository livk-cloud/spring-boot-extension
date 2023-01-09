package com.livk.commons.web;

import com.livk.commons.util.ObjectUtils;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
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
public final class RequestWrapper extends HttpServletRequestWrapper {

    private final ServletRequest request;

    private final String requestJson;

    private final MediaType contentType;

    /**
     * Instantiates a new Request wrapper.
     *
     * @param request the request
     * @param json    the json
     */
    public RequestWrapper(HttpServletRequest request, String json) {
        this(request, json, MediaType.APPLICATION_JSON);
    }

    /**
     * Instantiates a new Request wrapper.
     *
     * @param request     the request
     * @param json        the json
     * @param contentType the content type
     */
    public RequestWrapper(HttpServletRequest request, String json, String contentType) {
        this(request, json, MediaType.valueOf(contentType));
    }

    /**
     * Instantiates a new Request wrapper.
     *
     * @param request     the request
     * @param json        the json
     * @param contentType the content type
     */
    public RequestWrapper(HttpServletRequest request, String json, MediaType contentType) {
        super(request);
        this.request = getRequest();
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
        return ObjectUtils.isEmpty(contentType) ? request.getContentType() : contentType.getType();
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
