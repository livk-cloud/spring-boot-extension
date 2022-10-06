package com.livk.aop.proxy;

import com.livk.aop.interceptor.AnnotationInterceptor;
import lombok.RequiredArgsConstructor;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.Advisor;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.List;
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

    private final List<AnnotationInterceptor<?>> annotationInterceptors;

    @Override
    protected Collection<Advice> getProxyClass() {
        return annotationInterceptors.stream()
                .map(AnnotationInterceptor::getAdvisor)
                .map(Advisor::getAdvice)
                .collect(Collectors.toSet());
    }

    @Override
    protected Collection<Class<? extends Annotation>> findProxyAnnotations() {
        return annotationInterceptors.stream().map(AnnotationInterceptor::annotationClass).collect(Collectors.toSet());
    }

    @Override
    protected Class<? extends MethodInterceptor> findMethodInvocations() {
        return AnnotationInterceptor.class;
    }
}
