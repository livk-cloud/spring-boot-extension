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

package com.livk.commons.util;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author livk
 */
class ApplierTests {

	@Test
	void ofApplyExecutesConsumerWithValue() {
		AtomicInteger result = new AtomicInteger();
		Applier.of(42).apply(result::set);
		assertThat(result.get()).isEqualTo(42);
	}

	@Test
	void ofApplyExecutesRunnableWhenValuePresent() {
		AtomicBoolean executed = new AtomicBoolean(false);
		Applier.of(10).apply(() -> executed.set(true));
		assertThat(executed.get()).isTrue();
	}

	@Test
	void gtRetainsValueWhenGreater() {
		AtomicInteger result = new AtomicInteger();
		Applier.of(20).gt(10).apply(result::set);
		assertThat(result.get()).isEqualTo(20);
	}

	@Test
	void gtDiscardsValueWhenEqual() {
		AtomicInteger result = new AtomicInteger(-1);
		Applier.of(10).gt(10).apply(result::set);
		assertThat(result.get()).isEqualTo(-1);
	}

	@Test
	void gtDiscardsValueWhenLess() {
		AtomicInteger result = new AtomicInteger(-1);
		Applier.of(5).gt(10).apply(result::set);
		assertThat(result.get()).isEqualTo(-1);
	}

	@Test
	void geRetainsValueWhenGreater() {
		AtomicInteger result = new AtomicInteger();
		Applier.of(20).ge(10).apply(result::set);
		assertThat(result.get()).isEqualTo(20);
	}

	@Test
	void geRetainsValueWhenEqual() {
		AtomicInteger result = new AtomicInteger();
		Applier.of(10).ge(10).apply(result::set);
		assertThat(result.get()).isEqualTo(10);
	}

	@Test
	void geDiscardsValueWhenLess() {
		AtomicInteger result = new AtomicInteger(-1);
		Applier.of(5).ge(10).apply(result::set);
		assertThat(result.get()).isEqualTo(-1);
	}

	@Test
	void ltRetainsValueWhenLess() {
		AtomicInteger result = new AtomicInteger();
		Applier.of(5).lt(10).apply(result::set);
		assertThat(result.get()).isEqualTo(5);
	}

	@Test
	void ltDiscardsValueWhenEqual() {
		AtomicInteger result = new AtomicInteger(-1);
		Applier.of(10).lt(10).apply(result::set);
		assertThat(result.get()).isEqualTo(-1);
	}

	@Test
	void ltDiscardsValueWhenGreater() {
		AtomicInteger result = new AtomicInteger(-1);
		Applier.of(20).lt(10).apply(result::set);
		assertThat(result.get()).isEqualTo(-1);
	}

	@Test
	void leRetainsValueWhenLess() {
		AtomicInteger result = new AtomicInteger();
		Applier.of(5).le(10).apply(result::set);
		assertThat(result.get()).isEqualTo(5);
	}

	@Test
	void leRetainsValueWhenEqual() {
		AtomicInteger result = new AtomicInteger();
		Applier.of(10).le(10).apply(result::set);
		assertThat(result.get()).isEqualTo(10);
	}

	@Test
	void leDiscardsValueWhenGreater() {
		AtomicInteger result = new AtomicInteger(-1);
		Applier.of(20).le(10).apply(result::set);
		assertThat(result.get()).isEqualTo(-1);
	}

	@Test
	void filterRetainsValueWhenPredicateMatches() {
		AtomicReference<String> result = new AtomicReference<>();
		Applier.of("hello").filter(s -> s.startsWith("h")).apply(result::set);
		assertThat(result.get()).isEqualTo("hello");
	}

	@Test
	void filterDiscardsValueWhenPredicateDoesNotMatch() {
		AtomicReference<String> result = new AtomicReference<>();
		Applier.of("hello").filter(s -> s.startsWith("x")).apply(result::set);
		assertThat(result.get()).isNull();
	}

