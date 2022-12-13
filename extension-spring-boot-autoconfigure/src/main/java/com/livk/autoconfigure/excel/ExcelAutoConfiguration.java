package com.livk.autoconfigure.excel;

import com.alibaba.excel.EasyExcel;
import com.livk.auto.service.annotation.SpringAutoService;
import com.livk.autoconfigure.excel.resolver.ExcelMethodArgumentResolver;
import com.livk.autoconfigure.excel.resolver.ExcelMethodReturnValueHandler;
import com.livk.autoconfigure.excel.resolver.ReactiveExcelMethodArgumentResolver;
import com.livk.autoconfigure.excel.resolver.ReactiveExcelMethodReturnValueHandler;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.result.method.annotation.ArgumentResolverConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * <p>
 * ExcelAutoConfiguration
 * </p>
 *
 * @author livk
 *
 */
@AutoConfiguration
@SpringAutoService
@ConditionalOnClass(EasyExcel.class)
public class ExcelAutoConfiguration {

    @AutoConfiguration
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    public static class ExcelWebMvcAutoConfiguration implements WebMvcConfigurer {
        @Override
        public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
            resolvers.add(new ExcelMethodArgumentResolver());
        }

        @Override
        public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> handlers) {
            handlers.add(new ExcelMethodReturnValueHandler());
        }
    }

    @AutoConfiguration
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
    public static class ExcelWebFluxAutoConfiguration implements WebFluxConfigurer {

        @Bean
        public ReactiveExcelMethodReturnValueHandler reactiveExcelMethodReturnValueHandler() {
            return new ReactiveExcelMethodReturnValueHandler();
        }

        @Override
        public void configureArgumentResolvers(ArgumentResolverConfigurer configurer) {
            configurer.addCustomResolver(new ReactiveExcelMethodArgumentResolver());
        }
    }
}
