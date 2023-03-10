package com.livk.spi;

import com.google.auto.service.AutoService;

import java.io.InputStream;

/**
 * <p>
 * MinioFileService
 * </p>
 *
 * @author livk
 */
@AutoService(FileService.class)
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
