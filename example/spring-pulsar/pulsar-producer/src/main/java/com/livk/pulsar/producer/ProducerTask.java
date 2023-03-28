package com.livk.pulsar.producer;

import com.livk.commons.util.Snowflake;
import com.livk.pulsar.common.entity.PulsarMessage;
import lombok.RequiredArgsConstructor;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.client.api.Schema;
import org.springframework.pulsar.core.PulsarTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * <p>
 * ProducerTask
 * </p>
 *
 * @author livk
 */
@Component
@RequiredArgsConstructor
public class ProducerTask {

    private static final Snowflake SNOWFLAKE = new Snowflake();
    private final PulsarTemplate<String> pulsarTemplate;

    @Scheduled(cron = "0/5 * * * * ?")
    public void send() throws PulsarClientException {
        PulsarMessage<String> message = new PulsarMessage<>();
        message.setMsgId(UUID.randomUUID().toString());
        message.setSendTime(LocalDateTime.now());
        message.setData(SNOWFLAKE.nextId() + "");

        pulsarTemplate.newMessage(message.toJson())
                .withSchema(Schema.STRING)
                .withMessageCustomizer(builder -> builder.key(UUID.randomUUID().toString().substring(0, 5)))
                .sendAsync()
                .handle((messageId, throwable) -> throwable == null)
                .join();
    }

}
