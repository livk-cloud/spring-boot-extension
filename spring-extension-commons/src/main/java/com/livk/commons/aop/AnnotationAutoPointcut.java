package com.livk.commons.aop;

import org.springframework.aop.Pointcut;

import java.lang.annotation.Annotation;

/**
 * The interface Annotation auto pointcut.
 *
 * @author livk
 */
@FunctionalInterface
interface AnnotationAutoPointcut {

    /**
     * 根据注解获取到切点
     *
     * @param annotationType 注解类信息
     * @return 切点
     */
    Pointcut getPointcut(Class<? extends Annotation> annotationType);
}
