package com.livk.rocket.consumer.listener;

import com.livk.commons.jackson.JacksonUtils;
import com.livk.rocket.constant.RocketConstant;
import com.livk.rocket.dto.RocketDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * @author laokou
 */
@Slf4j
@Component
@RocketMQMessageListener(consumerGroup = "livk-consumer-group", topic = RocketConstant.LIVK_MESSAGE_TOPIC)
public class TestMessageListener implements RocketMQListener<RocketDTO> {

    @Override
    public void onMessage(RocketDTO dto) {
        if (new Random(20).nextInt() % 20 == 0) {
            throw new RuntimeException("消息重试");
        }
        log.info("rocketMQ receive message：{}", JacksonUtils.writeValueAsString(dto));
    }
}
