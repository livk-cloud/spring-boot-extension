package com.livk.commons.jackson;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.livk.auto.service.annotation.SpringAutoService;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.Locale;

/**
 * <p>
 * JacksonConfiguration
 * </p>
 *
 * @author livk
 */
@SpringAutoService
@AutoConfiguration(before = JacksonAutoConfiguration.class)
public class JacksonConfiguration {

    /**
     * Java time customizer jackson 2 object mapper builder customizer.
     *
     * @return the jackson 2 object mapper builder customizer
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer javaTimeCustomizer() {
        return builder -> {
            builder.locale(Locale.CHINA);
            builder.modules(new JavaTimeModule());
        };
    }
}
