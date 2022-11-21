package com.livk.aspect;

import com.livk.annotation.DynamicSource;
import com.livk.datasource.DataSourceContextHolder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;

/**
 * <p>
 * DataSourceAspect
 * </p>
 *
 * @author livk
 * @date 2022/3/23
 */
@Aspect
public class DataSourceAspect {

    @Around("@annotation(dynamicSource)||@within(dynamicSource)")
    public Object execute(ProceedingJoinPoint joinPoint, DynamicSource dynamicSource) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        if (dynamicSource == null) {
            dynamicSource = AnnotationUtils.findAnnotation(method, DynamicSource.class);
            if (dynamicSource == null) {
                dynamicSource = AnnotationUtils.findAnnotation(joinPoint.getTarget().getClass(), DynamicSource.class);
            }
        }
        if (dynamicSource != null) {
            DataSourceContextHolder.switchDataSource(dynamicSource.value());
        }
        Object proceed = joinPoint.proceed();
        DataSourceContextHolder.clear();
        return proceed;
    }

}
