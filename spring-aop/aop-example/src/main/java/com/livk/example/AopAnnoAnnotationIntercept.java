package com.livk.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.livk.aop.interceptor.AnnotationInterceptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;

@Slf4j
@Component
@RequiredArgsConstructor
public class AopAnnoAnnotationIntercept implements AnnotationInterceptor<AopAnno> {

    private final ObjectMapper objectMapper;

    @Override
    public Object invoke(@Nonnull MethodInvocation invocation, AopAnno annotation) throws Throwable {
        log.info("copy anno:{}", annotation);
        log.info("copy anno start");
        Object proceed = invocation.proceed();
        log.info("copy anno end");
        return proceed;
    }

    @Override
    public Class<AopAnno> annotationClass() {
        return AopAnno.class;
    }

    @Override
    public boolean supportClass() {
        return true;
    }
}
