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

package com.livk.context.storage;

import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * The type storage template.
 *
 * @author livk
 */
@RequiredArgsConstructor
public final class StorageTemplate implements StorageOperations {

	@Delegate
	private final AbstractStorageService<?> storageService;

	private final ExecutorService service = Executors.newVirtualThreadPerTaskExecutor();

	/**
	 * Remove bucket and obj.
	 * @param bucketName the bucket name
	 */
	public void removeBucketAndObj(String bucketName) {
		if (this.exist(bucketName)) {
			String[] filenames = this.getAllObj(bucketName).toArray(String[]::new);
			this.removeObjs(bucketName, filenames);
			this.removeBucket(bucketName);
		}
	}

	public void initBucket(String... bucketNames) {
		List<CompletableFuture<Void>> tasks = Arrays.stream(bucketNames).map(this::createBucketOfVirtual).toList();

		CompletableFuture.allOf(tasks.toArray(CompletableFuture[]::new)).join();
	}

	public Map<String, InputStream> download(String bucketName, String... fileNames) {
		Map<String, CompletableFuture<InputStream>> taskMap = Arrays.stream(fileNames)
			.collect(Collectors.toMap(Function.identity(), name -> downloadOfVirtual(bucketName, name)));

		CompletableFuture.allOf(taskMap.values().toArray(CompletableFuture[]::new)).join();

		return Maps.transformValues(taskMap, future -> Objects.requireNonNull(future).join());
	}

	/**
	 * Get external link string.
	 * @param bucketName the bucket name
	 * @param fileName the file name
	 * @return the string
	 */
	public String getExternalLink(String bucketName, String fileName) {
		String url = getStrUrl(bucketName, fileName);
		int index = url.indexOf('?');
		if (index == -1) {
			return url;
		}
		else {
			return url.substring(0, index);
		}
	}

	private CompletableFuture<InputStream> downloadOfVirtual(String bucketName, String fileName) {
		return CompletableFuture.supplyAsync(() -> storageService.download(bucketName, fileName), service);
	}

	private CompletableFuture<Void> createBucketOfVirtual(String bucketName) {
		return CompletableFuture.runAsync(() -> storageService.createBucket(bucketName), service);
	}

}
