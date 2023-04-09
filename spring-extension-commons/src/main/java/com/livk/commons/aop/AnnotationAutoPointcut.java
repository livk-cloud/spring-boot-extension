package com.livk.commons.aop;

import org.springframework.aop.Pointcut;

import java.lang.annotation.Annotation;

/**
 * @author livk
 */
interface AnnotationAutoPointcut {

    Pointcut getPointcut(Class<? extends Annotation> annotationType);
}
