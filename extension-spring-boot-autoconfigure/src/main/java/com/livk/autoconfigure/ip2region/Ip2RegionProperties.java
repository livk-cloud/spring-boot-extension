package com.livk.autoconfigure.ip2region;

import com.livk.commons.io.ResourceUtils;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;

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

    /**
     * The constant PREFIX.
     */
    public static final String PREFIX = "ip2region";

    private Boolean enabled = false;

    private String filePath = "classpath:ip/ip2region.xdb";

    /**
     * Get file resource resource [ ].
     *
     * @return the resource [ ]
     */
    public Resource getFileResource() {
        return ResourceUtils.getResource(filePath);
    }
}
