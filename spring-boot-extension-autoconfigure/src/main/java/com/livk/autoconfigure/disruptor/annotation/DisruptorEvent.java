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
 * @author livk
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DisruptorEvent {

    String value() default "";

    int bufferSize() default 1024 * 256;

    ProducerType type() default ProducerType.SINGLE;

    Class<? extends ThreadFactory> threadFactory() default DisruptorThreadFactory.class;

    String threadFactoryBeanName() default "";

    Class<? extends WaitStrategy> strategy() default BlockingWaitStrategy.class;

    String strategyBeanName() default "";
}
