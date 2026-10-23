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

package com.livk.commons.wrapper;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

/**
 * @author livk
 */
class MutableWrapperTests {

	@Test
	void mutableWithValueIsInitializedAndReturnsValue() {
		MutableWrapper<String> wrapper = MutableWrapper.mutable("livk");
		assertThat(wrapper.isInitialized()).isTrue();
		assertThat(wrapper.unwrap()).isEqualTo("livk");
	}

	@Test
	void mutableWithValueRejectsNull() {
		assertThatIllegalArgumentException().isThrownBy(() -> MutableWrapper.mutable((String) null))
			.withMessageContaining("not be null");
	}

	@Test
	void mutableWithValueAllowsMultipleSets() {
		MutableWrapper<String> wrapper = MutableWrapper.mutable("first");
		wrapper.set("second");
		assertThat(wrapper.isInitialized()).isTrue();
		assertThat(wrapper.unwrap()).isEqualTo("second");
	}

	@Test
	void mutableWithMultipleModeStartsUninitialized() {
		MutableWrapper<String> wrapper = MutableWrapper.mutable(MutableWrapper.Mode.MULTIPLE);
		assertThat(wrapper.isInitialized()).isFalse();
		assertThat(wrapper.unwrap()).isNull();
	}

	@Test
	void mutableWithMultipleModeAllowsRepeatedWrites() {
		MutableWrapper<String> wrapper = MutableWrapper.mutable(MutableWrapper.Mode.MULTIPLE);
		wrapper.set("first");
		assertThat(wrapper.isInitialized()).isTrue();
		assertThat(wrapper.unwrap()).isEqualTo("first");

		wrapper.set("second");
		assertThat(wrapper.isInitialized()).isTrue();
		assertThat(wrapper.unwrap()).isEqualTo("second");
	}

	@Test
	void mutableWithOnceModeAllowsOnlyFirstWrite() {
		MutableWrapper<String> wrapper = MutableWrapper.mutable(MutableWrapper.Mode.ONCE);
		assertThat(wrapper.isInitialized()).isFalse();

		wrapper.set("first");
		assertThat(wrapper.isInitialized()).isTrue();
		assertThat(wrapper.unwrap()).isEqualTo("first");

		wrapper.set("second");
		assertThat(wrapper.isInitialized()).isTrue();
		assertThat(wrapper.unwrap()).isEqualTo("first");
	}

	@Test
	void setRejectsNullValue() {
		MutableWrapper<String> wrapper = MutableWrapper.mutable(MutableWrapper.Mode.MULTIPLE);
		assertThatIllegalArgumentException().isThrownBy(() -> wrapper.set(null)).withMessageContaining("not be null");
		assertThat(wrapper.isInitialized()).isFalse();
		assertThat(wrapper.unwrap()).isNull();
	}

	@Test
	void setRejectsNullValueWhenAlreadyInitialized() {
		MutableWrapper<String> wrapper = MutableWrapper.mutable("livk");
		assertThatIllegalArgumentException().isThrownBy(() -> wrapper.set(null)).withMessageContaining("not be null");
		assertThat(wrapper.unwrap()).isEqualTo("livk");
	}

}
