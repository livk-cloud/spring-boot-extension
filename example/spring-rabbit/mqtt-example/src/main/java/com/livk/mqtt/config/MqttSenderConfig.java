package com.livk.mqtt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

/**
 * <p>
 * MqttSenderConfig
 * </p>
 *
 * @author livk
 */
@Configuration
public class MqttSenderConfig {

    public static final String CHANNEL_NAME_OUT = "mqttOutboundChannel";

    /**
     * MQTT信息通道（生产者）
     */
    @Bean(name = CHANNEL_NAME_OUT)
    public MessageChannel mqttOutboundChannel() {
        return new DirectChannel();
    }

    /**
     * MQTT消息处理器（生产者）
     */
    @Bean
    @ServiceActivator(inputChannel = CHANNEL_NAME_OUT)
    public MessageHandler mqttOutbound(MqttPahoClientFactory mqttPahoClientFactory, MqttProperties mqttProperties) {
        MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(mqttProperties.getSender().getClientId(),
                mqttPahoClientFactory);
        messageHandler.setAsync(true);
        messageHandler.setDefaultTopic(mqttProperties.getSender().getDefaultTopic());
        return messageHandler;
    }

}
