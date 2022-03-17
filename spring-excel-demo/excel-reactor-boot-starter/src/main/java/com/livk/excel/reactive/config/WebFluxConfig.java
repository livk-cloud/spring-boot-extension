package com.livk.excel.reactive.config;

import com.livk.excel.reactive.resolver.ExcelMethodArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.result.method.annotation.ArgumentResolverConfigurer;

/**
 * <p>
 * WebFluxConfig
 * </p>
 *
 * @author livk
 * @date 2022/3/9
 */
@Configuration(proxyBeanMethods = false)
public class WebFluxConfig implements WebFluxConfigurer {

    @Override
    public void configureArgumentResolvers(ArgumentResolverConfigurer configurer) {
        configurer.addCustomResolver(new ExcelMethodArgumentResolver());
    }


}
