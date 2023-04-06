package com.livk.commons.aop;

import org.springframework.aop.Pointcut;

import java.lang.annotation.Annotation;

/**
 * The type Annotation abstract pointcut type advisor.
 *
 * @param <A> the type parameter
 * @author livk
 */
public abstract class AnnotationAbstractPointcutTypeAdvisor<A extends Annotation> extends AnnotationAbstractPointcutAdvisor<A> {

    @Override
    public Pointcut getPointcut() {
        return AnnotationPointcutType.of(pointcutType(), annotationType);
    }

    /**
     * Pointcut type annotation pointcut type.
     *
     * @return the annotation pointcut type
     */
    protected AnnotationPointcutType pointcutType() {
        return AnnotationPointcutType.AUTO;
    }
}
