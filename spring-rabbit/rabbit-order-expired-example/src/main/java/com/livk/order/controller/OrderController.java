package com.livk.order.controller;

import com.livk.order.config.RabbitConfig;
import com.livk.util.LogUtils;
import lombok.RequiredArgsConstructor;
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
 * @date 2022/10/13
 */
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
        LogUtils.info("submit order {}", orderId);
        this.rabbitTemplate.convertAndSend(RabbitConfig.orderExchange, RabbitConfig.routingKeyOrder, orderId);
        return ResponseEntity.ok(Map.of("orderId", orderId));
    }

}
