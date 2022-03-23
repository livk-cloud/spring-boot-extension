package com.livk.security.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * UserService
 * </p>
 *
 * @author livk
 * @date 2022/3/23
 */
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private static final Map<String, UserDetails> user = new HashMap<>();

    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        String encode = passwordEncoder.encode("123456");
        user.put("livk", new User("livk", encode, AuthorityUtils.createAuthorityList("ROLE_ADMIN")));
        user.put("root", new User("root", encode, AuthorityUtils.createAuthorityList("ROLE_ADMIN")));
        user.put("admin", new User("admin", encode, AuthorityUtils.createAuthorityList("ROLE_ADMIN")));
    }

    @Override

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return user.get(username);
    }
}
