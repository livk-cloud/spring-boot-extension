package com.livk.http;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.client.reactive.ReactorResourceFactory;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;
import reactor.netty.resources.LoopResources;

import java.util.function.Function;

/**
 * <p>
 * WebClientConfig
 * </p>
 *
 * @author livk
 * @date 2022/5/9
 */
@AutoConfiguration
public class WebClientConfiguration {

    /**
     * spring官方建议使用{@link WebClient} <a href=
     * "https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#webmvc-client">Spring文档</a>
     *
     * @return WebClient
     */
    @Bean
    @ConditionalOnClass(WebClient.class)
    @ConditionalOnMissingBean
    public WebClient webClient() {
        ReactorResourceFactory factory = new ReactorResourceFactory();
        factory.setUseGlobalResources(false);
        factory.setConnectionProvider(ConnectionProvider.create("webClient", 50));
        factory.setLoopResources(LoopResources.create("webClient", 50, true));
        Function<HttpClient, HttpClient> function = httpClient ->
                httpClient.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10)
                        .doOnConnected(connection ->
                                connection.addHandlerLast(new ReadTimeoutHandler(10))
                                        .addHandlerLast(new WriteTimeoutHandler(10)));
        ReactorClientHttpConnector connector = new ReactorClientHttpConnector(factory, function);
        return WebClient.builder().clientConnector(connector).build();
    }
}
