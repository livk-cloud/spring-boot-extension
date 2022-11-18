package com.livk.sso.auth.config;

import com.livk.sso.auth.filter.TokenVerifyFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

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
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

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
                .addFilterBefore(new TokenLoginFilter(authenticationManagerBuilder.getObject(), properties),
                        UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(new TokenVerifyFilter(properties), BasicAuthenticationFilter.class)
                .logout()
                .logoutUrl("/oauth2/logout")
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .build();
    }

}
