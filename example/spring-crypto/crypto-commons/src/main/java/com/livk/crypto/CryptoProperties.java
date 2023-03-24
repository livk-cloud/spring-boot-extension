package com.livk.crypto;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * @author livk
 */
@Data
@ConfigurationProperties(CryptoProperties.PREFIX)
public class CryptoProperties {

    public static final String PREFIX = "spring.crypto";

    private CryptoType type;

    private Map<String, String> metadata;
}
