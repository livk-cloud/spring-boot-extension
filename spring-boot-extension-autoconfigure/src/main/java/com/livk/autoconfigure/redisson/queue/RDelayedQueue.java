package com.livk.autoconfigure.redisson.queue;

import org.redisson.api.RDestroyable;
import org.redisson.api.RFuture;
import org.redisson.api.RQueue;

import java.util.concurrent.TimeUnit;

/**
 * Copy of {@link org.redisson.api.RDelayedQueue}
 * <p>
 * 在Redisson 3.46.0 中被废弃,替代品仅Pro可用
 *
 * @author livk
 */
public interface RDelayedQueue<V> extends RQueue<V>, RDestroyable {

	/**
	 * Inserts element into this queue with specified transfer delay to destination queue.
	 * @param e the element to add
	 * @param delay for transition
	 * @param timeUnit for delay
	 */
	void offer(V e, long delay, TimeUnit timeUnit);

	/**
	 * Inserts element into this queue with specified transfer delay to destination queue.
	 * @param e the element to add
	 * @param delay for transition
	 * @param timeUnit for delay
	 * @return void
	 */
	RFuture<Void> offerAsync(V e, long delay, TimeUnit timeUnit);

}
