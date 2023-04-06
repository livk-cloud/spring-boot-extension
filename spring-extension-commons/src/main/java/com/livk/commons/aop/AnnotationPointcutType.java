package com.livk.commons.aop;

import com.google.common.collect.Sets;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.util.HashSet;

/**
 * The enum Pointcut type.
 *
 * @author livk
 */
public enum AnnotationPointcutType {

    /**
     * Type annotation pointcut type.
     */
    TYPE,

    /**
     * Method annotation pointcut type.
     */
    METHOD,

    /**
     * Type and method annotation pointcut type.
     */
    TYPE_AND_METHOD,

    /**
     * Type or method annotation pointcut type.
     */
    TYPE_OR_METHOD,

    /**
     * Auto annotation pointcut type.
     */
    AUTO;


    /**
     * Of pointcut.
     *
     * @param type       the type
     * @param targetType the target type
     * @return the pointcut
     */
    public static Pointcut of(AnnotationPointcutType type, Class<? extends Annotation> targetType) {
        return switch (type) {
            case TYPE -> AnnotationMatchingPointcut.forClassAnnotation(targetType);
            case METHOD -> AnnotationMatchingPointcut.forMethodAnnotation(targetType);
            case TYPE_AND_METHOD -> new AnnotationMatchingPointcut(targetType, targetType);
            case TYPE_OR_METHOD -> new AnnotationClassOrMethodPointcut(targetType);
            case AUTO -> {
                Target target = targetType.getAnnotation(Target.class);
                HashSet<ElementType> elementTypeHashSet = Sets.newHashSet(target.value());
                if (elementTypeHashSet.contains(ElementType.TYPE) && elementTypeHashSet.contains(ElementType.METHOD)) {
                    yield of(TYPE_OR_METHOD, targetType);
                } else if (elementTypeHashSet.contains(ElementType.TYPE)) {
                    yield of(TYPE, targetType);
                } else if (elementTypeHashSet.contains(ElementType.METHOD)) {
                    yield of(METHOD, targetType);
                } else {
                    throw new IllegalArgumentException("annotation:" + targetType + "Missing " + Target.class + " TYPE or METHOD information");
                }
            }
        };
    }
}
