package com.livk.autoconfigure.http;

import com.livk.autoconfigure.http.customizer.HttpServiceProxyFactoryCustomizer;
import com.livk.autoconfigure.http.factory.HttpServiceRegistrar;
import org.springframework.beans.factory.ObjectProvider;
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
@AutoConfiguration
@ConditionalOnClass(WebClient.class)
public class HttpAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public WebClient webClient(WebClient.Builder builder) {
        return builder.build();
    }

    @Bean
    public HttpServiceProxyFactory httpServiceProxyFactory(WebClient webClient,
                                                           ObjectProvider<HttpServiceProxyFactoryCustomizer> customizers) {
        HttpServiceProxyFactory.Builder builder = HttpServiceProxyFactory.builder(WebClientAdapter.forClient(webClient));
        customizers.orderedStream().forEach(customizer -> customizer.customize(builder));
        return builder.build();
    }


    @Bean
    public HttpServiceRegistrar httpServiceRegistrar(HttpServiceProxyFactory httpServiceProxyFactory) {
        return new HttpServiceRegistrar(httpServiceProxyFactory);
    }
}
