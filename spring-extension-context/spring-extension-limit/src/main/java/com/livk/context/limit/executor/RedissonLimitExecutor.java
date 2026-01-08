/*
 * Copyright 2021-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.livk.context.limit.executor;

import com.livk.context.limit.LimitExecutor;
import com.livk.context.limit.exception.LimitException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author livk Note:
 * <ul>
 * <li>{@code compositeKey} must be bounded and stable, otherwise the internal cache
 * maygrow indefinitely.</li>
 * <li>For the same {@code compositeKey}, {@code rate} and {@code rateInterval} mustremain
 * consistent.</li>
 * </ul>
 */
@Slf4j
@RequiredArgsConstructor
public class RedissonLimitExecutor extends WebRequestReentrantLimitExecutor implements LimitExecutor {

	private final RedissonClient redissonClient;

	private final Map<String, RRateLimiter> limiterCache = new ConcurrentHashMap<>();

	@Override
	protected boolean reentrantTryAccess(String compositeKey, int rate, Duration rateInterval) {
		if (!StringUtils.hasText(compositeKey)) {
			throw new LimitException("Composite key must not be null or empty");
		}
		RRateLimiter limiter = limiterCache.computeIfAbsent(compositeKey, redissonClient::getRateLimiter);
		// 幂等设置，失败也不影响后续 acquire
		if (!limiter.trySetRate(RateType.OVERALL, rate, rateInterval)) {
			log.debug("RateLimiter trySetRate failed, compositeKey: {}", compositeKey);
		}
		return limiter.tryAcquire();
	}

}
