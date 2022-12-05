package com.livk.spi;

import org.springframework.stereotype.Component;

/**
 * <p>
 * FileServiceFactory
 * </p>
 *
 * @author livk
 */
@Component
public class FileServiceFactory extends AbstractServiceLoad<FileService> {

    @Override
    protected String getKey(FileService fileService) {
        return fileService.getType();
    }

}
