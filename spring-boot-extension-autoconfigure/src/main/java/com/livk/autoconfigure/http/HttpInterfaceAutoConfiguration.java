/*
 * Copyright 2021 spring-boot-extension the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.livk.autoconfigure.http;

import com.livk.auto.service.annotation.SpringAutoService;
import com.livk.autoconfigure.http.customizer.HttpServiceProxyFactoryCustomizer;
import com.livk.autoconfigure.http.factory.HttpServiceRegistrar;
import com.livk.commons.http.WebClientConfiguration;
import com.livk.commons.http.annotation.EnableWebClient;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
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
@EnableWebClient
@AutoConfiguration(after = WebClientConfiguration.class)
@SpringAutoService
@Import(HttpServiceRegistrar.class)
@ConditionalOnClass(value = WebClient.class, name = "com.livk.http.marker.HttpMarker")
public class HttpInterfaceAutoConfiguration {

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
}
