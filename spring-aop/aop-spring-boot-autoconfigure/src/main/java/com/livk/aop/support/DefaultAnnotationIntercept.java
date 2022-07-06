package com.livk.aop.support;

import com.livk.aop.intercept.AnnotationIntercept;
import lombok.RequiredArgsConstructor;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

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
class DefaultAnnotationIntercept implements MethodInterceptor {

    private final AnnotationIntercept annotationIntercept;

    @Nullable
    @Override
    public Object invoke(@NonNull MethodInvocation invocation) throws Throwable {
        Method method = invocation.getMethod();
        Annotation annotation = method.getAnnotation(annotationIntercept.type());
        if (annotation == null) {
            annotation = AnnotationUtils.findAnnotation(method, annotationIntercept.type());
        }
        return annotationIntercept.invoke(invocation, AnnotationInvoke.of(annotation));
    }
}
