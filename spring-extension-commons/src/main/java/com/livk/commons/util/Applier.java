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

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * A fluent conditional applier for {@link Comparable} values.
 * <p>
 * {@code Applier} is similar to {@link java.util.Optional}, but focuses on
 * comparison-based filtering before executing a consumer action. Supports chaining via
 * greater-than, less-than, and other comparison operations, ultimately applying an action
 * through {@link #apply}.
 * <p>
 * Usage examples: <pre>{@code
 * // Execute action when age > 18
 * Applier.of(age).gt(18).apply(v -> System.out.println("Adult: " + v));
 *
 * // Chained filtering
 * Applier.of(score).ge(60).lt(90).apply(v -> System.out.println("Good: " + v));
 *
 * // Custom filter predicate
 * Applier.of(name).filter(n -> n.startsWith("A")).apply(System.out::println);
 * }</pre>
 *
 * @param <T> the type of value, must implement {@link Comparable}
 * @author livk
 * @see java.util.Optional
 * @see Comparable
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Applier<T extends Comparable<T>> {

	/**
	 * The target value held by this applier, may be {@code null} when the condition is
	 * not met.
	 */
	private final @Nullable T target;

	/**
	 * Create an empty {@code Applier} instance indicating the condition is not satisfied.
	 * @param <T> the type of value
	 * @return an empty {@code Applier} instance
	 */
	private static <T extends Comparable<T>> Applier<T> empty() {
		return new Applier<>(null);
	}

	/**
	 * Create an {@code Applier} instance with the given value.
	 * @param <T> the type of value
	 * @param target the target value, must not be {@code null}
	 * @return an {@code Applier} instance containing the target value
	 */
	public static <T extends Comparable<T>> Applier<T> of(T target) {
		return new Applier<>(target);
	}

	/**
	 * Filter using a custom predicate.
	 * <p>
	 * If the current value is {@code null} or the predicate test fails, an empty
	 * {@code Applier} is returned.
	 * @param predicate the filter condition, must not be {@code null}
	 * @return this instance if the condition is satisfied, otherwise an empty instance
	 * @throws NullPointerException if predicate is {@code null}
	 */
	public Applier<T> filter(Predicate<? super T> predicate) {
		Objects.requireNonNull(predicate);
		if (this.target == null) {
			return this;
		}
		else {
			return predicate.test(this.target) ? this : empty();
		}
	}

	/**
	 * Check whether the current value is strictly greater than the given value.
	 * <p>
	 * Equivalent to {@code target.compareTo(value) > 0}.
	 * @param value the baseline value, must not be {@code null}
	 * @return this instance if the current value is greater, otherwise an empty instance
	 * @throws NullPointerException if value is {@code null}
	 */
	public Applier<T> gt(T value) {
		Objects.requireNonNull(value);
		if (this.target == null || this.target.compareTo(value) <= 0) {
			return empty();
		}
		return this;
	}

	/**
	 * Check whether the current value is greater than or equal to the given value.
	 * <p>
	 * Equivalent to {@code target.compareTo(value) >= 0}.
	 * @param value the baseline value, must not be {@code null}
	 * @return this instance if the current value is greater or equal, otherwise an empty
	 * instance
	 * @throws NullPointerException if value is {@code null}
	 */
	public Applier<T> ge(T value) {
		Objects.requireNonNull(value);
		if (this.target == null || this.target.compareTo(value) < 0) {
			return empty();
		}
		return this;
	}

	/**
	 * Check whether the current value is strictly less than the given value.
	 * <p>
	 * Equivalent to {@code target.compareTo(value) < 0}.
	 * @param value the baseline value, must not be {@code null}
	 * @return this instance if the current value is less, otherwise an empty instance
	 * @throws NullPointerException if value is {@code null}
	 */
	public Applier<T> lt(T value) {
		Objects.requireNonNull(value);
		if (this.target == null || this.target.compareTo(value) >= 0) {
			return empty();
		}
		return this;
	}

	/**
	 * Check whether the current value is less than or equal to the given value.
	 * <p>
	 * Equivalent to {@code target.compareTo(value) <= 0}.
	 * @param value the baseline value, must not be {@code null}
	 * @return this instance if the current value is less or equal, otherwise an empty
	 * instance
	 * @throws NullPointerException if value is {@code null}
	 */
	public Applier<T> le(T value) {
		Objects.requireNonNull(value);
		if (this.target == null || this.target.compareTo(value) > 0) {
			return empty();
		}
		return this;
	}

	/**
	 * Check whether the current value is {@link Boolean#TRUE}.
	 * <p>
	 * Retains the value only when it is non-null, of type {@code Boolean}, and equals
	 * {@code true}; otherwise returns an empty instance.
	 * @return this instance if the current value is {@code true}, otherwise an empty
	 * instance
	 */
	public Applier<T> isTrue() {
		if (this.target != null && this.target instanceof Boolean val && val) {
			return this;
		}
		return empty();
	}

	/**
	 * If the current value is present (non-null), execute the given consumer on it.
	 * @param consumer the consumer action to execute, must not be {@code null}
	 * @throws NullPointerException if consumer is {@code null}
	 */
	public void apply(Consumer<? super T> consumer) {
		Objects.requireNonNull(consumer);
		if (this.target != null) {
			consumer.accept(this.target);
		}
	}

	/**
	 * If the current value is present (non-null), execute the given action.
	 * <p>
	 * Unlike {@link #apply(Consumer)}, this method does not pass the value to the action,
	 * suitable for scenarios where the target value is not needed.
	 * @param action the action to execute, must not be {@code null}
	 * @throws NullPointerException if action is {@code null}
	 */
	public void apply(Runnable action) {
		Objects.requireNonNull(action);
		if (this.target != null) {
			action.run();
		}
	}

}
