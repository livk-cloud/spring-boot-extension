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
     * Instantiates a new Response wrapper.
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
     * Get response data byte [ ].
     *
     * @return the byte [ ]
     * @throws IOException the io exception
     */
    public byte[] getResponseData() throws IOException {
        this.flushBuffer();
        return buffer.toByteArray();
    }

    private static class WrapperOutputStream extends ServletOutputStream {

        private final ByteArrayOutputStream stream;

        /**
         * Instantiates a new Wrapper output stream.
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
