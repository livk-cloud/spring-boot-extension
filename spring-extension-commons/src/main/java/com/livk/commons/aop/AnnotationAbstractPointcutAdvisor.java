package com.livk.commons.aop;

import com.livk.commons.util.AnnotationUtils;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInvocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.aop.IntroductionInterceptor;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.core.GenericTypeResolver;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * 注解型切点处理器
 *
 * @param <A> 注解
 * @author livk
 */
public abstract class AnnotationAbstractPointcutAdvisor<A extends Annotation> extends AbstractPointcutAdvisor
        implements IntroductionInterceptor {

    /**
     * 切点注解类型
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
     * 执行拦截的方法
     *
     * @param invocation 方法相关信息
     * @param annotation 注解信息
     * @return 方法返回结果
     * @throws Throwable the throwable
     */
    protected abstract Object invoke(MethodInvocation invocation, A annotation) throws Throwable;

    @Override
    public boolean implementsInterface(Class<?> intf) {
        return annotationType != null && annotationType.isAssignableFrom(intf);
    }

    @Override
    public Advice getAdvice() {
        return this;
    }
}
