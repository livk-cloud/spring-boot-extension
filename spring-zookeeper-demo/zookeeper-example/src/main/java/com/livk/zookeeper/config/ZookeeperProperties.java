package com.livk.zookeeper.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>
 * ZookeeperProperties
 * </p>
 *
 * @author livk
 * @date 2022/4/6
 */
@Data
@ConfigurationProperties(ZookeeperProperties.PREFIX)
public class ZookeeperProperties {

    public static final String PREFIX = "spring.zookeeper";

    private String address = "localhost";

    private int port = 2181;

    private int timeout = 4000;

}
