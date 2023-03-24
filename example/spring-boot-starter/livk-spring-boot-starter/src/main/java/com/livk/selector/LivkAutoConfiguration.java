package com.livk.selector;

import com.livk.auto.service.annotation.SpringAutoService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;

/**
 * <p>
 * LivkAutoConfiguration
 * </p>
 *
 * @author livk
 */
@Slf4j
@AutoConfiguration
@SpringAutoService(LivkImport.class)
public class LivkAutoConfiguration {

    @PostConstruct
    public void init() {
        log.info("{} is init", LivkAutoConfiguration.class);
    }
}
