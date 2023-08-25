package com.livk.autoconfigure.disruptor.support;

import com.livk.autoconfigure.disruptor.support.SpringDisruptor;
import com.livk.commons.spring.Customizer;

/**
 * @author livk
 */
public interface DisruptorCustomizer<T> extends Customizer<SpringDisruptor<T>> {
}
