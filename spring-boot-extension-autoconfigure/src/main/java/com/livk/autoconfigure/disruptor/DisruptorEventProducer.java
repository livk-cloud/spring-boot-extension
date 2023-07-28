package com.livk.autoconfigure.disruptor;

import com.livk.autoconfigure.disruptor.support.DisruptorEventWrapper;
import com.livk.autoconfigure.disruptor.support.SpringDisruptor;
import com.lmax.disruptor.RingBuffer;

import java.util.Arrays;
import java.util.List;

/**
 * @author livk
 */
public class DisruptorEventProducer<T> {

    private final RingBuffer<DisruptorEventWrapper<T>> ringBuffer;

    public DisruptorEventProducer(SpringDisruptor<T> disruptor) {
        ringBuffer = disruptor.getRingBuffer();
    }

    public final void send(T data) {
        long sequence = ringBuffer.next();
        try {
            DisruptorEventWrapper<T> event = ringBuffer.get(sequence);
            event.wrap(data);
        } finally {
            ringBuffer.publish(sequence);
        }
    }

    public final void sendBatch(List<T> dataList) {
        int n = dataList.size();
        long hi = ringBuffer.next(n);
        long lo = hi - (n - 1);
        try {
            for (int i = 0; i < dataList.size(); i++) {
                ringBuffer.get(i + lo).wrap(dataList.get(i));
            }
        } finally {
            ringBuffer.publish(lo, hi);
        }
    }

    @SafeVarargs
    public final void sendBatch(T... dataArray) {
        sendBatch(Arrays.asList(dataArray));
    }
}
