package com.livk.admin.server.converter;

import de.codecentric.boot.admin.server.config.AdminServerProperties;
import de.codecentric.boot.admin.server.utils.jackson.AdminServerModule;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;

/**
 * <p>
 * RegistrationHttpMessageConverters
 * </p>
 *
 * @author livk
 */
@Component
public class RegistrationHttpMessageConverters extends HttpMessageConverters {

    public RegistrationHttpMessageConverters(AdminServerProperties adminServerProperties) {
        super(jacksonHttpMessageConverter(adminServerProperties));
    }

    private static HttpMessageConverter<?> jacksonHttpMessageConverter(AdminServerProperties adminServerProperties) {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.getObjectMapper().registerModule(new AdminServerModule(adminServerProperties.getMetadataKeysToSanitize()));
        return converter;
    }
}
