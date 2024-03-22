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

package com.livk.context.disruptor.factory;

import com.livk.context.disruptor.support.DisruptorCustomizer;
import com.livk.context.disruptor.support.SpringDisruptor;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.dsl.ProducerType;
import java.util.concurrent.ThreadFactory;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.ResolvableType;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;

/**
 * @author livk
 */
public class DisruptorFactoryBean<T>
		implements FactoryBean<SpringDisruptor<T>>, BeanFactoryAware, InitializingBean, DisposableBean {

	@Setter
	private AnnotationAttributes attributes;

	private BeanFactory beanFactory;

	private SpringDisruptor<T> disruptor;

	@Setter
	private Class<T> type;

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

	private ThreadFactory createThreadFactory() {
		String threadFactoryBeanName = attributes.getString("threadFactoryBeanName");
		if (StringUtils.hasText(threadFactoryBeanName)) {
			return beanFactory.getBean(threadFactoryBeanName, ThreadFactory.class);
		}
		Class<? extends ThreadFactory> factoryClass = attributes.getClass("threadFactory");
		ThreadFactory threadFactory = BeanUtils.instantiateClass(factoryClass);
		return new VirtualThreadFactory(threadFactory);
	}

	private WaitStrategy createWaitStrategy() {
		String strategyBeanName = attributes.getString("strategyBeanName");
		if (StringUtils.hasText(strategyBeanName)) {
			return beanFactory.getBean(strategyBeanName, WaitStrategy.class);
		}
		Class<? extends WaitStrategy> strategyClass = attributes.getClass("strategy");
		return BeanUtils.instantiateClass(strategyClass);
	}

	@Override
	public void afterPropertiesSet() {
		SpringEventFactory<T> factory = new SpringEventFactory<>();
		ResolvableType disruptorCustomizerType = ResolvableType.forClassWithGenerics(DisruptorCustomizer.class, type);
		int bufferSize = attributes.getNumber("bufferSize").intValue();
		ProducerType producerType = attributes.getEnum("type");
		disruptor = new SpringDisruptor<>(factory, bufferSize, createThreadFactory(), producerType,
				createWaitStrategy());
		disruptor.handleEventsWith(SpringEventHandlerFactory.create(beanFactory, type));
		beanFactory.<DisruptorCustomizer<T>>getBeanProvider(disruptorCustomizerType)
			.forEach(customizer -> customizer.customize(disruptor));
		disruptor.start();
	}

	@Override
	public void destroy() {
		disruptor.shutdown();
	}

}
