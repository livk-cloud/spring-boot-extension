package com.livk.crypto;

import com.livk.auto.service.annotation.SpringAutoService;
import com.livk.crypto.parse.CryptoFormatter;
import com.livk.crypto.parse.SpringAnnotationFormatterFactory;
import com.livk.crypto.support.AesSecurity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author livk
 */
@SpringAutoService
@AutoConfiguration
public class CryptoAutoConfiguration {

    @Bean
    public AesSecurity aesSecurity() {
        return new AesSecurity();
    }

    @AutoConfiguration
    @RequiredArgsConstructor
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    public static class CryptoMvcAutoConfiguration implements WebMvcConfigurer {

        private final ObjectProvider<CryptoFormatter<?>> cryptoFormatters;

        @Override
        public void addFormatters(FormatterRegistry registry) {
            registry.addFormatterForFieldAnnotation(new SpringAnnotationFormatterFactory(cryptoFormatters));
        }
    }

    @AutoConfiguration
    @RequiredArgsConstructor
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
    public static class CryptoWebfluxAutoConfiguration implements WebFluxConfigurer {

        private final ObjectProvider<CryptoFormatter<?>> cryptoFormatters;

        @Override
        public void addFormatters(FormatterRegistry registry) {
            registry.addFormatterForFieldAnnotation(new SpringAnnotationFormatterFactory(cryptoFormatters));
        }
    }
}
