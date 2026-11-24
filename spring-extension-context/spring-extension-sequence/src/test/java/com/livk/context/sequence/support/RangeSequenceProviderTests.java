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

import com.livk.context.sequence.Sequence;
import com.livk.context.sequence.SequenceRange;
import com.livk.context.sequence.exception.SequenceException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author livk
 */
class RangeSequenceProviderTests {

	@Test
	void cachesSequencePerBizName() {
		RangeManager manager = mock(RangeManager.class);
		when(manager.nextRange("order", 10, 0L)).thenReturn(new SequenceRange(1, 2));
		when(manager.nextRange("user", 10, 0L)).thenReturn(new SequenceRange(5, 6));
		RangeSequenceProvider provider = new RangeSequenceProvider(manager, 10, 0);

		Sequence order = provider.get("order");
		assertThat(order).isSameAs(provider.get("order"));
		assertThat(order.nextValue()).isEqualTo(1);
		assertThat(provider.get("user").nextValue()).isEqualTo(5);
		verify(manager, times(1)).nextRange("order", 10, 0L);
	}

	@Test
	void rejectsBlankBizName() {
		RangeSequenceProvider provider = new RangeSequenceProvider(mock(RangeManager.class), 10, 0);
		assertThatThrownBy(() -> provider.get(" ")).isInstanceOf(SequenceException.class);
	}

}
