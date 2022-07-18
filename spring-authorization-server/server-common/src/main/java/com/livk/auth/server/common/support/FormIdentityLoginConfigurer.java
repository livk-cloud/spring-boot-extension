package com.livk.auth.server.common.support;


import com.livk.auth.server.common.handler.FormAuthenticationFailureHandler;
import com.livk.auth.server.common.handler.SsoLogoutSuccessHandler;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

/**
 * <p>
 * FormIdentityLoginConfigurer
 * </p>
 *
 * @author livk
 * @date 2022/7/18
 */
public class FormIdentityLoginConfigurer extends AbstractHttpConfigurer<FormIdentityLoginConfigurer, HttpSecurity> {
    @Override
    public void init(HttpSecurity http) throws Exception {
        http.formLogin(formLogin -> formLogin.loginPage("/token/login")
                        .loginProcessingUrl("/token/form")
                        .failureHandler(new FormAuthenticationFailureHandler()))
                .logout()
                .logoutSuccessHandler(new SsoLogoutSuccessHandler())
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
                .and()
                .csrf()
                .disable();
    }
}
