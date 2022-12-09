package com.livk.boot.consumer.listener;

import com.livk.boot.consumer.constant.RocketConstant;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * @author Kou Shenhai
 */
@Component
@RocketMQMessageListener(consumerGroup = "livk-consumer-group", topic = RocketConstant.LIVK_MESSAGE_TOPIC)
public class TestMessageListener implements RocketMQListener<String> {

    @Override
    public void onMessage(String message) {
        System.out.println(message);
    }
}