package com.livk.commons.web.multipart;

import com.livk.commons.io.FileUtils;
import com.livk.commons.util.ObjectUtils;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * <p>
 * ByteMultipartFile
 * </p>
 * <p>
 * 参考{@see org.springframework.mock.web.MockMultipartFile}
 * </p>
 *
 * @author livk
 */
public class ByteMultipartFile implements MultipartFile {

    private final String name;

    private final String originalFilename;

    private final String contentType;

    private final byte[] bytes;

    /**
     * Instantiates a new Byte multipart file.
     *
     * @param name             the name
     * @param originalFilename the original filename
     * @param contentType      the content type
     * @param content          the content
     */
    public ByteMultipartFile(String name, String originalFilename, String contentType, byte[] content) {
        Assert.hasLength(name, "Name must not be empty");
        this.name = name;
        this.originalFilename = (originalFilename != null ? originalFilename : "");
        this.contentType = contentType;
        this.bytes = (content != null ? content : new byte[0]);
    }

    /**
     * Instantiates a new Byte multipart file.
     *
     * @param name    the name
     * @param content the content
     */
    public ByteMultipartFile(String name, byte[] content) {
        this(name, "", "", content);
    }

    /**
     * Instantiates a new Byte multipart file.
     *
     * @param name             the name
     * @param originalFilename the original filename
     * @param contentType      the content type
     * @param contentStream    the content stream
     * @throws IOException the io exception
     */
    public ByteMultipartFile(String name, String originalFilename, String contentType, InputStream contentStream) throws IOException {
        this(name, originalFilename, contentType, FileUtils.copyToByteArray(contentStream));
    }

    /**
     * Instantiates a new Byte multipart file.
     *
     * @param name          the name
     * @param contentStream the content stream
     * @throws IOException the io exception
     */
    public ByteMultipartFile(String name, InputStream contentStream) throws IOException {
        this(name, FileUtils.copyToByteArray(contentStream));
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getOriginalFilename() {
        return originalFilename;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public boolean isEmpty() {
        return ObjectUtils.isEmpty(bytes);
    }

    @Override
    public long getSize() {
        return bytes.length;
    }

    @Override
    public byte[] getBytes() throws IOException {
        return bytes;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(bytes);
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        FileUtils.copy(bytes, dest);
    }
}
