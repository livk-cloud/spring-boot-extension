package com.livk.doc.webflux.example.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
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

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info().title("spring doc webmvc api")
                        .description("spring doc webmvc api")
                        .version("1.0.0"));
    }
}
