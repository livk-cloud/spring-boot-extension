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

package com.livk.context.sequence.support;

import com.livk.context.sequence.SequenceRange;
import com.livk.context.sequence.exception.SequenceException;
import com.livk.context.sequence.support.redis.SequenceRedisHelper;
import org.junit.jupiter.api.Test;

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
class RedisRangeManagerUnitTests {

	@Test
	void buildsRangeFromIncrement() {
		SequenceRedisHelper helper = mock(SequenceRedisHelper.class);
		given(helper.incrBy(any(), eq(10))).willReturn(110L);
		RedisRangeManager manager = new RedisRangeManager(helper);

		SequenceRange range = manager.nextRange("order", 10, 100);

		assertThat(range.getMin()).isEqualTo(101);
		assertThat(range.getMax()).isEqualTo(110);
		verify(helper).setNx(any(), eq(100L));
	}

	@Test
	void throwsWhenIncrementReturnsNull() {
		SequenceRedisHelper helper = mock(SequenceRedisHelper.class);
		given(helper.incrBy(any(), eq(5))).willReturn(null);
		RedisRangeManager manager = new RedisRangeManager(helper);

		assertThatThrownBy(() -> manager.nextRange("order", 5, 0)).isInstanceOf(SequenceException.class)
			.hasMessageContaining("order");
	}

}
