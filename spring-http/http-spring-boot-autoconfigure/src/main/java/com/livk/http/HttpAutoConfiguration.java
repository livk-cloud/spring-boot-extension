package com.livk.http;

import com.livk.http.factory.HttpServiceRegistrar;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

/**
 * <p>
 * HttpAutoConfiguration
 * </p>
 *
 * @author livk
 * @date 2022/5/20
 */
//@Import(HttpServiceRegistrar.class)
@AutoConfiguration
@ConditionalOnClass(WebClient.class)
public class HttpAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public WebClient webClient() {
        return WebClient.builder().build();
    }


    /**
     * 由于{@link HttpServiceProxyFactory#afterPropertiesSet()}被废弃 {@link }不能加入到IOC容器，不然会走afterPropertiesSet，导致报错
     */
    @Bean
    public HttpServiceRegistrar httpServiceRegistrar(WebClient webClient) {
        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory.builder(WebClientAdapter.forClient(webClient)).build();
        return new HttpServiceRegistrar(httpServiceProxyFactory);
    }
}
