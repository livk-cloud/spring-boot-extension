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

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.NetworkInterface;

/**
 * <p>
 * 雪花算法生成器
 * </p>
 *
 * @author livk
 */
@Slf4j
public class Snowflake {

	private final static long TIME_START_BASE = System.currentTimeMillis();

	private final static long WORKER_ID_BITS = 5L;

	private final static long DATACENTER_ID_BITS = 5L;

	private final static long MAX_WORKER_ID = ~(-1L << WORKER_ID_BITS);

	private final static long MAX_DATACENTER_ID = ~(-1L << DATACENTER_ID_BITS);

	private final static long SEQUENCE_BITS = 12L;

	private final static long WORKER_ID_SHIFT = SEQUENCE_BITS;

	private final static long DATACENTER_ID_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;

	private final static long TIMESTAMP_LEFT_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS + DATACENTER_ID_BITS;

	private final static long SEQUENCE_MASK = ~(-1L << SEQUENCE_BITS);

	private static long lastTimestamp = -1L;

	private final long workerId;

	private final long datacenterId;

	private long sequence = 0L;

	/**
	 * 构建一个默认的生成器
	 */
	public Snowflake() {
		this(-1, -1);
	}

	/**
	 * 构建一个的生成器
	 * @param workerId Working machine ID
	 * @param datacenterId Serial number
	 */
	public Snowflake(long workerId, long datacenterId) {
		if (datacenterId > MAX_DATACENTER_ID || datacenterId < 0) {
			datacenterId = getMaxDatacenterId();
		}
		if (workerId > MAX_WORKER_ID || workerId < 0) {
			workerId = getMaxWorkerId(datacenterId);
		}
		this.workerId = workerId;
		this.datacenterId = datacenterId;
	}

	/**
	 * <p>
	 * 获取 maxWorkerId
	 * </p>
	 * @param datacenterId the datacenter id
	 * @return the max worker id
	 */
	protected static long getMaxWorkerId(long datacenterId) {
		StringBuilder buffer = new StringBuilder();
		buffer.append(datacenterId);
		String name = ManagementFactory.getRuntimeMXBean().getName();
		if (StringUtils.hasText(name)) {
			buffer.append(name.split("@")[0]);
		}
		return (buffer.toString().hashCode() & 0xffff) % (MAX_WORKER_ID + 1);
	}

	/**
	 * <p>
	 * 数据标识id部分
	 * </p>
	 * @return the max datacenter id
	 */
	protected static long getMaxDatacenterId() {
		long id = 0L;
		try {
			InetAddress ip = InetAddress.getLocalHost();
			NetworkInterface network = NetworkInterface.getByInetAddress(ip);
			if (network == null) {
				id = 1L;
			}
			else {
				byte[] mac = network.getHardwareAddress();
				id = ((0x000000FF & (long) mac[mac.length - 1])
						| (0x0000FF00 & (((long) mac[mac.length - 2]) << 8))) >> 6;
				id = id % (MAX_DATACENTER_ID + 1);
			}
		}
		catch (Exception e) {
			log.error("getDatacenterId:{}", e.getMessage(), e);
		}
		return id;
	}

	/**
	 * 获取下一个ID
	 * @return ID long
	 */
	public synchronized long nextId() {
		long timestamp = System.currentTimeMillis();
		if (timestamp < lastTimestamp) {
			throw new RuntimeException(String.format(
					"Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
		}

		if (lastTimestamp == timestamp) {
			sequence = (sequence + 1) & SEQUENCE_MASK;
			if (sequence == 0) {
				timestamp = tilNextMillis(lastTimestamp);
			}
		}
		else {
			sequence = 0L;
		}
		lastTimestamp = timestamp;

		return ((timestamp - TIME_START_BASE) << TIMESTAMP_LEFT_SHIFT) | (datacenterId << DATACENTER_ID_SHIFT)
				| (workerId << WORKER_ID_SHIFT) | sequence;
	}

	private long tilNextMillis(long lastTimestamp) {
		long timestamp = System.currentTimeMillis();
		while (timestamp <= lastTimestamp) {
			timestamp = System.currentTimeMillis();
		}
		return timestamp;
	}

}
