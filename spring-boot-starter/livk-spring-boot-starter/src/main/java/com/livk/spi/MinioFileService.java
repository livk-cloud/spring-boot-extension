package com.livk.spi;

import java.io.InputStream;

/**
 * <p>
 * MinioFileService
 * </p>
 *
 * @author livk
 * @date 2022/3/18
 */
public class MinioFileService implements FileService {

    @Override
    public void upload(InputStream inputStream) {

    }

    @Override
    public byte[] download(final String filename) {
        return new byte[0];
    }

    @Override
    public String getType() {
        return this.getClass().getTypeName();
    }

}
