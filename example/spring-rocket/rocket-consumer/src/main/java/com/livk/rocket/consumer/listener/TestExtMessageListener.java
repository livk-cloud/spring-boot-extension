package com.livk.rocket.consumer.listener;

import com.livk.rocket.constant.RocketConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * @author laokou
 */
@Slf4j
@Component
@RocketMQMessageListener(consumerGroup = "livk-consumer-group-2", topic = RocketConstant.LIVK_EXT_MESSAGE_TOPIC)
public class TestExtMessageListener implements RocketMQListener<String> {

    @Override
    public void onMessage(String message) {
        log.info("ext rocketMQ receive message:{}", message);
    }
}
