package com.livk.admin.server.config;

import de.codecentric.boot.admin.server.config.AdminServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.UUID;

/**
 * <p>
 * SecurityConfig
 * </p>
 *
 * @author livk
 * @date 2021/11/8
 */
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   AdminServerProperties adminServer) throws Exception {
        SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
        successHandler.setTargetUrlParameter("redirectTo");
        successHandler.setDefaultTargetUrl(adminServer.path("/"));
        return http.authorizeHttpRequests()
                .requestMatchers(adminServer.path("/assets/**"))
                .permitAll()
                .requestMatchers(adminServer.path("/variables.css"))
                .permitAll()
                .requestMatchers(adminServer.path("/actuator/info"))
                .permitAll()
                .requestMatchers(adminServer.path("/actuator/health"))
                .permitAll()
                .requestMatchers(adminServer.path("/login"))
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .loginPage(adminServer.path("/login"))
                .successHandler(successHandler)
                .and()
                .logout()
                .logoutUrl(adminServer.path("/logout"))
                .and()
                .httpBasic(Customizer.withDefaults())
                .csrf()
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .ignoringRequestMatchers(new AntPathRequestMatcher(adminServer.path("/instances"), HttpMethod.POST.toString()),
                        new AntPathRequestMatcher(adminServer.path("/instances/*"), HttpMethod.DELETE.toString()),
                        new AntPathRequestMatcher(adminServer.path("/actuator/**")))
                .and()
                .rememberMe()
                .key(UUID.randomUUID().toString())
                .tokenValiditySeconds(1209600)
                .and()
                .build();
    }
}
