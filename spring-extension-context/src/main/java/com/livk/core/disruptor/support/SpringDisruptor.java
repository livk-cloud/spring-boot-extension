/*
 * Copyright 2021 spring-boot-extension the original author or authors.
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
 *
 */

package com.livk.core.disruptor.support;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.concurrent.ThreadFactory;

/**
 * The type Spring disruptor.
 *
 * @param <T> the type parameter
 * @author livk
 */
public class SpringDisruptor<T> extends Disruptor<DisruptorEventWrapper<T>> {

	/**
	 * Instantiates a new Spring disruptor.
	 * @param eventFactory the event factory
	 * @param ringBufferSize the ring buffer size
	 * @param threadFactory the thread factory
	 */
	public SpringDisruptor(EventFactory<DisruptorEventWrapper<T>> eventFactory, int ringBufferSize,
			ThreadFactory threadFactory) {
		super(eventFactory, ringBufferSize, threadFactory);
	}

	/**
	 * Instantiates a new Spring disruptor.
	 * @param eventFactory the event factory
	 * @param ringBufferSize the ring buffer size
	 * @param threadFactory the thread factory
	 * @param producerType the producer type
	 * @param waitStrategy the wait strategy
	 */
	public SpringDisruptor(EventFactory<DisruptorEventWrapper<T>> eventFactory, int ringBufferSize,
			ThreadFactory threadFactory, ProducerType producerType, WaitStrategy waitStrategy) {
		super(eventFactory, ringBufferSize, threadFactory, producerType, waitStrategy);
	}

}
