package com.livk.autoconfigure.dynamic.intercept;

import com.livk.autoconfigure.dynamic.annotation.DynamicSource;
import com.livk.autoconfigure.dynamic.datasource.DataSourceContextHolder;
import com.livk.commons.aop.AnnotationAbstractPointcutTypeAdvisor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * @author livk
 */
public class DataSourceInterceptor extends AnnotationAbstractPointcutTypeAdvisor<DynamicSource> {

    @Override
    protected Object invoke(MethodInvocation invocation, DynamicSource dynamicSource) throws Throwable {
        if (dynamicSource != null) {
            DataSourceContextHolder.switchDataSource(dynamicSource.value());
        }
        Object proceed = invocation.proceed();
        DataSourceContextHolder.clear();
        return proceed;
    }
}
