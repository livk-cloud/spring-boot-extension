package com.livk.caffeine.aspect;

import com.livk.caffeine.annotation.DoubleCache;
import com.livk.commons.aop.AnnotationAbstractPointcutTypeAdvisor;
import com.livk.commons.spring.spel.SpringExpressionResolver;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.concurrent.Callable;

/**
 * <p>
 * CacheAspect
 * </p>
 *
 * @author livk
 */
@Component
public class CacheInterceptor extends AnnotationAbstractPointcutTypeAdvisor<DoubleCache> {

    private final Cache cache;

    private final SpringExpressionResolver resolver = new SpringExpressionResolver();

    public CacheInterceptor(CacheManager cacheManager) {
        this.cache = cacheManager.getCache("redis-caffeine");
    }

    @Override
    protected Object invoke(MethodInvocation invocation, DoubleCache doubleCache) throws Throwable {
        Assert.notNull(doubleCache, "doubleCache is null");
        String spELResult = resolver.evaluate(doubleCache.key(), invocation.getMethod(), invocation.getArguments());
        String realKey = doubleCache.cacheName() + ":" + spELResult;
        switch (doubleCache.type()) {
            case FULL -> {
                return cache.get(realKey, call(invocation.proceed()));
            }
            case PUT -> {
                Object proceed = invocation.proceed();
                cache.put(realKey, proceed);
                return proceed;
            }
            case DELETE -> {
                Object proceed = invocation.proceed();
                cache.evict(realKey);
                return proceed;
            }
        }
        return invocation.proceed();
    }

    private Callable<Object> call(Object obj) {
        return () -> obj;
    }
}
