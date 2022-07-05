package com.livk.redisson.limit;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * <p>
 * WebmvcConfig
 * </p>
 *
 * @author livk
 * @date 2022/7/5
 */
@Configuration
public class WebmvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LimitHandlerInterceptor());
    }
}
