package com.livk.zookeeper.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * <p>
 * ZookeeperConfig
 * </p>
 *
 * @author livk
 * @date 2022/4/6
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(ZookeeperProperties.class)
public class ZookeeperConfig {

    @Bean
    public ZooKeeper zooKeeper(ZookeeperProperties properties) throws IOException {
        return new ZooKeeper(properties.getAddress() + ":" + properties.getPort(), properties.getTimeout(),
                event -> log.info("status:{}", event.getState()));
    }

}
