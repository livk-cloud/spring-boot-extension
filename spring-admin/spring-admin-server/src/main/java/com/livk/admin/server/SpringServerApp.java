package com.livk.admin.server;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.livk.spring.LivkSpring;
import de.codecentric.boot.admin.server.config.EnableAdminServer;
import de.codecentric.boot.admin.server.domain.values.Registration;
import de.codecentric.boot.admin.server.utils.jackson.RegistrationDeserializer;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
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
    public HttpMessageConverters fastJsonHttpMessageConverters() {
        return new HttpMessageConverters(new RegistrationHttpMessageConverters());
    }

    public static class RegistrationHttpMessageConverters extends MappingJackson2HttpMessageConverter {
        public RegistrationHttpMessageConverters() {
            SimpleModule simpleModule = new SimpleModule();
            simpleModule.addDeserializer(Registration.class, new RegistrationDeserializer());
            getObjectMapper().registerModule(simpleModule);
        }
    }
}
