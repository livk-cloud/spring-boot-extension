package com.livk.redisson.limit;

import com.livk.commons.aop.AnnotationAbstractPointcutTypeAdvisor;
import com.livk.commons.aop.AnnotationPointcutType;
import com.livk.commons.web.util.WebUtils;
import lombok.RequiredArgsConstructor;
import org.aopalliance.intercept.MethodInvocation;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;

/**
 * <p>
 * LimitAspect
 * </p>
 *
 * @author livk
 */
//@Component
@RequiredArgsConstructor
public class LimitAspect extends AnnotationAbstractPointcutTypeAdvisor<Limiter> {

    private final RedissonClient redissonClient;

    @Override
    protected Object invoke(MethodInvocation invocation, Limiter limiter) throws Throwable {
        Assert.notNull(limiter, "limiter not null");
        RRateLimiter rateLimiter = redissonClient.getRateLimiter(limiter.key());
        rateLimiter.trySetRate(RateType.OVERALL, limiter.rate(), limiter.rateInterval(), limiter.rateIntervalUnit());
        if (rateLimiter.tryAcquire(limiter.requestedTokens())) {
            return invocation.proceed();
        } else {
            WebUtils.out(ResponseEntity.status(403).build());
            return null;
        }
    }

    @Override
    protected AnnotationPointcutType pointcutType() {
        return AnnotationPointcutType.METHOD;
    }
}
