package com.livk.socket.config;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * <p>
 * WebSocketConfig
 * </p>
 *
 * @author livk
 * @date 2022/2/14
 */
@Configuration
public class WebSocketConfig {

    @PostConstruct
    public void init() {
        System.out.println(WebSocketConfig.class);
    }

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}
