package com.livk.autoconfigure.http;

import com.livk.auto.service.annotation.SpringAutoService;
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
 */
@AutoConfiguration
@SpringAutoService
@ConditionalOnClass(value = WebClient.class, name = "com.livk.http.marker.HttpMarker")
public class HttpAutoConfiguration {

    /**
     * Web client web client.
     *
     * @param builder the builder
     * @return the web client
     */
    @Bean
    @ConditionalOnMissingBean
    public WebClient webClient(WebClient.Builder builder) {
        return builder.build();
    }

    /**
     * Http service proxy factory http service proxy factory.
     *
     * @param webClient   the web client
     * @param customizers the customizers
     * @return the http service proxy factory
     */
    @Bean
    public HttpServiceProxyFactory httpServiceProxyFactory(WebClient webClient,
                                                           ObjectProvider<HttpServiceProxyFactoryCustomizer> customizers) {
        HttpServiceProxyFactory.Builder builder = HttpServiceProxyFactory.builder(WebClientAdapter.forClient(webClient));
        customizers.orderedStream().forEach(customizer -> customizer.customize(builder));
        return builder.build();
    }

    /**
     * Http service registrar http service registrar.
     *
     * @param httpServiceProxyFactory the http service proxy factory
     * @return the http service registrar
     */
    @Bean
    public HttpServiceRegistrar httpServiceRegistrar(HttpServiceProxyFactory httpServiceProxyFactory) {
        return new HttpServiceRegistrar(httpServiceProxyFactory);
    }
}
