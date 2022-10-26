package com.livk.doc.webflux.example.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * SpringDocConfig
 * </p>
 *
 * @author livk
 * @date 2022/10/19
 */
@Configuration
public class SpringDocConfig {

    @Bean
    public GroupedOpenApi openApi() {
        return GroupedOpenApi.builder()
                .group("open-api")
                .packagesToScan("com.livk.doc.webflux.example")
                .pathsToMatch("/**")
                .build();
    }
}
