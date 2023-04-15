package com.livk.commons.aop;

import org.springframework.aop.Pointcut;

import java.lang.annotation.Annotation;

/**
 * 使用{@see AnnotationPointcutType}的注解型切点处理器
 *
 * @param <A> the type parameter
 * @author livk
 * @see AnnotationPointcutType
 * @see AnnotationAbstractPointcutAdvisor
 */
public abstract class AnnotationAbstractPointcutTypeAdvisor<A extends Annotation> extends AnnotationAbstractPointcutAdvisor<A> {

    @Override
    public Pointcut getPointcut() {
        return pointcutType().getPointcut(annotationType);
    }

    /**
     * <p>用于指定不同的切点类型，默认为{@link AnnotationPointcutType#AUTO}</p>
     *
     * @return the annotation pointcut type
     */
    protected AnnotationPointcutType pointcutType() {
        return AnnotationPointcutType.AUTO;
    }
}
