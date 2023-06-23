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

package com.livk.redisson.order.controller;

import com.livk.redisson.order.entity.Employer;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 * OrderController
 * </p>
 *
 * @author livk
 */
@RestController
@RequestMapping("order")
public class OrderController implements DisposableBean {

	private final RDelayedQueue<Employer> delayedQueue;

	public OrderController(RedissonClient redissonClient) {
		RBlockingQueue<Employer> orderQueue = redissonClient.getBlockingQueue("order_queue");
		this.delayedQueue = redissonClient.getDelayedQueue(orderQueue);
	}

	@PostMapping("create")
	public void create() throws InterruptedException {
		for (int i = 0; i < 10; i++) {
			TimeUnit.SECONDS.sleep(1L);
			Employer callCdr = new Employer();
			callCdr.setSalary(345.6);
			callCdr.setPutTime();
			delayedQueue.offer(callCdr, 30, TimeUnit.SECONDS);
		}
	}


	@Override
	public void destroy() {
		delayedQueue.destroy();
	}
}
