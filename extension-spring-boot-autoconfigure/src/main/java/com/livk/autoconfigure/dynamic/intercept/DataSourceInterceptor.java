package com.livk.autoconfigure.dynamic.intercept;

import com.livk.autoconfigure.aop.AnnotationAbstractPointcutAdvisor;
import com.livk.autoconfigure.aop.AnnotationClassOrMethodPointcut;
import com.livk.autoconfigure.dynamic.annotation.DynamicSource;
import com.livk.autoconfigure.dynamic.datasource.DataSourceContextHolder;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.Pointcut;

/**
 * @author livk
 */
public class DataSourceInterceptor extends AnnotationAbstractPointcutAdvisor<DynamicSource> {

    @Override
    protected Object invoke(MethodInvocation invocation, DynamicSource dynamicSource) throws Throwable {
        if (dynamicSource != null) {
            DataSourceContextHolder.switchDataSource(dynamicSource.value());
        }
        Object proceed = invocation.proceed();
        DataSourceContextHolder.clear();
        return proceed;
    }

    @Override
    public Pointcut getPointcut() {
        return new AnnotationClassOrMethodPointcut(annotationType);
    }
}
