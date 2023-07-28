package com.livk.autoconfigure.disruptor.factory;

import org.springframework.lang.NonNull;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author livk
 */
public class DisruptorThreadFactory implements ThreadFactory {
    private static final AtomicLong index = new AtomicLong(1);

    @Override
    public Thread newThread(@NonNull Runnable r) {
        return new Thread(r, "disruptor-" + index.getAndIncrement());
    }
}
