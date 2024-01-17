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

package com.livk.core.disruptor.support;

import com.livk.core.disruptor.DisruptorEventConsumer;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.Sequence;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.core.ResolvableType;

/**
 * The type Spring event handler.
 *
 * @param <T> the type parameter
 * @author livk
 */
@RequiredArgsConstructor
public class SpringEventHandler<T> implements EventHandler<DisruptorEventWrapper<T>> {

	private final ObjectProvider<DisruptorEventConsumer<T>> disruptorEventConsumers;

	/**
	 * Instantiates a new Spring event handler.
	 * @param beanFactory the bean factory
	 * @param type the type
	 */
	public SpringEventHandler(BeanFactory beanFactory, Class<T> type) {
		ResolvableType resolvableType = ResolvableType.forClassWithGenerics(DisruptorEventConsumer.class, type);
		this.disruptorEventConsumers = beanFactory.getBeanProvider(resolvableType);
	}

	@Override
	public void onEvent(DisruptorEventWrapper<T> wrapper, long sequence, boolean endOfBatch) throws Exception {
		for (DisruptorEventConsumer<T> disruptorEventConsumer : disruptorEventConsumers) {
			disruptorEventConsumer.onEvent(wrapper.unwrap(), sequence, endOfBatch);
		}
	}

	@Override
	public void setSequenceCallback(Sequence sequenceCallback) {
		disruptorEventConsumers.orderedStream().forEach(consumer -> consumer.setSequenceCallback(sequenceCallback));
	}

	@Override
	public void onBatchStart(long batchSize, long queueDepth) {
		disruptorEventConsumers.orderedStream().forEach(consumer -> consumer.onBatchStart(batchSize, queueDepth));
	}

	@Override
	public void onStart() {
		disruptorEventConsumers.orderedStream().forEach(DisruptorEventConsumer::onStart);
	}

	@Override
	public void onShutdown() {
		disruptorEventConsumers.orderedStream().forEach(DisruptorEventConsumer::onShutdown);
	}

	@Override
	public void onTimeout(long sequence) throws Exception {
		for (DisruptorEventConsumer<T> disruptorEventConsumer : disruptorEventConsumers) {
			disruptorEventConsumer.onTimeout(sequence);
		}
	}

}
