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

package com.livk.commons.util;

/**
 * <p>
 * 雪花算法生成器
 * </p>
 *
 * @author livk
 */
public class SnowflakeIdGenerator {

	// 常量部分
	private static final long START_TIMESTAMP = 1609459200000L; // 开始时间戳（2021-01-01）

	private static final long SEQUENCE_BIT = 12; // 序列号占用的位数

	private static final long MACHINE_BIT = 5; // 机器标识占用的位数

	private static final long DATA_CENTER_BIT = 5; // 数据中心占用的位数

	// 最大值计算
	private static final long MAX_SEQUENCE = ~(-1L << SEQUENCE_BIT);

	private static final long MAX_MACHINE_ID = ~(-1L << MACHINE_BIT);

	private static final long MAX_DATA_CENTER_ID = ~(-1L << DATA_CENTER_BIT);

	// 移位操作
	private static final long MACHINE_SHIFT = SEQUENCE_BIT;

	private static final long DATA_CENTER_SHIFT = SEQUENCE_BIT + MACHINE_BIT;

	private static final long TIMESTAMP_SHIFT = SEQUENCE_BIT + MACHINE_BIT + DATA_CENTER_BIT;

	// 成员变量
	private final long dataCenterId; // 数据中心 ID

	private final long machineId; // 机器 ID

	private long sequence = 0L; // 当前序列号

	private volatile long lastTimestamp = -1L; // 上一次生成 ID 的时间戳，使用 volatile 保证可见性

	/**
	 * 构造方法
	 * @param dataCenterId 数据中心 ID
	 * @param machineId 机器 ID
	 */
	public SnowflakeIdGenerator(long dataCenterId, long machineId) {
		if (dataCenterId > MAX_DATA_CENTER_ID || dataCenterId < 0) {
			throw new IllegalArgumentException("DataCenter ID exceeds range: 0-" + MAX_DATA_CENTER_ID);
		}
		if (machineId > MAX_MACHINE_ID || machineId < 0) {
			throw new IllegalArgumentException("Machine ID exceeds range: 0-" + MAX_MACHINE_ID);
		}
		this.dataCenterId = dataCenterId;
		this.machineId = machineId;
	}

	/**
	 * 生成下一个唯一 ID
	 * @return 唯一 ID
	 */
	public synchronized long nextId() {
		long currentTimestamp = System.currentTimeMillis();

		if (currentTimestamp < lastTimestamp) {
			// 时间回拨，建议重试而不是抛出异常
			waitForNextMillisecond(lastTimestamp);
		}

		if (currentTimestamp == lastTimestamp) {
			// 同一毫秒内
			sequence = (sequence + 1) & MAX_SEQUENCE;
			if (sequence == 0L) {
				// 序列号溢出，阻塞到下一毫秒
				currentTimestamp = getNextMillisecond(lastTimestamp);
			}
		}
		else {
			// 不同毫秒，重置序列号
			sequence = 0L;
		}

		lastTimestamp = currentTimestamp;

		// 组合生成 ID
		return ((currentTimestamp - START_TIMESTAMP) << TIMESTAMP_SHIFT) | (dataCenterId << DATA_CENTER_SHIFT)
				| (machineId << MACHINE_SHIFT) | sequence;
	}

	/**
	 * 阻塞直到下一毫秒
	 * @param lastTimestamp 上一毫秒
	 * @return 当前时间戳
	 */
	private long getNextMillisecond(long lastTimestamp) {
		long currentTimestamp = System.currentTimeMillis();
		while (currentTimestamp <= lastTimestamp) {
			currentTimestamp = System.currentTimeMillis();
		}
		return currentTimestamp;
	}

	/**
	 * 等待下一毫秒
	 * @param lastTimestamp 上一毫秒
	 */
	private void waitForNextMillisecond(long lastTimestamp) {
		long currentTimestamp = System.currentTimeMillis();
		// 设置一个较高的最大重试次数，允许系统时间回拨一定范围
		int maxRetries = 20; // 根据具体场景调整
		int retries = 0;
		while (currentTimestamp <= lastTimestamp && retries < maxRetries) {
			currentTimestamp = System.currentTimeMillis();
			retries++;
			try {
				// 添加适当的延迟，减少 CPU 占用，避免高频轮询
				Thread.sleep(1); // 睡眠 1 毫秒，避免过多的 CPU 占用
			}
			catch (InterruptedException e) {
				Thread.currentThread().interrupt(); // 恢复中断状态
			}
		}
		if (retries >= maxRetries) {
			throw new IllegalStateException("Time moved backwards and exceeded retry limit.");
		}
	}

}
