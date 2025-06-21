/*
 * Copyright 2021 spring-boot-extension the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.livk.context.sequence.builder;

import com.livk.context.sequence.SequenceRange;
import com.livk.context.sequence.support.RangeManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author livk
 */
class DefaultRangeSequenceTest {

	static RangeManager rangeManager = new TestRangeManager();

	@Test
	void nextValue() {
		assertEquals(1, new DefaultRangeSequence(rangeManager, "test").nextValue());
	}

	static class TestRangeManager implements RangeManager {

		@Override
		public SequenceRange nextRange(String name) {
			return new SequenceRange(1L, 10L);
		}

		@Override
		public void step(int step) {

		}

		@Override
		public void stepStart(long stepStart) {

		}

		@Override
		public void init() {

		}

	}

}
