package com.livk.autoconfigure.limit.interceptor;

import com.livk.autoconfigure.limit.annotation.Limit;
import com.livk.autoconfigure.limit.exception.LimitException;
import com.livk.autoconfigure.limit.support.LimitExecutor;
import com.livk.autoconfigure.limit.support.LimitSupport;
import com.livk.commons.aop.AnnotationAbstractPointcutTypeAdvisor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.ObjectProvider;

/**
 * The type Limit interceptor.
 *
 * @author livk
 */
@Slf4j
@RequiredArgsConstructor
public class LimitInterceptor extends AnnotationAbstractPointcutTypeAdvisor<Limit> {

    /**
     * 执行器
     */
    private final ObjectProvider<LimitExecutor> provider;

    @Override
    protected Object invoke(MethodInvocation invocation, Limit limit) throws Throwable {
        LimitSupport limitSupport = LimitSupport.of(provider.getIfAvailable());
        boolean status = limitSupport.exec(limit, invocation.getMethod(), invocation.getArguments());
        if (status) {
            return invocation.proceed();
        } else {
            throw new LimitException("key=" + limit.key() + " is reach max limited access count=" + limit.rate() +
                                     " within period=" + limit.rateInterval() + " " + limit.rateIntervalUnit().name());
        }
    }

}
