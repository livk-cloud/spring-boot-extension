package com.livk.autoconfigure.disruptor.factory;

import com.livk.autoconfigure.disruptor.support.DisruptorEventWrapper;
import com.lmax.disruptor.EventFactory;

/**
 * @author livk
 */
public class SpringEventFactory<T> implements EventFactory<DisruptorEventWrapper<T>> {

    public DisruptorEventWrapper<T> newInstance() {
        return new DisruptorEventWrapper<>();
    }
}
