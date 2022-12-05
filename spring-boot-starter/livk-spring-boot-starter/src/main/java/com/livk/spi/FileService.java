package com.livk.spi;

import java.io.InputStream;

/**
 * <p>
 * FileService
 * </p>
 *
 * @author livk
 */
public interface FileService {

    void upload(InputStream inputStream);

    byte[] download(String filename);

    String getType();

}
