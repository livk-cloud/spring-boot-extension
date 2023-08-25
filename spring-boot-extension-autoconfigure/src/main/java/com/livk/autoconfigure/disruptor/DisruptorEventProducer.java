package com.livk.autoconfigure.disruptor;

import com.livk.autoconfigure.disruptor.support.DisruptorEventWrapper;
import com.livk.autoconfigure.disruptor.support.SpringDisruptor;
import com.lmax.disruptor.RingBuffer;

import java.util.Arrays;
import java.util.List;

/**
 * The type Disruptor event producer.
 *
 * @param <T> the type parameter
 * @author livk
 */
public class DisruptorEventProducer<T> {

    private final RingBuffer<DisruptorEventWrapper<T>> ringBuffer;

	/**
	 * Instantiates a new Disruptor event producer.
	 *
	 * @param disruptor the disruptor
	 */
	public DisruptorEventProducer(SpringDisruptor<T> disruptor) {
        ringBuffer = disruptor.getRingBuffer();
    }

	/**
	 * Send.
	 *
	 * @param data the data
	 */
	public final void send(T data) {
        long sequence = ringBuffer.next();
        try {
            DisruptorEventWrapper<T> event = ringBuffer.get(sequence);
            event.wrap(data);
        } finally {
            ringBuffer.publish(sequence);
        }
    }

	/**
	 * Send batch.
	 *
	 * @param dataList the data list
	 */
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

	/**
	 * Send batch.
	 *
	 * @param dataArray the data array
	 */
	@SafeVarargs
    public final void sendBatch(T... dataArray) {
        sendBatch(Arrays.asList(dataArray));
    }
}
