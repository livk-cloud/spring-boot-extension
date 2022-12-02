package com.livk.autoconfigure.openapi;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;

/**
 * <p>
 * OpenAPIAutoConfiguration
 * </p>
 *
 * @author livk
 * @date 2022/12/2
 */
@AutoConfiguration
@ConditionalOnBean(GroupedOpenApi.class)
public class OpenAPIAutoConfiguration {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info().title("spring doc webmvc api")
                        .description("spring doc webmvc api")
                        .version("1.0.0"));
    }
}
