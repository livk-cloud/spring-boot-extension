package com.livk.autoconfigure.disruptor.annotation;

import com.livk.autoconfigure.disruptor.factory.DisruptorThreadFactory;
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
	 *
	 * @return the string
	 */
	String value() default "";

	/**
	 * Buffer size int.
	 *
	 * @return the int
	 */
	int bufferSize() default 1024 * 256;

	/**
	 * Type producer type.
	 *
	 * @return the producer type
	 */
	ProducerType type() default ProducerType.SINGLE;

	/**
	 * Thread factory class.
	 *
	 * @return the class
	 */
	Class<? extends ThreadFactory> threadFactory() default DisruptorThreadFactory.class;

	/**
	 * Thread factory bean name string.
	 *
	 * @return the string
	 */
	String threadFactoryBeanName() default "";

	/**
	 * Strategy class.
	 *
	 * @return the class
	 */
	Class<? extends WaitStrategy> strategy() default BlockingWaitStrategy.class;

	/**
	 * Strategy bean name string.
	 *
	 * @return the string
	 */
	String strategyBeanName() default "";
}
