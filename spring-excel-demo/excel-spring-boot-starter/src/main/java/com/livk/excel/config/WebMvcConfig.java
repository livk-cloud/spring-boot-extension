package com.livk.excel.config;

import com.livk.excel.resolver.ExcelMethodArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * <p>
 * WebMvcConfig
 * </p>
 *
 * @author livk
 * @date 2022/1/17
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new ExcelMethodArgumentResolver());
    }
}
