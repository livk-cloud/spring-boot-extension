package com.livk.commons.spring;

import com.livk.auto.service.annotation.SpringFactories;
import com.livk.commons.bean.util.ClassUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * <p>
 * OtherBannerHandler
 * </p>
 *
 * @author livk
 */
@AutoConfiguration
@SpringFactories
public class PageHelperBannerSettings implements EnvironmentPostProcessor {

    private static final String PAGEHELPER_BANNER = "pagehelper.banner";

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        if (ClassUtils.isPresent("com.github.pagehelper.PageInterceptor", Thread.currentThread().getContextClassLoader())) {
            Boolean pageHelperBannerEnable = environment.getProperty(PAGEHELPER_BANNER, Boolean.class, false);
            System.setProperty(PAGEHELPER_BANNER, pageHelperBannerEnable.toString());
        }
    }
}
