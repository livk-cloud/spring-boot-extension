/*
 * Copyright 2021-2024 spring-boot-extension the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.livk.commons.micrometer;

import io.micrometer.context.ContextExecutorService;
import io.micrometer.context.ContextRegistry;
import io.micrometer.context.ContextScheduledExecutorService;
import io.micrometer.context.ContextSnapshot;
import io.micrometer.context.ContextSnapshotFactory;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Consumer;
import java.util.function.Supplier;
import lombok.experimental.UtilityClass;

/**
 * ContextSnapshot相关工具
 *
 * @author livk
 * @see ContextSnapshot
 * @see ContextSnapshotFactory
 */
@UtilityClass
public class ContextSnapshots {

	/**
	 * 构建一个ContextSnapshotFactory
	 */
	public static final ContextSnapshotFactory CONTEXT_SNAPSHOT_FACTORY = ContextSnapshotFactory.builder()
		.contextRegistry(ContextRegistry.getInstance())
		.clearMissing(false)
		.captureKeyPredicate(key -> true)
		.build();

	/**
	 * 创建一个Supplier ContextSnapshot
	 * @return Supplier
	 */
	public static Supplier<ContextSnapshot> supplier() {
		return CONTEXT_SNAPSHOT_FACTORY::captureAll;
	}

	/**
	 * 创建一个ContextSnapshot
	 * @return ContextSnapshot
	 */
	public static ContextSnapshot contextSnapshot() {
		return CONTEXT_SNAPSHOT_FACTORY.captureAll();
	}

	/**
	 * 使用ContextSnapshot包装runnable
	 * @param runnable 待包装的Runnable
	 * @return Runnable
	 */
	public static Runnable wrap(Runnable runnable) {
		return contextSnapshot().wrap(runnable);
	}

	/**
	 * 使用ContextSnapshot包装Callable
	 * @param <T> 泛型
	 * @param callable 待包装的Callable
	 * @return Callable
	 */
	public static <T> Callable<T> wrap(Callable<T> callable) {
		return contextSnapshot().wrap(callable);
	}

	/**
	 * 使用ContextSnapshot包装Consumer
	 * @param <T> 泛型
	 * @param consumer 待包装的Consumer
	 * @return Consumer
	 */
	public static <T> Consumer<T> wrap(Consumer<T> consumer) {
		return contextSnapshot().wrap(consumer);
	}

	/**
	 * 使用ContextSnapshot包装Executor
	 * @param executor 待包装的Executor
	 * @return Executor
	 */
	public static Executor wrap(Executor executor) {
		return contextSnapshot().wrapExecutor(executor);
	}

	/**
	 * 使用ContextSnapshot包装ExecutorService
	 * @param service 待包装的ExecutorService
	 * @return ExecutorService
	 */
	public static ExecutorService wrap(ExecutorService service) {
		return ContextExecutorService.wrap(service, supplier());
	}

	/**
	 * 使用ContextSnapshot包装ScheduledExecutorService
	 * @param service 待包装的ScheduledExecutorService
	 * @return ScheduledExecutorService
	 */
	public static ScheduledExecutorService wrap(ScheduledExecutorService service) {
		return ContextScheduledExecutorService.wrap(service, supplier());
	}

}
