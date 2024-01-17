/*
 * Copyright 2021-2024 spring-boot-extension the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.livk.core.limit.executor;

import com.livk.core.limit.LimitExecutor;
import com.livk.core.limit.exception.LimitException;
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
public class RedissonLimitExecutor extends ReentrantLimitExecutor implements LimitExecutor {

	private final RedissonClient redissonClient;

	@Override
	protected boolean reentrantTryAccess(String compositeKey, int rate, int rateInterval, TimeUnit rateIntervalUnit) {
		if (StringUtils.hasText(compositeKey)) {
			RRateLimiter rateLimiter = redissonClient.getRateLimiter(compositeKey);
			try {
				RateIntervalUnit unit = RateIntervalUnit.valueOf(rateIntervalUnit.name());
				rateLimiter.trySetRate(RateType.OVERALL, rate, rateInterval, unit);
				return rateLimiter.tryAcquire(1);
			}
			catch (Exception e) {
				throw new LimitException("un support TimeUnit " + rateIntervalUnit, e);
			}
		}
		throw new LimitException("Composite key is null or empty");
	}

}
