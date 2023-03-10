package com.livk.redisson.limit;

import com.livk.commons.web.util.WebUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

/**
 * <p>
 * LimitHandlerInterceptor
 * </p>
 *
 * @author livk
 */
@RequiredArgsConstructor
public class LimitHandlerInterceptor implements HandlerInterceptor {

    private final RedissonClient redissonClient;


    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Limiter limiter = handlerMethod.getMethodAnnotation(Limiter.class);
        if (limiter == null) {
            limiter = AnnotationCacheUtil.findAnnotation(handlerMethod.getMethod(), Limiter.class);
        }
        if (limiter != null) {
            RRateLimiter rateLimiter = redissonClient.getRateLimiter(limiter.key());
            rateLimiter.trySetRate(RateType.OVERALL, limiter.rate(), limiter.rateInterval(), limiter.rateIntervalUnit());
            rateLimiter.expireIfNotSet(Duration.of(2, ChronoUnit.MINUTES));
            if (rateLimiter.tryAcquire(limiter.requestedTokens())) {
                return true;
            } else {
                WebUtils.out(response, ResponseEntity.status(403).build());
                return false;
            }
        }
        return true;
    }
}
