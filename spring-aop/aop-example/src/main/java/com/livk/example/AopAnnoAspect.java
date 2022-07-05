package com.livk.example;

import com.livk.aop.intercept.AnnotationIntercept;
import com.livk.aop.support.AnnotationInvoke;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;

/**
 * <p>
 * AopAnnoAspect
 * </p>
 *
 * @author livk
 * @date 2022/7/5
 */
@Slf4j
@Component
public class AopAnnoAspect implements AnnotationIntercept {
    @Override
    public Class<? extends Annotation> getType() {
        return AopAnno.class;
    }

    @Override
    public Object invoke(MethodInvocation invocation, AnnotationInvoke<?> invoke) throws Throwable {
        AopAnno aopAnno = (AopAnno) invoke.getAnnotation();
        log.info("anno:{}", aopAnno);
        log.info("start");
        Object proceed = invocation.proceed();
        log.info("end");
        return proceed;
    }

    @Override
    public boolean supportMethod() {
        return AnnotationIntercept.super.supportMethod();
    }

    @Override
    public boolean supportClass() {
        return AnnotationIntercept.super.supportClass();
    }
}
