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

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.LongSupplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;

/**
 * @author livk
 */
class SnowflakeIdGeneratorTests {

	private static final long START_TIMESTAMP = 1609459200000L;

	private static final long SEQUENCE_BIT = 12;

	private static final long MACHINE_BIT = 5;

	private static final long DATA_CENTER_BIT = 5;

	private static final long MAX_SEQUENCE = ~(-1L << SEQUENCE_BIT);

	private static final long MACHINE_SHIFT = SEQUENCE_BIT;

	private static final long DATA_CENTER_SHIFT = SEQUENCE_BIT + MACHINE_BIT;

	private static final long TIMESTAMP_SHIFT = SEQUENCE_BIT + MACHINE_BIT + DATA_CENTER_BIT;

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
	void testBoundaryCondition() throws InterruptedException {
		long lastId = generator.nextId();
		Thread.sleep(2);
		long newId = generator.nextId();

		assertThat(newId).isNotEqualTo(lastId);
	}

	@Test
	void constructorRejectsOutOfRangeIds() {
		assertThatIllegalArgumentException().isThrownBy(() -> new SnowflakeIdGenerator(-1, 0))
			.withMessageContaining("DataCenter ID exceeds range");
		assertThatIllegalArgumentException().isThrownBy(() -> new SnowflakeIdGenerator(32, 0))
			.withMessageContaining("DataCenter ID exceeds range");
		assertThatIllegalArgumentException().isThrownBy(() -> new SnowflakeIdGenerator(0, -1))
			.withMessageContaining("Machine ID exceeds range");
		assertThatIllegalArgumentException().isThrownBy(() -> new SnowflakeIdGenerator(0, 32))
			.withMessageContaining("Machine ID exceeds range");
		assertThatIllegalArgumentException().isThrownBy(() -> new SnowflakeIdGenerator(0, 0, null))
			.withMessageContaining("currentTimeMillis must not be null");
	}

	@Test
	void constructorAcceptsBoundaryIds() {
		AtomicLong clock = new AtomicLong(START_TIMESTAMP + 1_000);
		SnowflakeIdGenerator min = new SnowflakeIdGenerator(0, 0, clock::get);
		SnowflakeIdGenerator max = new SnowflakeIdGenerator(31, 31, clock::get);

		long minId = min.nextId();
		clock.incrementAndGet();
		long maxId = max.nextId();

		assertThat(extractDataCenterId(minId)).isZero();
		assertThat(extractMachineId(minId)).isZero();
		assertThat(extractDataCenterId(maxId)).isEqualTo(31);
		assertThat(extractMachineId(maxId)).isEqualTo(31);
	}

	@Test
	void nextIdEncodesTimestampAndSequence() {
		AtomicLong clock = new AtomicLong(START_TIMESTAMP + 42);
		SnowflakeIdGenerator idGenerator = new SnowflakeIdGenerator(3, 7, clock::get);

		long first = idGenerator.nextId();
		long second = idGenerator.nextId();

		assertThat(extractTimestamp(first)).isEqualTo(42);
		assertThat(extractDataCenterId(first)).isEqualTo(3);
		assertThat(extractMachineId(first)).isEqualTo(7);
		assertThat(extractSequence(first)).isZero();
		assertThat(extractTimestamp(second)).isEqualTo(42);
		assertThat(extractSequence(second)).isEqualTo(1);
	}

	@Test
	void nextIdResetsSequenceAcrossMilliseconds() {
		AtomicLong clock = new AtomicLong(START_TIMESTAMP + 100);
		SnowflakeIdGenerator idGenerator = new SnowflakeIdGenerator(1, 1, clock::get);

		assertThat(extractSequence(idGenerator.nextId())).isZero();
		assertThat(extractSequence(idGenerator.nextId())).isEqualTo(1);

		clock.incrementAndGet();
		assertThat(extractSequence(idGenerator.nextId())).isZero();
	}

