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
import com.livk.context.sequence.support.RangeManager;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
class DefaultSequenceBuilderUnitTests {

	@Test
	void ignoresNonPositiveStep() {
		AtomicInteger observedStep = new AtomicInteger();
		RangeManager manager = (name, step, stepStart) -> {
			observedStep.set(step);
			return new SequenceRange(1, Math.max(step, 1));
		};
		SequenceBuilder.builder(manager).bizName("order").step(0).step(-3).build().nextValue();
		assertThat(observedStep.get()).isEqualTo(1000);
	}

	@Test
	void ignoresNegativeStepStart() {
		AtomicLong observedStart = new AtomicLong(-1);
		RangeManager manager = (name, step, stepStart) -> {
			observedStart.set(stepStart);
			return new SequenceRange(stepStart + 1, stepStart + step);
		};
		SequenceBuilder.builder(manager).bizName("order").stepStart(-1).build().nextValue();
		assertThat(observedStart.get()).isZero();
	}

	@Test
	void appliesValidStepAndStart() {
		AtomicInteger observedStep = new AtomicInteger();
		AtomicLong observedStart = new AtomicLong();
		RangeManager manager = (name, step, stepStart) -> {
			observedStep.set(step);
			observedStart.set(stepStart);
			return new SequenceRange(stepStart + 1, stepStart + step);
		};
		SequenceBuilder.builder(manager).bizName("order").step(20).stepStart(5).build().nextValue();
		assertThat(observedStep.get()).isEqualTo(20);
		assertThat(observedStart.get()).isEqualTo(5);
	}

}
