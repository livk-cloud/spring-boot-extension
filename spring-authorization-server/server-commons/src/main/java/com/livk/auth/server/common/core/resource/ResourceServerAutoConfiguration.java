package com.livk.auth.server.common.core.resource;

import com.livk.auto.service.annotation.SpringAutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;

/**
 * @author livk
 */
@SpringAutoService
@AutoConfiguration
@RequiredArgsConstructor
public class ResourceServerAutoConfiguration {

    /**
     * 资源服务器toke内省处理器
     *
     * @param authorizationService token 存储实现
     * @return TokenIntrospector
     */
    @Bean
    public OpaqueTokenIntrospector opaqueTokenIntrospector(OAuth2AuthorizationService authorizationService) {
        return new CustomOpaqueTokenIntrospector(authorizationService);
    }

}
