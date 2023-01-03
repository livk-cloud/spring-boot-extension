package com.livk.commons.spring;

import com.livk.auto.service.annotation.SpringFactories;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.support.SpringFactoriesLoader;

import java.util.Objects;

/**
 * <p>
 * BeanFactoryPostProcessorLoad
 * </p>
 *
 * @author livk
 * @date 2023/1/3
 */
@AutoConfiguration
@SpringFactories(type = ApplicationContextInitializer.class)
public class BeanFactoryPostProcessorLoad implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        SpringFactoriesLoader.loadFactories(BeanFactoryPostProcessor.class,
                        BeanFactoryPostProcessorLoad.class.getClassLoader())
                .stream()
                .filter(Objects::nonNull)
                .forEach(applicationContext::addBeanFactoryPostProcessor);
    }
}
