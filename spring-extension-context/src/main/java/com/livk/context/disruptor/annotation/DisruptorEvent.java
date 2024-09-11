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

package com.livk.context.disruptor.annotation;

import com.livk.context.disruptor.factory.DisruptorThreadFactory;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.dsl.ProducerType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.ThreadFactory;

/**
 * The interface Disruptor event.
 *
 * @author livk
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DisruptorEvent {

	/**
	 * Value string.
	 * @return the string
	 */
	String value() default "";

	/**
	 * Buffer size int.
	 * @return the int
	 */
	int bufferSize() default 1024 * 256;

	/**
	 * Type producer type.
	 * @return the producer type
	 */
	ProducerType type() default ProducerType.SINGLE;

	/**
	 * Thread factory class.
	 * @return the class
	 */
	Class<? extends ThreadFactory> threadFactory() default DisruptorThreadFactory.class;

	/**
	 * Thread factory bean name string.
	 * @return the string
	 */
	String threadFactoryBeanName() default "";

	/**
	 * 是否开启虚拟线程
	 */
	boolean isVirtual() default true;

	/**
	 * Strategy class.
	 * @return the class
	 */
	Class<? extends WaitStrategy> strategy() default BlockingWaitStrategy.class;

	/**
	 * Strategy bean name string.
	 * @return the string
	 */
	String strategyBeanName() default "";

}
