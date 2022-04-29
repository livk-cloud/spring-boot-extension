package com.livk.redis.listener;

import com.livk.common.redis.supprot.LivkRedisTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * LivkStreamListener
 * </p>
 *
 * @author livk
 * @date 2022/4/7
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LivkStreamListener
		implements StreamListener<String, ObjectRecord<String, String>>, InitializingBean, DisposableBean {

	private final LivkRedisTemplate livkRedisTemplate;

	private StreamMessageListenerContainer<String, ObjectRecord<String, String>> listenerContainer;

	@Override
	public void onMessage(ObjectRecord<String, String> message) {
		log.info("id:{}", message.getId().getSequence());
		log.info("value:{}", message.getValue());
		log.info("stream:{}", message.getStream());
	}

	@Override
	public void afterPropertiesSet() {
		if (Boolean.TRUE.equals(livkRedisTemplate.hasKey("livk-streamKey"))) {
			var groups = livkRedisTemplate.opsForStream().groups("livk-group");
			if (groups.isEmpty()) {
				livkRedisTemplate.opsForStream().createGroup("livk-streamKey", "livk-group");
			}
		}
		else {
			livkRedisTemplate.opsForStream().createGroup("livk-streamKey", "livk-group");
		}

		var options = StreamMessageListenerContainer.StreamMessageListenerContainerOptions.builder().batchSize(10)
				.executor(new ThreadPoolExecutor(4, 10, 0, TimeUnit.SECONDS, new ArrayBlockingQueue<>(10)))
				.errorHandler(t -> log.error("ERROR:{}", t.getMessage())).pollTimeout(Duration.ZERO)
				.serializer(RedisSerializer.string()).targetType(String.class).build();
		this.listenerContainer = StreamMessageListenerContainer
				.create(Objects.requireNonNull(livkRedisTemplate.getConnectionFactory()), options);

		this.listenerContainer.receive(Consumer.from("livk-group", "livk"),
				StreamOffset.create("livk-streamKey", ReadOffset.lastConsumed()), this);
		this.listenerContainer.start();
	}

	@Override
	public void destroy() {
		this.listenerContainer.stop();
	}

}
