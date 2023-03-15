package com.livk.sso.auth.config;

import com.livk.sso.auth.handler.CustomAccessDeniedHandler;
import com.livk.sso.auth.handler.CustomAuthenticationFailureHandler;
import com.livk.sso.auth.handler.CustomAuthenticationSuccessHandler;
import com.livk.sso.auth.handler.CustomLogoutSuccessHandler;
import com.livk.sso.auth.support.CustomAuthenticationEntryPoint;
import com.livk.sso.commons.filter.TokenVerifyFilter;
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
 */
@Configuration
public class WebSecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        TokenLoginFilter tokenLoginFilter = new TokenLoginFilter(authenticationManagerBuilder.getObject());
        tokenLoginFilter.setAuthenticationSuccessHandler(new CustomAuthenticationSuccessHandler());
        tokenLoginFilter.setAuthenticationFailureHandler(new CustomAuthenticationFailureHandler());

        TokenVerifyFilter tokenVerifyFilter = new TokenVerifyFilter(authenticationManagerBuilder.getObject());
        return http.csrf()
                .disable()
                .authorizeHttpRequests()
                .requestMatchers("/user/query")
                .hasAnyRole("ADMIN")
                .anyRequest()
                .authenticated()
                .and()
                .addFilterBefore(tokenLoginFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(tokenVerifyFilter, BasicAuthenticationFilter.class)
                .exceptionHandling()
                .accessDeniedHandler(new CustomAccessDeniedHandler())
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .and()
                .logout()
                .logoutUrl("/oauth2/logout")
                .logoutSuccessHandler(new CustomLogoutSuccessHandler())
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .build();
    }

}
