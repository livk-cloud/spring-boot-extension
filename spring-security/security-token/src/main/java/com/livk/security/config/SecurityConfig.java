package com.livk.security.config;

import com.livk.security.filter.AuthorizationTokenFilter;
import com.livk.security.handler.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * <p>
 * SecurityConfig
 * </p>
 *
 * @author livk
 * @date 2022/3/23
 */
@EnableWebSecurity
public class SecurityConfig {

    @Value("${permitAll.url}")
    private String[] permitAllUrl;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationProvider authenticationProvider,
                                                   AuthorizationTokenFilter authorizationTokenFilter) throws Exception {
        return http.authorizeHttpRequests()
                .requestMatchers(permitAllUrl)
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .loginProcessingUrl("/login")
                .permitAll()
                .failureHandler(new LivkAuthenticationFailureHandler())
                .successHandler(new LivkAuthenticationSuccessHandler())
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessHandler(new LivkLogoutSuccessHandler())
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterAfter(authorizationTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .accessDeniedHandler(new LivkAccessDeniedHandler())
                .authenticationEntryPoint(new LivkAuthenticationEntryPoint())
                .and()
                .cors()
                .disable()
                .csrf()
                .disable()
                .build();
    }

}
