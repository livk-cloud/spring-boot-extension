package com.livk.autoconfigure.aop;

import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInvocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.aop.IntroductionInterceptor;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.core.GenericTypeResolver;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * The type Annotation abstract pointcut advisor.
 *
 * @param <A> the type parameter
 * @author livk
 */
public abstract class AnnotationAbstractPointcutAdvisor<A extends Annotation> extends AbstractPointcutAdvisor
        implements IntroductionInterceptor {

    /**
     * The Annotation type.
     */
    @SuppressWarnings("unchecked")
    protected final Class<A> annotationType = (Class<A>) GenericTypeResolver.resolveTypeArgument(this.getClass(), AnnotationAbstractPointcutAdvisor.class);


    @Nullable
    @Override
    public Object invoke(@NotNull MethodInvocation invocation) throws Throwable {
        Method method = invocation.getMethod();
        A annotation = AnnotationUtils.findAnnotation(method, annotationType);
        if (annotation == null && invocation.getThis() != null) {
            annotation = AnnotationUtils.findAnnotation(invocation.getThis().getClass(), annotationType);
        }
        return invoke(invocation, annotation);
    }

    /**
     * Invoke object.
     *
     * @param invocation the invocation
     * @param annotation the annotation
     * @return the object
     * @throws Throwable the throwable
     */
    protected abstract Object invoke(MethodInvocation invocation, A annotation) throws Throwable;

    @Override
    public boolean implementsInterface(Class<?> intf) {
        return annotationType.isAssignableFrom(intf);
    }

    @Override
    public Advice getAdvice() {
        return this;
    }
}
