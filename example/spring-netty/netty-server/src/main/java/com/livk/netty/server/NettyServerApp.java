package com.livk.netty.server;

import com.livk.commons.spring.SpringLauncher;
import com.livk.netty.server.process.NettyServer;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @author livk
 */
@SpringBootApplication
public class NettyServerApp {
    public static void main(String[] args) {
        SpringLauncher.run(NettyServerApp.class, args);
    }

    @Bean(initMethod = "start", destroyMethod = "close")
    public NettyServer nettyServer() {
        return new NettyServer();
    }
}
