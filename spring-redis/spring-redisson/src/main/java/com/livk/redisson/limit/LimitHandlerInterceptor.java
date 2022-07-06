package com.livk.redisson.limit;

import com.livk.support.SpringContextHolder;
import com.livk.util.ResponseUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * <p>
 * LimitHandlerInterceptor
 * </p>
 *
 * @author livk
 * @date 2022/7/5
 */
public class LimitHandlerInterceptor implements HandlerInterceptor {

    private volatile RedissonClient redissonClient;

    private void init() {
        if (redissonClient == null) {
            synchronized (this) {
                if (redissonClient == null) {
                    redissonClient = SpringContextHolder.getBean(RedissonClient.class);
                }
            }
        }
    }


    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        init();
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Limiter limiter = handlerMethod.getMethodAnnotation(Limiter.class);
        if (limiter == null) {
            limiter = AnnotationCacheUtil.findAnnotation(handlerMethod.getMethod(), Limiter.class);
        }
        if (limiter != null) {
            RRateLimiter rateLimiter = redissonClient.getRateLimiter(limiter.key());
            rateLimiter.trySetRate(RateType.OVERALL, limiter.rate(), limiter.rateInterval(), limiter.rateIntervalUnit());
            if (rateLimiter.tryAcquire(limiter.requestedTokens())) {
                return true;
            } else {
                ResponseUtils.out(response, ResponseEntity.status(403).build());
                return false;
            }
        }
        return true;
    }
}
