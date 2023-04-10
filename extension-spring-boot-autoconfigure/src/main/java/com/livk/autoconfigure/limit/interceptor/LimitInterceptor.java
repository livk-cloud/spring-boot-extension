package com.livk.autoconfigure.limit.interceptor;

import com.livk.autoconfigure.limit.annotation.Limit;
import com.livk.autoconfigure.limit.exception.LimitException;
import com.livk.autoconfigure.limit.support.LimitExecutor;
import com.livk.commons.aop.AnnotationAbstractPointcutTypeAdvisor;
import com.livk.commons.spring.spel.SpringExpressionResolver;
import com.livk.commons.web.util.WebUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInvocation;

import java.util.concurrent.TimeUnit;

/**
 * The type Limit interceptor.
 *
 * @author livk
 */
@Slf4j
@RequiredArgsConstructor
public class LimitInterceptor extends AnnotationAbstractPointcutTypeAdvisor<Limit> {

    /**
     * SpEL表达式解析器
     */
    private final SpringExpressionResolver resolver = new SpringExpressionResolver();

    private final LimitExecutor limitExecutor;

    @Override
    protected Object invoke(MethodInvocation invocation, Limit limit) throws Throwable {
        String key = limit.key();
        int rate = limit.rate();
        int rateInterval = limit.rateInterval();
        TimeUnit rateIntervalUnit = limit.rateIntervalUnit();
        String spELKey = resolver.evaluate(key, invocation.getMethod(), invocation.getArguments());
        if (limit.restrictIp()) {
            String ip = WebUtils.realIp(WebUtils.request());
            spELKey = spELKey + "#" + ip;
        }
        boolean status = limitExecutor.tryAccess(spELKey, rate, rateInterval, rateIntervalUnit);
        if (status) {
            return invocation.proceed();
        } else {
            throw new LimitException("key=" + key + " is reach max limited access count=" + rate + " within period=" + rateInterval + " " + rateIntervalUnit.name());
        }
    }

}
