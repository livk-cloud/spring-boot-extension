package com.livk.auth.server.service.impl;

import com.livk.auth.server.mapper.UsersMapper;
import com.livk.auth.server.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * <p>
 * UserServiceImpl
 * </p>
 *
 * @author livk
 *
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UsersMapper usersMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username.equals("18664960000")) {
            username = "livk";
        }
        return usersMapper.getOne(username);
    }

}
