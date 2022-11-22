package com.livk.util;

import lombok.experimental.UtilityClass;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.lang.annotation.Annotation;

/**
 * <p>
 * AnnotationUtils
 * </p>
 *
 * @author livk
 * @date 2022/11/4
 */
@UtilityClass
public class AnnotationUtils extends org.springframework.core.annotation.AnnotationUtils {

    /**
     * 获取方法上或者类路径上的注解,方法级别优先,类路径允许复合注解
     *
     * @param methodParameter parameter
     * @param annotationClass annotation
     * @param <A>             annotation泛型
     * @return annotation
     */
    public <A extends Annotation> A getAnnotation(MethodParameter methodParameter, Class<A> annotationClass) {
        A annotation = methodParameter.getMethodAnnotation(annotationClass);
        if (annotation == null) {
            Class<?> containingClass = methodParameter.getContainingClass();
            annotation = AnnotatedElementUtils.getMergedAnnotation(containingClass, annotationClass);
        }
        return annotation;
    }

    /**
     * 判断方法上或者类路径上是否包含注解,类路径允许复合注解
     *
     * @param methodParameter parameter
     * @param annotationClass annotation
     * @param <A>             annotation泛型
     * @return bool
     */
    public <A extends Annotation> boolean hasAnnotation(MethodParameter methodParameter, Class<A> annotationClass) {
        Class<?> containingClass = methodParameter.getContainingClass();
        return (AnnotatedElementUtils.hasAnnotation(containingClass, annotationClass) ||
                methodParameter.hasMethodAnnotation(annotationClass));
    }
}
