package com.livk.proto.amqp.controller;

import com.livk.proto.User;
import com.livk.proto.amqp.config.AmqpConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author livk
 */
@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {

    private final RabbitTemplate rabbitTemplate;

    @GetMapping("send")
    public void send() {
        ThreadLocalRandom current = ThreadLocalRandom.current();
        for (int i = 0; i < 10; i++) {
            User user = new User(100L + i, "amqp", "serializer@amqp.com", current.nextInt(0, 2));
            rabbitTemplate.convertAndSend(AmqpConfig.TOPIC_NAME, user);
        }
    }
}
