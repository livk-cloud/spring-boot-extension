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

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author livk
 */
@Slf4j
class SnowflakeIdGeneratorTests {

	final SnowflakeIdGenerator generator = new SnowflakeIdGenerator(1, 2);

	@Test
	void nextId() throws InterruptedException {
		int num = 10000;
		List<Long> list = new CopyOnWriteArrayList<>();
		try (ExecutorService service = Executors.newFixedThreadPool(1000, Thread.ofVirtual().factory())) {
			CountDownLatch countDownLatch = new CountDownLatch(num);
			for (int i = 0; i < num; i++) {
				service.execute(() -> {
					list.add(generator.nextId());
					countDownLatch.countDown();
				});
			}
			countDownLatch.await();
			service.shutdown();

			// 用 AssertJ 断言 list 中无重复元素
			assertThat(list).doesNotHaveDuplicates();
		}
	}

	@Test
	void testUniqueIds() {
		int num = 10000;
		Set<Long> idSet = new HashSet<>();
		for (int i = 0; i < num; i++) {
			long id = generator.nextId();
			assertThat(idSet).doesNotContain(id);
			idSet.add(id);
		}
	}

	@Test
	void testTimeRollbackHandling() throws InterruptedException {
		long id1 = generator.nextId();

		System.setProperty("user.timezone", "UTC"); // 模拟时区变化
		Thread.sleep(1000);

		long id2 = generator.nextId();

		assertThat(id1).isNotEqualTo(id2).withFailMessage("生成的 ID 相同，时间回拨没有处理正确");
	}

	@Test
	void testSequenceOverflow() {
		long startTime = System.currentTimeMillis();
		long lastId = generator.nextId();

		for (int i = 0; i < 10000; i++) {
			long currentId = generator.nextId();
			assertThat(currentId).isNotEqualTo(lastId).withFailMessage("ID 重复：" + currentId);
			lastId = currentId;
		}

		long endTime = System.currentTimeMillis();
		log.info("生成 10000 个 ID 耗时：{} 毫秒", endTime - startTime);
	}

	@Test
	void testBoundaryCondition() throws InterruptedException {
		long lastId = generator.nextId();
		Thread.sleep(2);
		long newId = generator.nextId();

		assertThat(newId).isNotEqualTo(lastId).withFailMessage("跨毫秒生成的 ID 重复");
	}

}
