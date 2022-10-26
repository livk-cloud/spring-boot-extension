package com.livk.example;

import com.livk.aop.autoconfigure.interceptor.AnnotationInterceptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;

@Slf4j
@Component
@RequiredArgsConstructor
public class AopAnnoAnnotationIntercept implements AnnotationInterceptor<AopAnno> {

    @Override
    public Object invoke(@Nonnull MethodInvocation invocation, AopAnno annotation) throws Throwable {
        log.info("copy anno:{}", annotation);
        log.info("copy anno start");
        Object proceed = invocation.proceed();
        log.info("copy anno end");
        return proceed;
    }
}
