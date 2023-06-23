/*
 * Copyright 2021 spring-boot-extension the original author or authors.
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
 *
 */

package com.livk.commons.util;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * <p>
 * SnowflakeTest
 * </p>
 *
 * @author livk
 */
class SnowflakeTest {

	static final Snowflake SNOWFLAKE = new Snowflake();

	@Test
	void nextId() throws InterruptedException {
		int num = 10000;
		List<Long> list = new CopyOnWriteArrayList<>();
		ExecutorService service = Executors.newFixedThreadPool(1000);
		CountDownLatch countDownLatch = new CountDownLatch(num);
		for (int i = 0; i < num; i++) {
			service.submit(() -> {
				list.add(SNOWFLAKE.nextId());
				countDownLatch.countDown();
			});
		}
		countDownLatch.await();
		service.shutdown();
		Set<Long> set = new HashSet<>(list);
		assertEquals(set.size(), list.size());
	}
}
