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

package com.livk.commons.spring.transaction;

import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

/**
 * Spring事务批量操作
 *
 * @author livk
 */
@RequiredArgsConstructor
public class TransactionBatchOperations {

	/**
	 * 默认操作SQL数量
	 */
	public static final Integer DEFAULT_NUM = 1000;

	private final TransactionTemplate transactionTemplate;

	@Setter
	private Executor executor;

	/**
	 * 创建TransactionBatchOperations
	 * @param transactionTemplate the transaction template
	 * @param executor the executor
	 */
	public TransactionBatchOperations(TransactionTemplate transactionTemplate, Executor executor) {
		this(transactionTemplate);
		this.executor = executor;
	}

	/**
	 * 批量执行
	 * <p>
	 * 使用默认数量{@link #DEFAULT_NUM}
	 * @param <T> 泛型
	 * @param dataList 带操作的数据
	 * @param ops 执行过程
	 */
	public <T> void executeBatch(List<T> dataList, Consumer<List<T>> ops) {
		executeBatch(dataList, DEFAULT_NUM, ops);
	}

	/**
	 * 批量执行
	 * @param <T> 泛型
	 * @param dataList 带操作的数据
	 * @param batchNum 批次数量
	 * @param ops 执行过程
	 */
	public <T> void executeBatch(List<T> dataList, int batchNum, Consumer<List<T>> ops) {
		List<List<T>> partition = Lists.partition(dataList, batchNum);
		AtomicBoolean rollback = new AtomicBoolean(false);
		List<CompletableFuture<Void>> futures = new ArrayList<>(partition.size());
		partition.forEach(item -> {
			CompletableFuture<Void> future = execute(() -> transactionTemplate.execute(status -> {
				try {
					ops.accept(item);
				}
				catch (Exception e) {
					rollback.compareAndSet(false, true);
				}
				finally {
					if (rollback.get()) {
						status.setRollbackOnly();
					}
				}
				return true;
			}));
			futures.add(future);
		});
		CompletableFuture.allOf(futures.toArray(new CompletableFuture<?>[0])).join();
		if (rollback.get()) {
			throw new RuntimeException();
		}
	}

	private CompletableFuture<Void> execute(Runnable runnable) {
		if (executor == null) {
			return CompletableFuture.runAsync(runnable);
		}
		else {
			return CompletableFuture.runAsync(runnable, executor);
		}
	}

}
