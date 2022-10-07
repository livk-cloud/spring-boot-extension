package com.livk.aop.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.PointcutAdvisor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.core.GenericTypeResolver;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.annotation.Annotation;

/**
 * <p>
 * AnnotationInterceptor
 * </p>
 *
 * @author livk
 * @date 2022/10/6
 */
public interface AnnotationInterceptor<T extends Annotation> extends MethodInterceptor {

    @Nullable
    @Override
    default Object invoke(@Nonnull MethodInvocation invocation) throws Throwable {
        T annotation = null;
        Class<T> targetAnnotationClass = annotationClass();
        if (supportClass()) {
            Object obj = invocation.getThis();
            annotation = obj == null ? null : obj.getClass().getAnnotation(targetAnnotationClass);
        }
        if (supportMethod()) {
            T methodAnnotation = invocation.getMethod().getAnnotation(targetAnnotationClass);
            annotation = methodAnnotation == null ? annotation : methodAnnotation;
        }
        if (annotation != null) {
            return this.invoke(invocation, annotation);
        }
        return invocation.proceed();
    }

    Object invoke(@Nonnull MethodInvocation invocation, T annotation) throws Throwable;

    @SuppressWarnings("unchecked")
    default Class<T> annotationClass() {
        return (Class<T>) GenericTypeResolver.resolveTypeArgument(this.getClass(), AnnotationInterceptor.class);
    }

    default boolean supportMethod() {
        return true;
    }

    default boolean supportClass() {
        return false;
    }

    default PointcutAdvisor getAdvisor() {
        AnnotationMatchingPointcut pointcut;
        Class<T> targetAnnotationClass = annotationClass();
        if (supportClass() && supportMethod()) {
            pointcut = new AnnotationMatchingPointcut(targetAnnotationClass, targetAnnotationClass);
        } else if (supportClass()) {
            pointcut = AnnotationMatchingPointcut.forClassAnnotation(targetAnnotationClass);
        } else {
            pointcut = AnnotationMatchingPointcut.forMethodAnnotation(targetAnnotationClass);
        }
        return new DefaultPointcutAdvisor(pointcut, this);
    }
}
