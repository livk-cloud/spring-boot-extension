/*
 * Copyright 2021-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * you may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
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

/**
 * @author livk
 */
@Slf4j
@RequiredArgsConstructor
public class RedissonLimitExecutor extends ReentrantLimitExecutor implements LimitExecutor {

	private final RedissonClient redissonClient;

	@Override
	protected boolean reentrantTryAccess(String compositeKey, int rate, Duration rateInterval) {
		if (StringUtils.hasText(compositeKey)) {
			RRateLimiter limiter = redissonClient.getRateLimiter(compositeKey);
			limiter.trySetRate(RateType.OVERALL, rate, rateInterval);
			return limiter.tryAcquire(1);
		}
		throw new LimitException("Composite key is null or empty");
	}

}
