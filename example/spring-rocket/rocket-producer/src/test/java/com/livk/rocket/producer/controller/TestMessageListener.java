package com.livk.rocket.producer.controller;

import com.livk.rocket.constant.RocketConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * @author livk
 */
@Slf4j
@Component
@RocketMQMessageListener(consumerGroup = "livk-consumer-group", topic = RocketConstant.LIVK_MESSAGE_TOPIC)
public class TestMessageListener implements RocketMQListener<String> {

    @Override
    public void onMessage(String message) {
        log.info("rocketMQ receive message:{}", message);
    }
}