	@Test
	void chainedFiltersWork() {
		AtomicInteger result = new AtomicInteger();
		Applier.of(75).ge(60).lt(90).apply(result::set);
		assertThat(result.get()).isEqualTo(75);
	}

	@Test
	void chainedFiltersDiscardWhenAnyFails() {
		AtomicInteger result = new AtomicInteger(-1);
		Applier.of(95).ge(60).lt(90).apply(result::set);
		assertThat(result.get()).isEqualTo(-1);
	}

	@Test
	void isTrueRetainsWhenBooleanTrue() {
		AtomicBoolean executed = new AtomicBoolean(false);
		Applier.of(Boolean.TRUE).isTrue().apply(v -> executed.set(true));
		assertThat(executed.get()).isTrue();
	}

	@Test
	void isTrueDiscardsWhenBooleanFalse() {
		AtomicBoolean executed = new AtomicBoolean(false);
		Applier.of(Boolean.FALSE).isTrue().apply(v -> executed.set(true));
		assertThat(executed.get()).isFalse();
	}

	@Test
	void applyConsumerDoesNotExecuteAfterDiscarded() {
		AtomicBoolean executed = new AtomicBoolean(false);
		Applier.of(5).gt(10).apply(v -> executed.set(true));
		assertThat(executed.get()).isFalse();
	}

	@Test
	void applyRunnableDoesNotExecuteAfterDiscarded() {
		AtomicBoolean executed = new AtomicBoolean(false);
		Applier.of(5).gt(10).apply(() -> executed.set(true));
		assertThat(executed.get()).isFalse();
	}

	@Test
	void applyConsumerThrowsOnNull() {
		assertThatThrownBy(() -> Applier.of(1).apply((Consumer<? super Integer>) null))
			.isInstanceOf(NullPointerException.class);
	}

	@Test
	void applyRunnableThrowsOnNull() {
		assertThatThrownBy(() -> Applier.of(1).apply((Runnable) null)).isInstanceOf(NullPointerException.class);
	}

	@Test
	void filterThrowsOnNullPredicate() {
		assertThatThrownBy(() -> Applier.of(1).filter(null)).isInstanceOf(NullPointerException.class);
	}

	@Test
	void gtThrowsOnNullValue() {
		assertThatThrownBy(() -> Applier.of(1).gt(null)).isInstanceOf(NullPointerException.class);
	}

	@Test
	void geThrowsOnNullValue() {
		assertThatThrownBy(() -> Applier.of(1).ge(null)).isInstanceOf(NullPointerException.class);
	}

	@Test
	void ltThrowsOnNullValue() {
		assertThatThrownBy(() -> Applier.of(1).lt(null)).isInstanceOf(NullPointerException.class);
	}

	@Test
	void leThrowsOnNullValue() {
		assertThatThrownBy(() -> Applier.of(1).le(null)).isInstanceOf(NullPointerException.class);
	}

	@Test
	void worksWithStringComparable() {
		AtomicReference<String> result = new AtomicReference<>();
		Applier.of("banana").gt("apple").apply(result::set);
		assertThat(result.get()).isEqualTo("banana");
	}

	@Test
	void worksWithStringComparableLessThan() {
		AtomicReference<String> result = new AtomicReference<>();
		Applier.of("apple").gt("banana").apply(result::set);
		assertThat(result.get()).isNull();
	}

	@Test
	void isTrueRunnableExecutesWhenTrue() {
		AtomicBoolean executed = new AtomicBoolean(false);
		Applier.of(Boolean.TRUE).isTrue().apply(() -> executed.set(true));
		assertThat(executed.get()).isTrue();
	}

	@Test
	void isTrueRunnableDoesNotExecuteWhenFalse() {
		AtomicBoolean executed = new AtomicBoolean(false);
		Applier.of(Boolean.FALSE).isTrue().apply(() -> executed.set(true));
		assertThat(executed.get()).isFalse();
	}

}
