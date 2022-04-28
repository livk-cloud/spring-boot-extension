package com.livk.aspect;

import com.livk.annotation.DataSource;
import com.livk.datasource.DataSourceContextHolder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

/**
 * <p>
 * DataSourceAspect
 * </p>
 *
 * @author livk
 * @date 2022/3/23
 */
@Aspect
@Component
public class DataSourceAspect {

    @Around("@annotation(dataSource)||@within(dataSource)")
    public Object execute(ProceedingJoinPoint joinPoint, DataSource dataSource) throws Throwable {
        var signature = (MethodSignature) joinPoint.getSignature();
        var method = signature.getMethod();
        if (dataSource == null) {
            var methodAnnotation = AnnotationUtils.findAnnotation(method, DataSource.class);
            if (methodAnnotation != null) {
                dataSource = methodAnnotation;
            } else {
                dataSource = AnnotationUtils.findAnnotation(joinPoint.getTarget().getClass(), DataSource.class);
            }
        }
        if (dataSource != null) {
            DataSourceContextHolder.switchDataSource(dataSource.value());
        }
        var proceed = joinPoint.proceed();
        DataSourceContextHolder.clear();
        return proceed;
    }
}
