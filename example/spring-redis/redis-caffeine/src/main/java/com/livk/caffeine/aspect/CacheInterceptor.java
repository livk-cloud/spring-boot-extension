package com.livk.caffeine.aspect;

import com.livk.caffeine.annotation.DoubleCache;
import com.livk.caffeine.handler.CacheHandlerAdapter;
import com.livk.commons.aop.AnnotationAbstractPointcutTypeAdvisor;
import com.livk.commons.spring.spel.SpringExpressionResolver;
import lombok.RequiredArgsConstructor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * <p>
 * CacheAspect
 * </p>
 *
 * @author livk
 */
@Component
@RequiredArgsConstructor
public class CacheInterceptor extends AnnotationAbstractPointcutTypeAdvisor<DoubleCache> {

    private final CacheHandlerAdapter adapter;

    private final SpringExpressionResolver resolver = new SpringExpressionResolver();

    @Override
    protected Object invoke(MethodInvocation invocation, DoubleCache doubleCache) throws Throwable {
        Assert.notNull(doubleCache, "doubleCache is null");
        String spELResult = resolver.evaluate(doubleCache.key(), invocation.getMethod(), invocation.getArguments());
        String realKey = doubleCache.cacheName() + ":" + spELResult;
        switch (doubleCache.type()) {
            case FULL -> {
                return adapter.readAndPut(realKey, invocation.proceed(), doubleCache.timeOut());
            }
            case PUT -> {
                Object proceed = invocation.proceed();
                adapter.put(realKey, proceed, doubleCache.timeOut());
                return proceed;
            }
            case DELETE -> {
                Object proceed = invocation.proceed();
                adapter.delete(realKey);
                return proceed;
            }
        }
        return invocation.proceed();
    }
}
