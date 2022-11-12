package com.livk.admin.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.livk.spring.LivkSpring;
import de.codecentric.boot.admin.server.config.EnableAdminServer;
import de.codecentric.boot.admin.server.domain.values.Registration;
import de.codecentric.boot.admin.server.utils.jackson.RegistrationDeserializer;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

/**
 * <p>
 * SpringServerApp
 * </p>
 *
 * @author livk
 * @date 2022/11/10
 */
@EnableAdminServer
@SpringBootApplication
public class SpringServerApp {
    public static void main(String[] args) {
        LivkSpring.run(SpringServerApp.class, args);
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer customizer() {
        return builder -> {
            SimpleModule simpleModule = new SimpleModule();
            simpleModule.addDeserializer(Registration.class, new RegistrationDeserializer());
            builder.build().registerModule(simpleModule);
        };
    }

    @Bean
    public HttpMessageConverters fastJsonHttpMessageConverters() {
        ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json().build();
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addDeserializer(Registration.class, new RegistrationDeserializer());
        objectMapper.registerModule(simpleModule);
        return new HttpMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper));
    }
}
