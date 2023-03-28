package com.livk.pulsar.producer.controller;

import com.livk.commons.test.TestLogUtils;
import org.apache.pulsar.client.api.Message;
import org.springframework.pulsar.annotation.PulsarListener;
import org.springframework.stereotype.Component;

/**
 * <p>
 * MessageConsumerTest
 * </p>
 *
 * @author livk
 */
@Component
public class MessageConsumerTest {

    @PulsarListener
    public void receive(Message<String> message) {
        String key = message.getKey();
        String data = message.getValue();
        String topic = message.getTopicName();
        TestLogUtils.info("topic:{} key:{} data:{}", topic, key, data);
    }
}
