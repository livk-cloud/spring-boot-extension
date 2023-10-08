/*
 * Copyright 2021 spring-boot-extension the original author or authors.
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
 *
 */

package com.livk.commons.micrometer;

import io.micrometer.context.*;
import lombok.experimental.UtilityClass;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * The type Context snapshots.
 *
 * @author livk
 */
@UtilityClass
public class ContextSnapshots {

	public static final ContextSnapshotFactory CONTEXT_SNAPSHOT_FACTORY = ContextSnapshotFactory.builder()
		.contextRegistry(ContextRegistry.getInstance()).clearMissing(false)
		.captureKeyPredicate(key -> true)
		.build();


	/**
	 * Supplier supplier.
	 *
	 * @return the supplier
	 */
	public static Supplier<ContextSnapshot> supplier() {
		return CONTEXT_SNAPSHOT_FACTORY::captureAll;
	}

	/**
	 * Context snapshot context snapshot.
	 *
	 * @return the context snapshot
	 */
	public static ContextSnapshot contextSnapshot() {
		return CONTEXT_SNAPSHOT_FACTORY.captureAll();
	}

	/**
	 * Wrap runnable.
	 *
	 * @param runnable the runnable
	 * @return the runnable
	 */
	public static Runnable wrap(Runnable runnable) {
		return contextSnapshot().wrap(runnable);
	}

	/**
	 * Wrap callable.
	 *
	 * @param <T>      the type parameter
	 * @param callable the callable
	 * @return the callable
	 */
	public static <T> Callable<T> wrap(Callable<T> callable) {
		return contextSnapshot().wrap(callable);
	}

	/**
	 * Wrap consumer.
	 *
	 * @param <T>      the type parameter
	 * @param consumer the consumer
	 * @return the consumer
	 */
	public static <T> Consumer<T> wrap(Consumer<T> consumer) {
		return contextSnapshot().wrap(consumer);
	}

	/**
	 * Wrap executor.
	 *
	 * @param executor the executor
	 * @return the executor
	 */
	public static Executor wrap(Executor executor) {
		return contextSnapshot().wrapExecutor(executor);
	}

	/**
	 * Wrap executor service.
	 *
	 * @param service the service
	 * @return the executor service
	 */
	public static ExecutorService wrap(ExecutorService service) {
		return ContextExecutorService.wrap(service, supplier());
	}

	/**
	 * Wrap scheduled executor service.
	 *
	 * @param service the service
	 * @return the scheduled executor service
	 */
	public static ScheduledExecutorService wrap(ScheduledExecutorService service) {
		return ContextScheduledExecutorService.wrap(service, supplier());
	}
}
