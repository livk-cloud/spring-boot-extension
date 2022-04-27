package com.livk.pulsar.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.Consumer;
import org.apache.pulsar.client.api.PulsarClientException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * Consumer
 * </p>
 *
 * @author livk
 * @date 2022/4/27
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MessageConsumer implements Runnable, InitializingBean {

    private final Consumer<String> consumer;

    @Override
    public void run() {
        while (true) {
            try {
                var message = consumer.receive();
                var key = message.getKey();
                var data = new String(message.getData(), StandardCharsets.UTF_8);
                var topic = message.getTopicName();
                log.info("topic:{} key:{} data:{}", topic, key, data);
                consumer.acknowledge(message);
            } catch (PulsarClientException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void afterPropertiesSet() {
        var executor = new ThreadPoolExecutor(5, 10, 0,
                TimeUnit.SECONDS, new ArrayBlockingQueue<>(10));
        executor.execute(this);
    }
}
