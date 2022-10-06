package com.livk.aop;

import com.livk.aop.interceptor.AnnotationInterceptor;
import com.livk.aop.proxy.AnnotationAutoScanProxy;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.List;

/**
 * <p>
 * AopAutoConfiguration
 * </p>
 *
 * @author livk
 * @date 2022/7/5
 */
@AutoConfiguration
public class AopAutoConfiguration {

    @Bean
    public AnnotationAutoScanProxy annotationAutoScanProxy(List<AnnotationInterceptor<?>> annotationInterceptors) {
        return new AnnotationAutoScanProxy(annotationInterceptors);
    }
}
