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
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author livk
 */
class AbstractRangeManagerTests {

	@Test
	void nextRangeRejectsBlankName() {
		StubRangeManager manager = new StubRangeManager(null);
		assertThatThrownBy(() -> manager.nextRange(" ", 10, 0)).isInstanceOf(SequenceException.class)
			.hasMessageContaining("name is empty");
		assertThatThrownBy(() -> manager.nextRange(null, 10, 0)).isInstanceOf(SequenceException.class)
			.hasMessageContaining("name is empty");
	}

	@Test
	void nextRangeWrapsNonSequenceException() {
		StubRangeManager manager = new StubRangeManager(new IllegalStateException("boom"));
		assertThatThrownBy(() -> manager.nextRange("biz", 10, 0)).isInstanceOf(SequenceException.class)
			.hasMessageContaining("buildNextRange error")
			.hasCauseInstanceOf(IllegalStateException.class);
	}

	@Test
	void nextRangeRethrowsSequenceException() {
		SequenceException original = new SequenceException("original");
		StubRangeManager manager = new StubRangeManager(original);
		assertThatThrownBy(() -> manager.nextRange("biz", 10, 0)).isSameAs(original);
	}

	@Test
	void nextRangeReturnsBuiltRange() {
		StubRangeManager manager = new StubRangeManager(null);
		SequenceRange range = manager.nextRange("biz", 10, 0);
		assertThat(range.getMin()).isEqualTo(1L);
		assertThat(range.getMax()).isEqualTo(10L);
	}

	static final class StubRangeManager extends AbstractRangeManager {

		private final Exception toThrow;

		StubRangeManager(Exception toThrow) {
			this.toThrow = toThrow;
		}

		@Override
		protected SequenceRange buildNextRange(String name, int step, long stepStart) {
			if (this.toThrow instanceof RuntimeException runtimeException) {
				throw runtimeException;
			}
			if (this.toThrow != null) {
				throw new RuntimeException(this.toThrow);
			}
			return new SequenceRange(1L, 10L);
		}

	}

}
