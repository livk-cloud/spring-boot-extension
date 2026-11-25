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

import com.livk.context.sequence.Sequence;
import com.livk.context.sequence.SequenceRange;
import com.livk.context.sequence.support.RangeManager;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
class DefaultSequenceBuilderUnitTests {

	private final RangeManager manager = (name, step, stepStart) -> new SequenceRange(1, step);

	@Test
	void ignoresNonPositiveStep() throws Exception {
		Sequence sequence = SequenceBuilder.builder(this.manager).bizName("order").step(0).step(-3).build();
		assertThat(readInt(sequence, "step")).isEqualTo(1000);
	}

	@Test
	void ignoresNegativeStepStart() throws Exception {
		Sequence sequence = SequenceBuilder.builder(this.manager).bizName("order").stepStart(-1).build();
		assertThat(readLong(sequence, "stepStart")).isZero();
	}

	@Test
	void appliesValidStepAndStart() throws Exception {
		Sequence sequence = SequenceBuilder.builder(this.manager).bizName("order").step(20).stepStart(5).build();
		assertThat(readInt(sequence, "step")).isEqualTo(20);
		assertThat(readLong(sequence, "stepStart")).isEqualTo(5);
	}

	private static int readInt(Sequence sequence, String field) throws Exception {
		Field f = sequence.getClass().getDeclaredField(field);
		f.setAccessible(true);
		return f.getInt(sequence);
	}

	private static long readLong(Sequence sequence, String field) throws Exception {
		Field f = sequence.getClass().getDeclaredField(field);
		f.setAccessible(true);
		return f.getLong(sequence);
	}

}
