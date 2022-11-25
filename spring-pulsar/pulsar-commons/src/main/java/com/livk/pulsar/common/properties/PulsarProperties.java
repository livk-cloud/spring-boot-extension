package com.livk.pulsar.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>
 * PulsarProperties
 * </p>
 *
 * @author livk
 * @date 2022/4/27
 */
@Data
@ConfigurationProperties(PulsarProperties.PULSAR_PREFIX)
public class PulsarProperties {

    public static final String PULSAR_PREFIX = "spring.pulsar";
    /**
     * Pulsar IP
     */
    private String address;
    /**
     * topic
     */
    private String topic;

}
