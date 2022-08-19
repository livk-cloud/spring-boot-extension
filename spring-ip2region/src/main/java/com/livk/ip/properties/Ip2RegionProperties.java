package com.livk.ip.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

/**
 * <p>
 * Ip2RegionProperties
 * </p>
 *
 * @author livk
 * @date 2022/8/18
 */
@Data
@ConfigurationProperties(Ip2RegionProperties.PREFIX)
public class Ip2RegionProperties {

    private static final ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
    public static final String PREFIX = "ip2region";

    private Boolean enabled = false;

    private String filePath = "classpath:/ip/ip2region.xdb";

    public Resource getFileResource() {
        return resourceResolver.getResource(filePath);
    }
}
