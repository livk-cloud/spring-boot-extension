package com.livk.netty.server.process;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.net.InetSocketAddress;

/**
 * @author livk
 */
@Slf4j
public class NettyServer implements AutoCloseable {

    private final EventLoopGroup boss = new NioEventLoopGroup();

    private final EventLoopGroup work = new NioEventLoopGroup();
    @Value("${spring.netty.server.port}")
    private Integer port;

    public void start() throws InterruptedException {
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(boss, work)
                // 指定Channel
                .channel(NioServerSocketChannel.class)
                //使用指定的端口设置套接字地址
                .localAddress(new InetSocketAddress(port))

                //服务端可连接队列数,对应TCP/IP协议listen函数中backlog参数
                .option(ChannelOption.SO_BACKLOG, 1024)

                //设置TCP长连接,一般如果两个小时内没有数据的通信时,TCP会自动发送一个活动探测数据报文
                .childOption(ChannelOption.SO_KEEPALIVE, true)

                //将小的数据包包装成更大的帧进行传送，提高网络的负载
                .childOption(ChannelOption.TCP_NODELAY, true)

                .childHandler(new NettyServerHandlerInitializer());
        ChannelFuture future = bootstrap.bind().sync();
        if (future.isSuccess()) {
            log.info("启动 Netty Server");
        }
    }

    @Override
    public void close() throws InterruptedException {
        boss.shutdownGracefully().sync();
        work.shutdownGracefully().sync();
    }
}
