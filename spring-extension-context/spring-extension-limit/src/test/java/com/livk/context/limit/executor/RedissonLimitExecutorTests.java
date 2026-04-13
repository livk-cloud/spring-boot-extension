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

import com.livk.context.limit.exception.LimitException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RedissonClient;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

/**
 * @author livk
 */
class RedissonLimitExecutorTests {

	RedissonClient redissonClient;

	RedissonLimitExecutor executor;

	@BeforeEach
	void setUp() {
		redissonClient = mock(RedissonClient.class);
		executor = new RedissonLimitExecutor(redissonClient);
	}

	@Test
	void reentrantTryAccessReturnsTrueWhenAllowed() {
		RRateLimiter limiter = mock(RRateLimiter.class);
		given(redissonClient.getRateLimiter("testKey")).willReturn(limiter);
		given(limiter.trySetRate(any(), anyInt(), any())).willReturn(true);
		given(limiter.tryAcquire()).willReturn(true);

		boolean result = executor.reentrantTryAccess("testKey", 10, Duration.ofSeconds(60));
		assertThat(result).isTrue();
	}

	@Test
	void reentrantTryAccessReturnsFalseWhenDenied() {
		RRateLimiter limiter = mock(RRateLimiter.class);
		given(redissonClient.getRateLimiter("testKey")).willReturn(limiter);
		given(limiter.trySetRate(any(), anyInt(), any())).willReturn(true);
		given(limiter.tryAcquire()).willReturn(false);

		boolean result = executor.reentrantTryAccess("testKey", 10, Duration.ofSeconds(60));
		assertThat(result).isFalse();
	}

	@Test
	void reentrantTryAccessWithEmptyKeyThrows() {
		assertThatThrownBy(() -> executor.reentrantTryAccess("", 10, Duration.ofSeconds(60)))
			.isInstanceOf(LimitException.class);
	}

	@Test
	void reentrantTryAccessWithNullKeyThrows() {
		assertThatThrownBy(() -> executor.reentrantTryAccess(null, 10, Duration.ofSeconds(60)))
			.isInstanceOf(LimitException.class);
	}

	@Test
	void reentrantTryAccessCachesLimiterForSameKey() {
		RRateLimiter limiter = mock(RRateLimiter.class);
		given(redissonClient.getRateLimiter("cachedKey")).willReturn(limiter);
		given(limiter.trySetRate(any(), anyInt(), any())).willReturn(true);
		given(limiter.tryAcquire()).willReturn(true);

		executor.reentrantTryAccess("cachedKey", 10, Duration.ofSeconds(60));
		executor.reentrantTryAccess("cachedKey", 10, Duration.ofSeconds(60));

		// getRateLimiter should only be called once due to caching
		org.mockito.Mockito.verify(redissonClient, org.mockito.Mockito.times(1)).getRateLimiter("cachedKey");
	}

}
