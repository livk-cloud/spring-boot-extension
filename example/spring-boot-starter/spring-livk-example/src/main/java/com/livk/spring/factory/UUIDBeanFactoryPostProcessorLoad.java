package com.livk.spring.factory;

import com.livk.auto.service.annotation.SpringFactories;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author livk
 */
@AutoConfiguration
@SpringFactories(ApplicationContextInitializer.class)
public class UUIDBeanFactoryPostProcessorLoad implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        applicationContext.addBeanFactoryPostProcessor(new UUIDBeanFactoryPostProcessor());
    }
}
