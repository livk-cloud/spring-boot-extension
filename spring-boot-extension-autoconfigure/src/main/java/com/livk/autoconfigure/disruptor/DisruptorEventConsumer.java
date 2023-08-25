package com.livk.autoconfigure.disruptor;

import com.lmax.disruptor.EventHandler;

/**
 * @author livk
 */
public interface DisruptorEventConsumer<T> extends EventHandler<T> {

}
