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

package com.livk.redisson.order;

import com.livk.commons.util.DateUtils;
import com.livk.redisson.order.entity.Employer;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RedissonClient;
import org.redisson.client.RedisException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author livk
 */
@Slf4j
@Component
public class OrderQueueConsumer implements Runnable, InitializingBean, DisposableBean {

	private final RBlockingQueue<Employer> orderQueue;

	private final ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 10, 0, TimeUnit.SECONDS,
			new ArrayBlockingQueue<>(10));

	public OrderQueueConsumer(RedissonClient redissonClient) {
		this.orderQueue = redissonClient.getBlockingQueue("order_queue");
	}

	@Override
	public void run() {
		while (true) {
			try {
				Employer employer = orderQueue.take();
				log.info("订单取消时间：{} ==订单生成时间:{}", DateUtils.format(LocalDateTime.now(), DateUtils.HMS),
						employer.getPutTime());
			}
			catch (InterruptedException e) {
				log.warn("interrupted", e);
				Thread.currentThread().interrupt();
				throw new RedisException(e);
			}
		}
	}

	@Override
	public void afterPropertiesSet() {
		executor.execute(this);
	}

	@Override
	public void destroy() {
		executor.shutdown();
	}

	public boolean isEmpty() {
		return orderQueue.isEmpty();
	}

}
