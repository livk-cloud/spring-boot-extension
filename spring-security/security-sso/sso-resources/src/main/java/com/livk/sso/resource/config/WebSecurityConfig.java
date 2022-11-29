package com.livk.sso.resource.config;

import com.livk.sso.commons.RsaKeyProperties;
import com.livk.sso.resource.filter.TokenVerifyFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * <p>
 * WebSecurityConfig
 * </p>
 *
 * @author livk
 * @date 2022/4/11
 */
@Configuration
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, RsaKeyProperties properties,
                                                   AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        return http.csrf()
                .disable()
                .authorizeHttpRequests()
                .requestMatchers("/user/query")
                .hasAnyRole("ADMIN")
                .anyRequest()
                .authenticated()
                .and()
                .addFilter(new TokenVerifyFilter(authenticationManagerBuilder.getObject(), properties))
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .build();
    }

}
