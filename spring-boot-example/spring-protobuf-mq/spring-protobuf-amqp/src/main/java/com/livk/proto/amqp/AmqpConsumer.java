package com.livk.proto.amqp;

import com.livk.proto.ConsumerCheck;
import com.livk.proto.User;
import com.livk.proto.amqp.config.AmqpConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * @author livk
 */
@Slf4j
@Component
public class AmqpConsumer {

    @RabbitListener(queuesToDeclare = @Queue(AmqpConfig.TOPIC_NAME))
    public void consumer(@Payload User user) {
        log.info(" : {}", user);
		ConsumerCheck.success();
    }
}
