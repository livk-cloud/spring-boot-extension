package com.livk.auth.server.common.core;

import com.livk.auth.server.common.handler.FormAuthenticationFailureHandler;
import com.livk.auth.server.common.handler.SsoLogoutSuccessHandler;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

/**
 * <p>基于授权码模式 统一认证登录 spring security & sas 都可以使用 所以抽取成 HttpConfigurer</p>
 *
 * @author livk
 */
public final class FormIdentityLoginConfigurer
        extends AbstractHttpConfigurer<FormIdentityLoginConfigurer, HttpSecurity> {

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
