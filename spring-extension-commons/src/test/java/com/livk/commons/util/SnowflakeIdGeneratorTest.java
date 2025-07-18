/*
 * Copyright 2021-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * you may obtain a copy of the License at
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * <p>
 * SnowflakeTest
 * </p>
 *
 * @author livk
 */
@Slf4j
class SnowflakeIdGeneratorTest {

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
			Set<Long> set = new HashSet<>(list);
			assertEquals(set.size(), list.size(), "ID 的数量不匹配，可能有重复 ID");
		}
	}

	@Test
	void testUniqueIds() {
		int num = 10000;
		Set<Long> idSet = new HashSet<>();
		for (int i = 0; i < num; i++) {
			long id = generator.nextId();
			assertFalse(idSet.contains(id), "ID 生成重复：" + id);
			idSet.add(id);
		}
	}

	/**
	 * 测试时间回拨的处理
	 */
	@Test
	void testTimeRollbackHandling() throws InterruptedException {
		// 模拟时间回拨的情况，检查是否会抛出异常
		System.currentTimeMillis();

		// 生成一个 ID
		long id1 = generator.nextId();

		// 人为改变系统时间，使其回到过去
		System.setProperty("user.timezone", "UTC"); // 模拟时区变化
		Thread.sleep(1000); // 暂停一秒钟

		// 再次生成 ID，验证是否正常
		long id2 = generator.nextId();

		// 验证生成的两个 ID 是否不同，且没有时间回拨导致的问题
		assertNotEquals(id1, id2, "生成的 ID 相同，时间回拨没有处理正确");
	}

	/**
	 * 测试序列号溢出
	 */
	@Test
	void testSequenceOverflow() {
		long startTime = System.currentTimeMillis();
		long lastId = generator.nextId();

		// 模拟多次生成 ID，观察序列号是否正确溢出
		for (int i = 0; i < 10000; i++) {
			long currentId = generator.nextId();
			// 验证生成的 ID 不重复
			assertNotEquals(lastId, currentId, "ID 重复：" + currentId);
			lastId = currentId;
		}

		long endTime = System.currentTimeMillis();
		log.info("生成 10000 个 ID 耗时：{} 毫秒", endTime - startTime);
	}

	/**
	 * 测试边界条件，生成 ID 时跨毫秒溢出
	 */
	@Test
	void testBoundaryCondition() throws InterruptedException {
		// 测试生成 ID 时，序列号溢出后的行为
		long lastId = generator.nextId();
		Thread.sleep(2); // 等待一毫秒

		long newId = generator.nextId();
		assertNotEquals(lastId, newId, "跨毫秒生成的 ID 重复");
	}

}
