package com.livk.selector;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;

/**
 * <p>
 * LivkAutoConfiguration
 * </p>
 *
 * @author livk
 * @date 2022/9/7
 */
@Slf4j
@AutoConfiguration
public class LivkAutoConfiguration {

    @PostConstruct
    public void init() {
        log.info("{} is init", LivkAutoConfiguration.class);
    }
}
