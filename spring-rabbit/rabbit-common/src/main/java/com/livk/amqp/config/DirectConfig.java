package com.livk.amqp.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * DirectConfig
 * </p>
 *
 * @author livk
 * @date 2022/4/14
 */
@Configuration
public class DirectConfig {

    public static final String RABBIT_DIRECT_TOPIC = "rabbitDirectTopic";

    public static final String RABBIT_DIRECT_EXCHANGE = "rabbitDirectExchange";

    public static final String RABBIT_DIRECT_BINDING = "rabbitDirectBinding";

    @Bean
    public Queue directQueue() {
        return new Queue(RABBIT_DIRECT_TOPIC, true, false, false);
    }

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(RABBIT_DIRECT_EXCHANGE, true, false);
    }

    @Bean
    public Binding directBinding() {
        return BindingBuilder.bind(directQueue()).to(directExchange()).with(RABBIT_DIRECT_BINDING);
    }

}
