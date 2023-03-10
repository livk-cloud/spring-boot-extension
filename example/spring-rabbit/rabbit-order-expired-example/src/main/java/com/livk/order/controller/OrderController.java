package com.livk.order.controller;

import com.livk.order.config.RabbitConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

/**
 * <p>
 * OrderController
 * </p>
 *
 * @author livk
 */
@Slf4j
@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final AmqpTemplate rabbitTemplate;

    /**
     * 模拟提交订单
     *
     * @return java.lang.Object
     * @author nxq
     */
    @GetMapping
    public HttpEntity<Map<String, String>> submit() {
        String orderId = UUID.randomUUID().toString();
        log.info("submit order {}", orderId);
        this.rabbitTemplate.convertAndSend(RabbitConfig.orderExchange, RabbitConfig.routingKeyOrder, orderId);
        return ResponseEntity.ok(Map.of("orderId", orderId));
    }

}
