package com.livk.http;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * <p>
 * WebClientConfig
 * </p>
 *
 * @author livk
 * @date 2022/5/9
 */
@Configuration
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
        return WebClient.create();
    }

}
