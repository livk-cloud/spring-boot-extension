package com.livk.spi;

import com.livk.auto.service.annotation.SpringAutoService;
import org.springframework.stereotype.Component;

/**
 * <p>
 * FileServiceFactory
 * </p>
 *
 * @author livk
 */
@Component
@SpringAutoService
public class FileServiceFactory extends AbstractServiceLoad<FileService> {

    @Override
    protected String getKey(FileService fileService) {
        return fileService.getType();
    }

}
