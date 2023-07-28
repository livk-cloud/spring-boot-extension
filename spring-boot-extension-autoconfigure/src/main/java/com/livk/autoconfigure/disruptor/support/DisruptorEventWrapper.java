package com.livk.autoconfigure.disruptor.support;

import com.livk.commons.beans.GenericWrapper;

/**
 * @author livk
 */
public class DisruptorEventWrapper<V> implements GenericWrapper<V> {

    private V real;

    public void wrap(V unwrap) {
        if (this.real == null) {
            this.real = unwrap;
        }
    }

    @Override
    public V unwrap() {
        return real;
    }
}
