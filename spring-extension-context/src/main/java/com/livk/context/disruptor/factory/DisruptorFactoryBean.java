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

package com.livk.context.disruptor.factory;

import com.livk.context.disruptor.support.DisruptorEventConsumer;
import com.livk.context.disruptor.support.DisruptorEventWrapper;
import com.livk.context.disruptor.support.SpringDisruptor;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.dsl.ProducerType;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.core.ResolvableType;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.concurrent.ThreadFactory;

/**
 * @author livk
 */
@RequiredArgsConstructor
public class DisruptorFactoryBean<T>
		implements FactoryBean<SpringDisruptor<T>>, BeanFactoryAware, InitializingBean, DisposableBean {

	private final Class<T> type;

	@Setter
	private int bufferSize = 1024 * 256;

	@Setter
	private ProducerType producerType;

	@Setter
	private WaitStrategy waitStrategy;

	@Setter
	private ThreadFactory threadFactory;

	@Setter
	private String strategyBeanName;

	@Setter
	private String threadFactoryBeanName;

	@Setter
	private boolean useVirtualThreads = true;

	private BeanFactory beanFactory;

	private SpringDisruptor<T> disruptor;

	@Override
	public SpringDisruptor<T> getObject() {
		return disruptor;
	}

	@Override
	public Class<?> getObjectType() {
		return SpringDisruptor.class;
	}

	@Override
	public void setBeanFactory(@NonNull BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

	@Override
	public void afterPropertiesSet() {
		SpringEventFactory<T> eventFactory = new SpringEventFactory<>();
		ThreadFactory factory = getThreadFactory();
		Assert.notNull(factory, "threadFactory must not be null");
		WaitStrategy strategy = getWaitStrategy();
		Assert.notNull(strategy, "waitStrategy must not be null");
		Assert.notNull(producerType, "producerType must not be null");
		disruptor = new SpringDisruptor<>(eventFactory, bufferSize, factory, producerType, strategy);
		disruptor.handleEventsWith(createEventHandler(beanFactory, type));
		disruptor.start();
	}

	private ThreadFactory getThreadFactory() {
		ThreadFactory factory = this.threadFactory;
		if (StringUtils.hasText(threadFactoryBeanName)) {
			factory = beanFactory.getBean(threadFactoryBeanName, ThreadFactory.class);
		}
		if (useVirtualThreads) {
			factory = new VirtualThreadFactory(factory);
		}
		return factory;
	}

	private WaitStrategy getWaitStrategy() {
		WaitStrategy strategy = this.waitStrategy;
		if (StringUtils.hasText(strategyBeanName)) {
			strategy = beanFactory.getBean(strategyBeanName, WaitStrategy.class);
		}
		return strategy;
	}

	private EventHandler<DisruptorEventWrapper<T>> createEventHandler(BeanFactory beanFactory, Class<T> type) {
		ResolvableType resolvableType = ResolvableType.forClassWithGenerics(DisruptorEventConsumer.class, type);
		ObjectProvider<DisruptorEventConsumer<T>> disruptorEventConsumers = beanFactory.getBeanProvider(resolvableType);
		return new AggregateEventHandlerProvider<>(disruptorEventConsumers);
	}

	@Override
	public void destroy() {
		disruptor.shutdown();
	}

	private static class SpringEventFactory<T> implements EventFactory<DisruptorEventWrapper<T>> {

		public DisruptorEventWrapper<T> newInstance() {
			return new DisruptorEventWrapper<>();
		}

	}

	@RequiredArgsConstructor
	private static final class AggregateEventHandlerProvider<T> implements EventHandler<DisruptorEventWrapper<T>> {

		private final ObjectProvider<DisruptorEventConsumer<T>> consumerObjectProvider;

		@Override
		public void onEvent(DisruptorEventWrapper<T> event, long sequence, boolean endOfBatch) throws Exception {
			for (DisruptorEventConsumer<T> eventConsumer : consumerObjectProvider) {
				eventConsumer.onEvent(event.unwrap(), sequence, endOfBatch);
			}
		}

		@Override
		public void onStart() {
			for (EventHandler<T> eventHandler : consumerObjectProvider) {
				eventHandler.onStart();
			}
		}

		@Override
		public void onShutdown() {
			for (EventHandler<T> eventHandler : consumerObjectProvider) {
				eventHandler.onShutdown();
			}
		}

	}

	@RequiredArgsConstructor
	private static class VirtualThreadFactory implements ThreadFactory {

		private final ThreadFactory delegate;

		@Override
		public Thread newThread(@NonNull Runnable r) {
			Thread thread = delegate.newThread(r);
			return Thread.ofVirtual()
				.name("virtual-" + thread.getName())
				.inheritInheritableThreadLocals(true)
				.unstarted(thread);
		}

	}

}
