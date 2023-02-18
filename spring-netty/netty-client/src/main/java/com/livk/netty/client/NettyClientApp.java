package com.livk.netty.client;

import com.livk.commons.spring.SpringLauncher;
import com.livk.netty.client.process.NettyClient;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @author livk
 */
@SpringBootApplication
public class NettyClientApp {
    public static void main(String[] args) {
        SpringLauncher.run(NettyClientApp.class, args);
    }

    @Bean(initMethod = "start", destroyMethod = "close")
    public NettyClient nettyClient() {
        return new NettyClient();
    }
}
