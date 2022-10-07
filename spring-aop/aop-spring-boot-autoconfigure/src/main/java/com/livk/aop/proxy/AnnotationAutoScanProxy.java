package com.livk.aop.proxy;

import com.livk.aop.interceptor.AnnotationInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.aop.PointcutAdvisor;
import org.springframework.beans.factory.ObjectProvider;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * <p>
 * AnnotationAutoScanProxy
 * </p>
 *
 * @author livk
 * @date 2022/10/6
 */
@RequiredArgsConstructor
public class AnnotationAutoScanProxy extends AbstractAutoScanProxy {

    public static final String BEAN_NAME = "com.livk.aop.proxy.AnnotationAutoScanProxy";

    private final ObjectProvider<AnnotationInterceptor<?>> annotationInterceptors;

    @Override
    protected Collection<PointcutAdvisor> getPointcutAdvisors() {
        return annotationInterceptors.stream()
                .map(AnnotationInterceptor::getAdvisor)
                .collect(Collectors.toSet());
    }

    @Override
    protected Collection<Class<? extends Annotation>> findProxyAnnotations() {
        return annotationInterceptors.stream().map(AnnotationInterceptor::annotationClass).collect(Collectors.toSet());
    }
}
