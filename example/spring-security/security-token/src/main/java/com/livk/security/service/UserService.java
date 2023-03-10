package com.livk.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 * UserService
 * </p>
 *
 * @author livk
 */
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService, InitializingBean {

    private static final Map<String, UserDetails> user = new ConcurrentHashMap<>();

    private final PasswordEncoder passwordEncoder;


    private void init() {
        String encode = passwordEncoder.encode("123456");
        user.put("livk", new User("livk", encode, AuthorityUtils.createAuthorityList("ROLE_ADMIN")));
        user.put("root", new User("root", encode, AuthorityUtils.createAuthorityList("ROLE_ADMIN")));
        user.put("admin", new User("admin", encode, AuthorityUtils.createAuthorityList("ROLE_ADMIN")));
    }

    @Override

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails userDetails = user.get(username);
        init();
        return userDetails;
    }

    @Override
    public void afterPropertiesSet() {
        init();
    }
}
