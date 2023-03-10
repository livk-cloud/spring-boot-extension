package com.livk.amqp.config;

import com.livk.auto.service.annotation.SpringAutoService;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.HeadersExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * HeadersConfig
 * </p>
 *
 * @author livk
 */
@Configuration
@SpringAutoService
public class HeadersConfig {

    /**
     * HEADERS_EXCHANGE交换机名称
     */
    public static final String HEADERS_EXCHANGE_NAME = "headers.exchange.name";

    /**
     * RabbitMQ的HEADERS_EXCHANGE交换机的队列A的名称
     */
    public static final String HEADERS_EXCHANGE_QUEUE_A = "headers.queue.a";

    /**
     * RabbitMQ的HEADERS_EXCHANGE交换机的队列B的名称
     */
    public static final String HEADERS_EXCHANGE_QUEUE_B = "headers.queue.b";

    @Bean
    public Queue headersQueueA() {
        return new Queue(HEADERS_EXCHANGE_QUEUE_A, true, false, false);
    }

    @Bean
    public Queue headersQueueB() {
        return new Queue(HEADERS_EXCHANGE_QUEUE_B, true, false, false);
    }

    @Bean
    public HeadersExchange rabbitmqDemoHeadersExchange() {
        return new HeadersExchange(HEADERS_EXCHANGE_NAME, true, false);
    }

    @Bean
    public Binding bindHeadersA() {
        Map<String, Object> map = new HashMap<>();
        map.put("username", "livk");
        map.put("password", "livk");
        // 全匹配
        return BindingBuilder.bind(headersQueueA()).to(rabbitmqDemoHeadersExchange()).whereAll(map).match();
    }

    @Bean
    public Binding bindHeadersB() {
        Map<String, Object> map = new HashMap<>();
        map.put("code", "livk");
        map.put("auth", "livk");
        // 部分匹配
        return BindingBuilder.bind(headersQueueB()).to(rabbitmqDemoHeadersExchange()).whereAny(map).match();
    }

}
