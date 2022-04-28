package com.livk.mqtt.config;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;

/**
 * <p>
 * MqttConfig
 * </p>
 *
 * @author livk
 * @date 2022/3/3
 */
@Configuration
@EnableConfigurationProperties(MqttProperties.class)
public class MqttConfig {
    @Bean
    public MqttConnectOptions mqttConnectOptions(MqttProperties properties) {
        var options = new MqttConnectOptions();
        options.setUserName(properties.getUsername());
        options.setPassword(properties.getPassword().toCharArray());
        options.setServerURIs(properties.getUrl().split(";"));
        options.setConnectionTimeout(properties.getConnectTimeout());
        options.setKeepAliveInterval(properties.getKeepAliveInterval());
        return options;
    }

    @Bean
    public MqttPahoClientFactory mqttPahoClientFactory(MqttConnectOptions mqttConnectOptions) {
        var factory = new DefaultMqttPahoClientFactory();
        factory.setConnectionOptions(mqttConnectOptions);
        return factory;
    }
}
