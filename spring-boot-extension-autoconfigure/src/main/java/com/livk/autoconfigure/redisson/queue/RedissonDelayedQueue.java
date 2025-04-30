package com.livk.autoconfigure.redisson.queue;

import org.redisson.api.RQueue;
import org.redisson.api.RedissonClient;
import org.redisson.command.CommandAsyncExecutor;
import org.redisson.config.Config;
import org.redisson.connection.ConnectionManager;
import org.redisson.liveobject.core.RedissonObjectBuilder;

/**
 * @author livk
 */
public class RedissonDelayedQueue<V> extends org.redisson.RedissonDelayedQueue<V> implements RDelayedQueue<V> {

	public RedissonDelayedQueue(RedissonClient redisson, RQueue<V> queue) {
		super(queue.getCodec(), RedissonDelayedQueue.getCommandExecutor(redisson), queue.getName());
	}

	private static CommandAsyncExecutor getCommandExecutor(RedissonClient redisson) {
		Config configCopy = new Config(redisson.getConfig());
		ConnectionManager connectionManager = ConnectionManager.create(configCopy);
		RedissonObjectBuilder objectBuilder = null;
		if (redisson.getConfig().isReferenceEnabled()) {
			objectBuilder = new RedissonObjectBuilder(redisson);
		}
		return connectionManager.createCommandExecutor(objectBuilder, RedissonObjectBuilder.ReferenceType.DEFAULT);
	}

}
