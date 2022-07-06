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
public class AopAnnoAspectCopy implements AnnotationIntercept {
    @Override
    public Class<? extends Annotation> type() {
        return AopAnno.class;
    }

    @Override
    public Object invoke(MethodInvocation invocation, AnnotationInvoke<?> invoke) throws Throwable {
        AopAnno aopAnno = (AopAnno) invoke.getAnnotation();
        log.info("copy anno:{}", aopAnno);
        log.info("copy anno start");
        Object proceed = invocation.proceed();
        log.info("copy anno end");
        return proceed;
    }
}
