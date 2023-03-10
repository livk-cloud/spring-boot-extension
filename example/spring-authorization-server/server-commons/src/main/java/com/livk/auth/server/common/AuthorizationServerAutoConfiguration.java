package com.livk.auth.server.common;

import com.livk.auth.server.common.core.UserDetailsAuthenticationProvider;
import com.livk.auth.server.common.service.Oauth2UserDetailsService;
import com.livk.auto.service.annotation.SpringAutoService;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * <p>
 * AuthorizationServerAutoConfiguration
 * </p>
 *
 * @author livk
 *
 */
@SpringAutoService
@AutoConfiguration
public class AuthorizationServerAutoConfiguration {

    @Bean
    public UserDetailsAuthenticationProvider userDetailsAuthenticationProvider(PasswordEncoder passwordEncoder,
                                                                               ObjectProvider<Oauth2UserDetailsService> oauth2UserDetailsServices) {
        return new UserDetailsAuthenticationProvider(passwordEncoder, oauth2UserDetailsServices);
    }
}
