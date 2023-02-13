package com.livk.autoconfigure.oss;

import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.Assert;

/**
 * The type Oss properties.
 *
 * @author livk
 */
@Data
@ConfigurationProperties(OSSProperties.PREFIX)
public class OSSProperties implements InitializingBean {
    /**
     * The constant PREFIX.
     */
    public static final String PREFIX = "spring.oss";

    private static final String TEMPLATE = "%s://%s:%d";

    private String host;

    private Integer port;

    private String accessKey;

    private String secretKey;

    private boolean ssl = false;

    /**
     * Endpoint string.
     *
     * @return the string
     */
    public String endpoint() {
        String http = ssl ? "https" : "http";
        return String.format(TEMPLATE, http, host, port);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.hasText(host, "host not be blank");
        Assert.notNull(port, "port not be null");
        Assert.hasText(accessKey, "accessKey not be blank");
        Assert.hasText(secretKey, "secretKey not be blank");
    }
}
