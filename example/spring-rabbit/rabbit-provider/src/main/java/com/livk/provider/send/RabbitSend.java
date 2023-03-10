package com.livk.provider.send;

import com.livk.amqp.config.DirectConfig;
import com.livk.amqp.config.FanoutConfig;
import com.livk.amqp.config.HeadersConfig;
import com.livk.amqp.config.TopicConfig;
import com.livk.amqp.entity.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * <p>
 * RabbitSend
 * </p>
 *
 * @author livk
 */
@Component
@RequiredArgsConstructor
public class RabbitSend {

    private final RabbitTemplate rabbitTemplate;

    public <T> void sendMsgDirect(Message<T> message) {
        message.setMsgId(UUID.randomUUID().toString());
        message.setSendTime(LocalDateTime.now());
        rabbitTemplate.convertAndSend(DirectConfig.RABBIT_DIRECT_EXCHANGE, DirectConfig.RABBIT_DIRECT_BINDING, message);
    }

    public <T> void sendMsgFanout(Message<T> message) {
        message.setMsgId(UUID.randomUUID().toString());
        message.setSendTime(LocalDateTime.now());
        rabbitTemplate.convertAndSend(FanoutConfig.FANOUT_EXCHANGE_DEMO_NAME, "", message);
    }

    public <T> void sendMsgTopic(Message<T> message, String routingKey) {
        message.setMsgId(UUID.randomUUID().toString());
        message.setSendTime(LocalDateTime.now());
        rabbitTemplate.convertAndSend(TopicConfig.TOPIC_EXCHANGE_NAME, routingKey, message);
    }

    public <T> void sendMsgHeaders(Message<T> message, Map<String, Object> map) {
        message.setMsgId(UUID.randomUUID().toString());
        message.setSendTime(LocalDateTime.now());
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
        messageProperties.setContentType("UTF-8");
        messageProperties.getHeaders().putAll(map);
        org.springframework.amqp.core.Message msg = new org.springframework.amqp.core.Message(
                message.toString().getBytes(), messageProperties);
        rabbitTemplate.convertAndSend(HeadersConfig.HEADERS_EXCHANGE_NAME, null, msg);
    }

}
