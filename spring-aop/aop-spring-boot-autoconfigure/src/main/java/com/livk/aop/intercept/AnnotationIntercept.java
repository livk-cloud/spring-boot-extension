package com.livk.aop.intercept;

import com.livk.aop.support.AnnotationInvoke;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.annotation.Annotation;

/**
 * <p>
 * AnnotationIntercept
 * </p>
 *
 * @author livk
 * @date 2022/7/5
 */
public interface AnnotationIntercept {

    Class<? extends Annotation> getType();

    default boolean supportMethod() {
        return true;
    }

    default boolean supportClass() {
        return false;
    }

    Object invoke(MethodInvocation invocation, AnnotationInvoke<?> invoke) throws Throwable;
}
