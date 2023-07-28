package com.livk.autoconfigure.disruptor.support;

import com.livk.autoconfigure.disruptor.DisruptorEventConsumer;
import com.lmax.disruptor.EventHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.core.ResolvableType;

/**
 * @author livk
 */
@RequiredArgsConstructor
public class SpringEventHandler<T> implements EventHandler<DisruptorEventWrapper<T>> {

    private final ObjectProvider<DisruptorEventConsumer<T>> disruptorEventConsumers;

    public SpringEventHandler(BeanFactory beanFactory, Class<T> type) {
        ResolvableType resolvableType = ResolvableType.forClassWithGenerics(DisruptorEventConsumer.class, type);
        this.disruptorEventConsumers = beanFactory.getBeanProvider(resolvableType);
    }

    @Override
    public void onEvent(DisruptorEventWrapper<T> wrapper, long sequence, boolean endOfBatch) throws Exception {
        for (DisruptorEventConsumer<T> disruptorEventConsumer : disruptorEventConsumers) {
            disruptorEventConsumer.onEvent(wrapper.unwrap(), sequence, endOfBatch);
        }
    }
}
