package com.livk.spi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

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
public class FileServiceFactory implements InitializingBean {

    private final Map<String, FileService> fileServiceMap = new ConcurrentHashMap<>();

    @Override
    public void afterPropertiesSet() {
        var serviceLoader = ServiceLoader.load(FileService.class);
        for (FileService next : serviceLoader) {
            fileServiceMap.put(next.getType(), next);
        }
        log.info("{}", fileServiceMap);
    }
}
