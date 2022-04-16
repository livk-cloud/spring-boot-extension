package com.livk.spi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * <p>
 * FileServiceFactory
 * </p>
 *
 * @author livk
 * @date 2022/3/18
 */
@Slf4j
@Component
public class FileServiceFactory extends AbstractServiceLoad<FileService> {

    @Override
    protected String getKey(FileService fileService) {
        return fileService.getType();
    }
}