	@Test
	void nextIdWaitsWhenSequenceOverflows() {
		long base = START_TIMESTAMP + 200;
		AtomicInteger overflowReads = new AtomicInteger();
		// 0..MAX_SEQUENCE: 同一毫秒内用尽序列；MAX_SEQUENCE+1: 溢出当次仍读到同一毫秒；
		// 之后 getNextMillisecond 首次即读到下一毫秒（不进入 while）
		LongSupplier timeSupplier = () -> overflowReads.getAndIncrement() <= MAX_SEQUENCE + 1 ? base : base + 1;
		SnowflakeIdGenerator idGenerator = new SnowflakeIdGenerator(1, 1, timeSupplier);

		long first = idGenerator.nextId();
		for (int i = 0; i < MAX_SEQUENCE; i++) {
			idGenerator.nextId();
		}
		long overflowId = idGenerator.nextId();

		assertThat(extractTimestamp(first)).isEqualTo(200);
		assertThat(extractTimestamp(overflowId)).isEqualTo(201);
		assertThat(extractSequence(overflowId)).isZero();
	}

	@Test
	void nextIdBlocksInGetNextMillisecondUntilClockAdvances() {
		long base = START_TIMESTAMP + 250;
		AtomicInteger overflowReads = new AtomicInteger();
		// 溢出后 getNextMillisecond 先读到同一毫秒，进入 while，再推进到下一毫秒
		LongSupplier timeSupplier = () -> overflowReads.getAndIncrement() <= MAX_SEQUENCE + 2 ? base : base + 1;
		SnowflakeIdGenerator idGenerator = new SnowflakeIdGenerator(1, 1, timeSupplier);

		idGenerator.nextId();
		for (int i = 0; i < MAX_SEQUENCE; i++) {
			idGenerator.nextId();
		}
		long overflowId = idGenerator.nextId();

		assertThat(extractTimestamp(overflowId)).isEqualTo(251);
		assertThat(extractSequence(overflowId)).isZero();
	}

	@Test
	void nextIdRecoversFromClockRollbackOnSameGenerator() {
		AtomicInteger calls = new AtomicInteger();
		LongSupplier timeSupplier = () -> {
			int call = calls.getAndIncrement();
			if (call == 0) {
				return START_TIMESTAMP + 400;
			}
			if (call < 6) {
				return START_TIMESTAMP + 390;
			}
			return START_TIMESTAMP + 401;
		};
		SnowflakeIdGenerator idGenerator = new SnowflakeIdGenerator(4, 5, timeSupplier);

		long first = idGenerator.nextId();
		long second = idGenerator.nextId();

		assertThat(extractTimestamp(first)).isEqualTo(400);
		assertThat(extractTimestamp(second)).isEqualTo(401);
		assertThat(extractSequence(second)).isZero();
	}

	@Test
	void nextIdFailsWhenClockRollbackExceedsRetryLimit() {
		AtomicInteger calls = new AtomicInteger();
		LongSupplier timeSupplier = () -> {
			int call = calls.getAndIncrement();
			if (call == 0) {
				return START_TIMESTAMP + 500;
			}
			return START_TIMESTAMP + 400;
		};
		SnowflakeIdGenerator idGenerator = new SnowflakeIdGenerator(1, 1, timeSupplier);

		idGenerator.nextId();
		assertThatIllegalStateException().isThrownBy(idGenerator::nextId)
			.withMessageContaining("Time moved backwards and exceeded retry limit");
	}

	@Test
	void waitForNextMillisecondRestoresInterruptStatus() {
		AtomicInteger calls = new AtomicInteger();
		LongSupplier timeSupplier = () -> {
			int call = calls.getAndIncrement();
			if (call == 0) {
				return START_TIMESTAMP + 600;
			}
			if (call < 4) {
				return START_TIMESTAMP + 590;
			}
			return START_TIMESTAMP + 601;
		};
		SnowflakeIdGenerator idGenerator = new SnowflakeIdGenerator(1, 1, timeSupplier);

		idGenerator.nextId();
		Thread.currentThread().interrupt();
		try {
			long id = idGenerator.nextId();
			assertThat(extractTimestamp(id)).isEqualTo(601);
			assertThat(Thread.currentThread().isInterrupted()).isTrue();
		}
		finally {
			// clear interrupt flag for subsequent tests
			boolean ignore = Thread.interrupted();
		}
	}

	private static long extractTimestamp(long id) {
		return id >> TIMESTAMP_SHIFT;
	}

	private static long extractDataCenterId(long id) {
		return (id >> DATA_CENTER_SHIFT) & 0x1F;
	}

	private static long extractMachineId(long id) {
		return (id >> MACHINE_SHIFT) & 0x1F;
	}

	private static long extractSequence(long id) {
		return id & MAX_SEQUENCE;
	}

}
