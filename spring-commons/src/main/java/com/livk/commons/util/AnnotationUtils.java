package com.livk.commons.util;

import lombok.experimental.UtilityClass;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * <p>
 * AnnotationUtils
 * </p>
 *
 * @author livk
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
    public <A extends Annotation> A getAnnotationElement(MethodParameter methodParameter, Class<A> annotationClass) {
        A annotation = methodParameter.getMethodAnnotation(annotationClass);
        if (annotation == null) {
            Class<?> containingClass = methodParameter.getContainingClass();
            annotation = AnnotatedElementUtils.getMergedAnnotation(containingClass, annotationClass);
        }
        return annotation;
    }

    /**
     * 获取方法上或者类路径上的注解,方法级别优先,类路径允许复合注解
     *
     * @param method          method
     * @param annotationClass annotation
     * @param <A>             annotation泛型
     * @return annotation
     */
    public <A extends Annotation> A getAnnotationElement(Method method, Class<A> annotationClass) {
        A annotation = getAnnotation(method, annotationClass);
        if (annotation == null) {
            annotation = getAnnotation(method.getDeclaringClass(), annotationClass);
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
    public <A extends Annotation> boolean hasAnnotationElement(MethodParameter methodParameter, Class<A> annotationClass) {
        Class<?> containingClass = methodParameter.getContainingClass();
        return (AnnotatedElementUtils.hasAnnotation(containingClass, annotationClass) ||
                methodParameter.hasMethodAnnotation(annotationClass));
    }

    /**
     * 判断方法上或者类路径上是否包含注解,类路径允许复合注解
     *
     * @param method          method
     * @param annotationClass annotation
     * @param <A>             annotation泛型
     * @return bool
     */
    public <A extends Annotation> boolean hasAnnotationElement(Method method, Class<A> annotationClass) {
        return method.isAnnotationPresent(annotationClass) ||
               AnnotatedElementUtils.hasAnnotation(method.getDeclaringClass(), annotationClass);
    }
}
