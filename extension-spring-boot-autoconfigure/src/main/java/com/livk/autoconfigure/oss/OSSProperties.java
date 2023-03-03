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

    private String url;

    private String accessKey;

    private String secretKey;

    /**
     * Endpoint string.
     *
     * @return the string
     */
    public String endpoint() {
        return url.replaceAll(getPrefix() + ":", "");
    }

    /**
     * Gets prefix.
     *
     * @return the prefix
     */
    public String getPrefix() {
        int index = url.indexOf(":");
        if (index != -1) {
            return url.substring(0, index);
        }
        throw new RuntimeException("url缺少前缀!");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.hasText(url, "url not be blank");
        Assert.hasText(accessKey, "accessKey not be blank");
        Assert.hasText(secretKey, "secretKey not be blank");
    }
}
