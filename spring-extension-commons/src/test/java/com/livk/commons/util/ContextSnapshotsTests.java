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

import io.micrometer.context.ContextSnapshot;
import org.junit.jupiter.api.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
class ContextSnapshotsTests {

	@Test
	void testCapture() {
		ContextSnapshot snapshot = ContextSnapshots.capture();
		assertThat(snapshot).isNotNull();
	}

	@Test
	void testWrapRunnable() {
		AtomicBoolean executed = new AtomicBoolean(false);
		Runnable runnable = () -> executed.set(true);
		Runnable wrapped = ContextSnapshots.wrap(runnable);

		assertThat(wrapped).isNotNull();
		wrapped.run();
		assertThat(executed.get()).isTrue();
	}

	@Test
	void testWrapCallable() throws Exception {
		Callable<String> callable = () -> "test";
		Callable<String> wrapped = ContextSnapshots.wrap(callable);

		assertThat(wrapped).isNotNull();
		assertThat(wrapped.call()).isEqualTo("test");
	}

	@Test
	void testWrapConsumer() {
		AtomicReference<String> result = new AtomicReference<>();
		Consumer<String> consumer = result::set;
		Consumer<String> wrapped = ContextSnapshots.wrap(consumer);

		assertThat(wrapped).isNotNull();
		wrapped.accept("test");
		assertThat(result.get()).isEqualTo("test");
	}

	@Test
	void testWrapExecutor() {
		Executor executor = Runnable::run;
		Executor wrapped = ContextSnapshots.wrap(executor);

		assertThat(wrapped).isNotNull();
		AtomicBoolean executed = new AtomicBoolean(false);
		wrapped.execute(() -> executed.set(true));
		assertThat(executed.get()).isTrue();
	}

	@Test
	void testWrapExecutorService() {
		ExecutorService service = Executors.newSingleThreadExecutor();
		ExecutorService wrapped = ContextSnapshots.wrap(service);

		assertThat(wrapped).isNotNull();
		wrapped.shutdown();
	}

	@Test
	void testWrapScheduledExecutorService() {
		ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
		ScheduledExecutorService wrapped = ContextSnapshots.wrap(service);

		assertThat(wrapped).isNotNull();
		wrapped.shutdown();
	}

}
