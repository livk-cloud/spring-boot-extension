package com.livk.autoconfigure.disruptor.support;

import com.livk.commons.beans.GenericWrapper;

/**
 * The type Disruptor event wrapper.
 *
 * @param <V> the type parameter
 * @author livk
 */
public class DisruptorEventWrapper<V> implements GenericWrapper<V> {

    private V real;

	/**
	 * Wrap.
	 *
	 * @param unwrap the unwrap
	 */
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
