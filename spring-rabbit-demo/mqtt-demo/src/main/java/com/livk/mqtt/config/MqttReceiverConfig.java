package com.livk.mqtt.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import java.util.Objects;

/**
 * <p>
 * MqttReceiverConfig
 * </p>
 *
 * @author livk
 * @date 2022/3/2
 */
@Slf4j
@Configuration
public class MqttReceiverConfig {
    public static final String CHANNEL_NAME_IN = "mqttInboundChannel";

    /**
     * MQTT信息通道（消费者）
     */
    @Bean(name = CHANNEL_NAME_IN)
    public MessageChannel mqttInboundChannel() {
        return new DirectChannel();
    }


    /**
     * MQTT消息订阅绑定（消费者）
     */
    @Bean
    public MessageProducer inbound(MqttPahoClientFactory factory,
                                   MqttProperties mqttProperties) {
        // 可以同时消费（订阅）多个Topic
        var adapter = new MqttPahoMessageDrivenChannelAdapter(mqttProperties.getReceiver().getClientId(),
                factory, mqttProperties.getReceiver().getDefaultTopic());
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        // 设置订阅通道
        adapter.setOutputChannel(mqttInboundChannel());
        return adapter;
    }

    /**
     * MQTT消息处理器（消费者）
     */
    @Bean
    @ServiceActivator(inputChannel = CHANNEL_NAME_IN)
    public MessageHandler handler() {
        return message -> {
            String topic = Objects.requireNonNull(message.getHeaders().get("mqtt_receivedTopic")).toString();
            String msg = message.getPayload().toString();
            log.info("接收到订阅消息:\ttopic:{}\tmessage:{}", topic, msg);
        };
    }
}
