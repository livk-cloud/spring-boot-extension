package com.livk.http;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 * HttpConfiguration
 * </p>
 *
 * @author livk
 * @date 2022/6/7
 */
@Configuration(proxyBeanMethods = false)
public class RestTemplateConfiguration {

    @Bean
    @ConditionalOnMissingBean({OkHttpClient.class, RestTemplate.class})
    public RestTemplate restTemplate() {
        ConnectionPool pool = new ConnectionPool(200, 300, TimeUnit.SECONDS);
        OkHttpClient httpClient = new OkHttpClient().newBuilder().connectionPool(pool)
                .connectTimeout(3, TimeUnit.SECONDS).readTimeout(3, TimeUnit.SECONDS).writeTimeout(3, TimeUnit.SECONDS)
                .hostnameVerifier((s, sslSession) -> true).build();
        return getRestTemplate(httpClient);
    }

    @Bean
    @ConditionalOnBean(OkHttpClient.class)
    @ConditionalOnMissingBean
    public RestTemplate restTemplate(OkHttpClient okHttpClient) {
        return getRestTemplate(okHttpClient);
    }

    private RestTemplate getRestTemplate(OkHttpClient okHttpClient) {
        return new RestTemplate(new OkHttp3ClientHttpRequestFactory(okHttpClient));
    }

}
