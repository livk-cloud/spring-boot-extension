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

package com.livk.context.sequence.builder;

import com.livk.context.sequence.SequenceRange;
import com.livk.context.sequence.exception.SequenceException;
import com.livk.context.sequence.support.RangeManager;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author livk
 */
class DefaultRangeSequenceTests {

	@Test
	void nextValue() {
		DefaultRangeSequence sequence = new DefaultRangeSequence(new FixedRangeManager(), "test", 10, 0);
		for (int i = 0; i < 10; i++) {
			assertThat(sequence.nextValue()).isEqualTo(i + 1);
		}
	}

	@Test
	void nextValueRefreshesRangeAfterExhaustion() {
		AtomicInteger calls = new AtomicInteger();
		RangeManager manager = (name, step, stepStart) -> {
			int round = calls.getAndIncrement();
			long start = round * 10L + 1;
			return new SequenceRange(start, start + 9);
		};
		DefaultRangeSequence sequence = new DefaultRangeSequence(manager, "test", 10, 0);
		for (long expected = 1; expected <= 20; expected++) {
			assertThat(sequence.nextValue()).isEqualTo(expected);
		}
		assertThat(calls.get()).isEqualTo(2);
	}

	@Test
	void nextValueWrapsManagerFailure() {
		RangeManager failing = (name, step, stepStart) -> {
			throw new IllegalStateException("boom");
		};
		DefaultRangeSequence sequence = new DefaultRangeSequence(failing, "order", 10, 0);
		assertThatThrownBy(sequence::nextValue).isInstanceOf(SequenceException.class)
			.hasMessageContaining("order")
			.hasCauseInstanceOf(IllegalStateException.class);
	}

	@Test
	void constructorRejectsNullName() {
		assertThatThrownBy(() -> new DefaultRangeSequence(new FixedRangeManager(), null, 10, 0))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void nextValueRefreshesAlreadyExhaustedRange() {
		AtomicInteger calls = new AtomicInteger();
		RangeManager manager = (name, step, stepStart) -> {
			if (calls.getAndIncrement() == 0) {
				SequenceRange exhausted = new SequenceRange(1, 1);
				exhausted.next();
				exhausted.next();
				return exhausted;
			}
			return new SequenceRange(11, 12);
		};
		DefaultRangeSequence sequence = new DefaultRangeSequence(manager, "recover", 2, 0);
		assertThat(sequence.nextValue()).isEqualTo(11);
		assertThat(sequence.nextValue()).isEqualTo(12);
		assertThat(calls.get()).isGreaterThanOrEqualTo(2);
	}

	static class FixedRangeManager implements RangeManager {

		@Override
		public SequenceRange nextRange(String name, int step, long stepStart) {
			return new SequenceRange(1L, 10L);
		}

	}

}
