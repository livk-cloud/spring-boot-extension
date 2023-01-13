package com.livk.autoconfigure.ip2region;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;

/**
 * <p>
 * Ip2RegionProperties
 * </p>
 *
 * @author livk
 */
@Data
@ConfigurationProperties(Ip2RegionProperties.PREFIX)
public class Ip2RegionProperties {

    private static final ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
    /**
     * The constant PREFIX.
     */
    public static final String PREFIX = "ip2region";

    private Boolean enabled = false;

    private String filePath = "classpath*:ip/ip2region.xdb";

    /**
     * Get file resource resource [ ].
     *
     * @return the resource [ ]
     * @throws IOException the io exception
     */
    public Resource[] getFileResource() throws IOException {
        return resourceResolver.getResources(filePath);
    }
}
