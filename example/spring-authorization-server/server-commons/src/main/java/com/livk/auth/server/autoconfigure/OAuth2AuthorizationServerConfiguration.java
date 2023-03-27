package com.livk.auth.server.autoconfigure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;

/**
 * @author livk
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(OAuth2AuthorizationServerProperties.class)
class OAuth2AuthorizationServerConfiguration {

    private final OAuth2AuthorizationServerPropertiesMapper propertiesMapper;

    OAuth2AuthorizationServerConfiguration(OAuth2AuthorizationServerProperties properties) {
        this.propertiesMapper = new OAuth2AuthorizationServerPropertiesMapper(properties);
    }

    @Bean
    @ConditionalOnMissingBean
    AuthorizationServerSettings authorizationServerSettings() {
        return this.propertiesMapper.asAuthorizationServerSettings();
    }

}
