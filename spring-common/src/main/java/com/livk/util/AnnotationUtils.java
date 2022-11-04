package com.livk.util;

import lombok.experimental.UtilityClass;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.web.reactive.HandlerResult;

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

    public <A extends Annotation> A getAnnotation(MethodParameter returnType, Class<A> annotationClass) {
        A annotation = returnType.getMethodAnnotation(annotationClass);
        if (annotation == null) {
            Class<?> containingClass = returnType.getContainingClass();
            annotation = AnnotatedElementUtils.getMergedAnnotation(containingClass, annotationClass);
        }
        return annotation;
    }

    public <A extends Annotation> A getAnnotation(HandlerResult result, Class<A> annotationClass) {
        MethodParameter returnType = result.getReturnTypeSource();
        return getAnnotation(returnType, annotationClass);
    }

    public <A extends Annotation> boolean hasAnnotation(MethodParameter returnType, Class<A> annotationClass) {
        Class<?> containingClass = returnType.getContainingClass();
        return (AnnotatedElementUtils.hasAnnotation(containingClass, annotationClass) ||
                returnType.hasMethodAnnotation(annotationClass));
    }

    public <A extends Annotation> boolean hasAnnotation(HandlerResult result, Class<A> annotationClass) {
        MethodParameter returnType = result.getReturnTypeSource();
        return hasAnnotation(returnType, annotationClass);
    }
}
