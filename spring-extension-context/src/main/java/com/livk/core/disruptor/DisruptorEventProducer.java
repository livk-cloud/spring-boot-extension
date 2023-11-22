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

package com.livk.core.disruptor;

import com.livk.core.disruptor.support.DisruptorEventWrapper;
import com.livk.core.disruptor.support.SpringDisruptor;
import com.lmax.disruptor.RingBuffer;

import java.util.Arrays;
import java.util.List;

/**
 * The type Disruptor event producer.
 *
 * @param <T> the type parameter
 * @author livk
 */
public class DisruptorEventProducer<T> {

	private final RingBuffer<DisruptorEventWrapper<T>> ringBuffer;

	/**
	 * Instantiates a new Disruptor event producer.
	 * @param disruptor the disruptor
	 */
	public DisruptorEventProducer(SpringDisruptor<T> disruptor) {
		ringBuffer = disruptor.getRingBuffer();
	}

	/**
	 * Send.
	 * @param data the data
	 */
	public final void send(T data) {
		long sequence = ringBuffer.next();
		try {
			DisruptorEventWrapper<T> event = ringBuffer.get(sequence);
			event.wrap(data);
		}
		finally {
			ringBuffer.publish(sequence);
		}
	}

	/**
	 * Send batch.
	 * @param dataList the data list
	 */
	public final void sendBatch(List<T> dataList) {
		int n = dataList.size();
		long hi = ringBuffer.next(n);
		long lo = hi - (n - 1);
		try {
			for (int i = 0; i < dataList.size(); i++) {
				ringBuffer.get(i + lo).wrap(dataList.get(i));
			}
		}
		finally {
			ringBuffer.publish(lo, hi);
		}
	}

	/**
	 * Send batch.
	 * @param dataArray the data array
	 */
	@SafeVarargs
	public final void sendBatch(T... dataArray) {
		sendBatch(Arrays.asList(dataArray));
	}

}
