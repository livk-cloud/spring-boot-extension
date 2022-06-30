package com.livk.mqtt.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>
 * MqttProperties
 * </p>
 *
 * @author livk
 * @date 2022/3/3
 */
@Data
@ConfigurationProperties(MqttProperties.MQTT)
public class MqttProperties {

    public static final String MQTT = "spring.mqtt";

    private String username;

    private String password;

    private String url;

    private int connectTimeout = 10;

    private int keepAliveInterval = 20;

    private Meta sender;

    private Meta receiver;

    @Data
    public static class Meta {

        private String clientId;

        private String defaultTopic;

    }

}
