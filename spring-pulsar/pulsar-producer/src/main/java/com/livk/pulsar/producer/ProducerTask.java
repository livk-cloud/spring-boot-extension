package com.livk.pulsar.producer;

import lombok.RequiredArgsConstructor;
import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.Schema;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * <p>
 * ProducerTask
 * </p>
 *
 * @author livk
 * @date 2022/4/27
 */
@Component
@RequiredArgsConstructor
public class ProducerTask {

    private final Producer<String> producer;

    @Scheduled(cron = "0/5 * * * * ?")
    public void send() {
        producer.newMessage(Schema.STRING).key(UUID.randomUUID().toString().substring(0, 5))
                .value(UUID.randomUUID().toString()).sendAsync().handle((messageId, throwable) -> throwable == null)
                .join();
    }

}
