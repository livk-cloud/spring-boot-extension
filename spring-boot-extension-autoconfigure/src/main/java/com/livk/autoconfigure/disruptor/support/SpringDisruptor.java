package com.livk.autoconfigure.disruptor.support;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.concurrent.ThreadFactory;

/**
 * @author livk
 */
public class SpringDisruptor<T> extends Disruptor<DisruptorEventWrapper<T>> {
    public SpringDisruptor(EventFactory<DisruptorEventWrapper<T>> eventFactory,
                           int ringBufferSize,
                           ThreadFactory threadFactory) {
        super(eventFactory, ringBufferSize, threadFactory);
    }

    public SpringDisruptor(EventFactory<DisruptorEventWrapper<T>> eventFactory,
                           int ringBufferSize,
                           ThreadFactory threadFactory,
                           ProducerType producerType,
                           WaitStrategy waitStrategy) {
        super(eventFactory, ringBufferSize, threadFactory, producerType, waitStrategy);
    }
}
