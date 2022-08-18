package com.livk.ip.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

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

    public static final String PREFIX = "ip2region";

    private Boolean enabled = false;

    private String filePath = "/ip/ip2region.xdb";
}
