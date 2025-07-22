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

package com.livk.redis;

import com.livk.context.redis.RedisOps;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

/**
 * @author livk
 */
@Component
public class RedisBloom {

	// 布隆过滤器key1
	private static final String BLOOM_KEY = "redis:bloom:filter";

	// 初始化集合长度
	private static final int length = Integer.MAX_VALUE;

	// 准备hash计算次数
	/**
	 * 准备自定义哈希算法需要用到的质数，因为一条数据需要hash计算5次 且5次的结果要不一样
	 */
	private static final int[] primeNums = new int[] { 17, 19, 29, 31, 37 };

	private final ValueOperations<String, Object> forValue;

	public RedisBloom(RedisOps redisOps) {
		this.forValue = redisOps.opsForValue();
	}

	public void addKey(String data) {
		for (int primeNum : primeNums) {
			int hashcode = this.hash(data, primeNum);
			int bitIndex = hashcode & (length - 1);
			forValue.setBit(BLOOM_KEY, bitIndex, true);
		}
	}

	public boolean hasKey(String data) {
		for (int primeNum : primeNums) {
			int hashcode = this.hash(data, primeNum);
			int bitIndex = hashcode & (length - 1);
			if (Boolean.FALSE.equals(forValue.getBit(BLOOM_KEY, bitIndex))) {
				return false;
			}
		}
		return true;
	}

	public int hash(String data, int prime) {
		int h = 0;
		for (char c : data.toCharArray()) {
			h = prime * h + c;
		}
		return h;
	}

}
