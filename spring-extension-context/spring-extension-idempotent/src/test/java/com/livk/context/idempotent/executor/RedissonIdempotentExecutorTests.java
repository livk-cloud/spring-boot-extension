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

package com.livk.context.idempotent.executor;

import com.livk.context.idempotent.exception.IdempotentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * @author livk
 */
class RedissonIdempotentExecutorTests {

	RedissonClient redissonClient;

	RedissonIdempotentExecutor executor;

	@BeforeEach
	void setUp() {
		redissonClient = mock(RedissonClient.class);
		executor = new RedissonIdempotentExecutor(redissonClient);
	}

	@Test
	void tryAcquireReturnsTrueWhenTokenIsFree() {
		RBucket<String> bucket = bucket("order");
		given(bucket.setIfAbsent(eq("1"), any(Duration.class))).willReturn(true);

		assertThat(executor.tryAcquire("order", Duration.ofSeconds(30))).isTrue();
		verify(bucket).setIfAbsent("1", Duration.ofSeconds(30));
	}

	@Test
	void tryAcquireReturnsFalseWhenTokenIsOccupied() {
		RBucket<String> bucket = bucket("order");
		given(bucket.setIfAbsent(eq("1"), any(Duration.class))).willReturn(false);

		assertThat(executor.tryAcquire("order", Duration.ofSeconds(30))).isFalse();
	}

	@Test
	void releaseDeletesTheToken() {
		RBucket<String> bucket = bucket("order");

		executor.release("order");

		verify(bucket).delete();
	}

	@Test
	void tryAcquireRejectsBlankKey() {
		assertThatThrownBy(() -> executor.tryAcquire(" ", Duration.ofSeconds(1)))
			.isInstanceOf(IdempotentException.class);
	}

	@Test
	void tryAcquireRejectsNonPositiveTtl() {
		assertThatThrownBy(() -> executor.tryAcquire("order", Duration.ZERO)).isInstanceOf(IdempotentException.class);
		assertThatThrownBy(() -> executor.tryAcquire("order", Duration.ofSeconds(-1)))
			.isInstanceOf(IdempotentException.class);
	}

	@SuppressWarnings("unchecked")
	private RBucket<String> bucket(String key) {
		RBucket<String> bucket = mock(RBucket.class);
		given(redissonClient.<String>getBucket(RedissonIdempotentExecutor.KEY_PREFIX + key)).willReturn(bucket);
		return bucket;
	}

}
