package com.livk.commons.spring;

import com.livk.auto.service.annotation.SpringFactories;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

/**
 * <p>
 * OtherBannerHandler
 * </p>
 *
 * @author livk
 */
@AutoConfiguration
@SpringFactories(type = ApplicationContextInitializer.class)
public class PageHelperBannerSettings implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private static final String PAGEHELPER_BANNER = "pagehelper.banner";

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        Environment environment = applicationContext.getEnvironment();

        boolean PageHelperBannerEnable = Boolean
                .parseBoolean(System.getProperty(PAGEHELPER_BANNER, environment.getProperty(PAGEHELPER_BANNER)));
        System.setProperty(PAGEHELPER_BANNER, Boolean.toString(PageHelperBannerEnable));
    }

}
