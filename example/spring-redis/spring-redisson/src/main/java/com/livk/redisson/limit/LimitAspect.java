package com.livk.redisson.limit;

import com.livk.commons.web.util.WebUtils;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.lang.reflect.Method;

/**
 * <p>
 * LimitAspect
 * </p>
 *
 * @author livk
 */
//@Aspect
@Component
@RequiredArgsConstructor
public class LimitAspect {

    private final RedissonClient redissonClient;

    @Around("@annotation(limiter)")
    public Object around(ProceedingJoinPoint joinPoint, Limiter limiter) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        if (limiter == null) {
            limiter = AnnotationUtils.findAnnotation(method, Limiter.class);
        }
        Assert.notNull(limiter, "limiter not null");
        RRateLimiter rateLimiter = redissonClient.getRateLimiter(limiter.key());
        rateLimiter.trySetRate(RateType.OVERALL, limiter.rate(), limiter.rateInterval(), limiter.rateIntervalUnit());
        if (rateLimiter.tryAcquire(limiter.requestedTokens())) {
            return joinPoint.proceed();
        } else {
            WebUtils.out(ResponseEntity.status(403).build());
            return null;
        }
    }

}
