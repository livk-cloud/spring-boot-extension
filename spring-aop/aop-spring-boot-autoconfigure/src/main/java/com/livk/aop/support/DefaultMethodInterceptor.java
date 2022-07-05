package com.livk.aop.support;

import com.livk.aop.intercept.AnnotationIntercept;
import lombok.RequiredArgsConstructor;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * <p>
 * DefaultMethodInterceptor
 * </p>
 *
 * @author livk
 * @date 2022/7/5
 */
@RequiredArgsConstructor
class DefaultMethodInterceptor implements MethodInterceptor {

    private final AnnotationIntercept annotationIntercept;

    @Nullable
    @Override
    public Object invoke(@NotNull MethodInvocation invocation) throws Throwable {
        Method method = invocation.getMethod();
        Annotation annotation = method.getAnnotation(annotationIntercept.getType());
        if (annotation == null) {
            annotation = AnnotationUtils.findAnnotation(method, annotationIntercept.getType());
        }
        return annotationIntercept.invoke(invocation, AnnotationInvoke.of(annotation));
    }
}
