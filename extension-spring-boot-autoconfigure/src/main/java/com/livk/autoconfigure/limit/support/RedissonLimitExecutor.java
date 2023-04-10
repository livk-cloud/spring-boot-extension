package com.livk.autoconfigure.limit.support;

import com.livk.autoconfigure.limit.exception.LimitException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

/**
 * @author livk
 */
@Slf4j
@RequiredArgsConstructor
public class RedissonLimitExecutor implements LimitExecutor {

    private final RedissonClient redissonClient;

    @Override
    public boolean tryAccess(String compositeKey, int rate, int rateInterval, TimeUnit rateIntervalUnit) {
        if (StringUtils.hasText(compositeKey)) {
            RRateLimiter rateLimiter = redissonClient.getRateLimiter(compositeKey);
            try {
                RateIntervalUnit unit = RateIntervalUnit.valueOf(rateIntervalUnit.name());
                rateLimiter.trySetRate(RateType.OVERALL, rate, rateInterval, unit);
                return rateLimiter.tryAcquire(1);
            } catch (Exception e) {
                throw new LimitException("un support TimeUnit " + rateIntervalUnit, e);
            }
        }
        throw new LimitException("Composite key is null or empty");
    }

}
