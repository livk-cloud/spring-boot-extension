package com.livk.autoconfigure.disruptor.support;

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
	 *
	 * @param eventFactory   the event factory
	 * @param ringBufferSize the ring buffer size
	 * @param threadFactory  the thread factory
	 */
	public SpringDisruptor(EventFactory<DisruptorEventWrapper<T>> eventFactory,
                           int ringBufferSize,
                           ThreadFactory threadFactory) {
        super(eventFactory, ringBufferSize, threadFactory);
    }

	/**
	 * Instantiates a new Spring disruptor.
	 *
	 * @param eventFactory   the event factory
	 * @param ringBufferSize the ring buffer size
	 * @param threadFactory  the thread factory
	 * @param producerType   the producer type
	 * @param waitStrategy   the wait strategy
	 */
	public SpringDisruptor(EventFactory<DisruptorEventWrapper<T>> eventFactory,
                           int ringBufferSize,
                           ThreadFactory threadFactory,
                           ProducerType producerType,
                           WaitStrategy waitStrategy) {
        super(eventFactory, ringBufferSize, threadFactory, producerType, waitStrategy);
    }
}
