package com.livk.boot.producer.config;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Kou Shenhai
 */
@Configuration
public class RocketConfig {

    @Value("${rocketmq.producer.group:livk-producer-group}")
    private String producerGroup;

    @Value("${rocketmq.name-server:192.168.62.128:9876}")
    private String nameServer;

    @Bean(name = "rocketMQTemplate")
    public RocketMQTemplate rocketMQTemplate() {
        RocketMQTemplate rocketMQTemplate = new RocketMQTemplate();
        DefaultMQProducer defaultMQProducer = new DefaultMQProducer();
        defaultMQProducer.setProducerGroup(producerGroup);
        defaultMQProducer.setNamesrvAddr(nameServer);
        rocketMQTemplate.setProducer(defaultMQProducer);
        return rocketMQTemplate;
    }

}
