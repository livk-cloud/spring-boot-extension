package com.livk.auth.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.livk.auth.server.domain.User;
import com.livk.auth.server.mapper.UsersMapper;
import com.livk.auth.server.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * <p>
 * UserServiceImpl
 * </p>
 *
 * @author livk
 * @date 2021/12/22
 */
@Service
public class UserServiceImpl extends ServiceImpl<UsersMapper, User> implements UserService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username.equals("18664960000")) {
            username = "livk";
        }
        return this.getOne(Wrappers.lambdaQuery(User.class).eq(User::getUsername, username));
    }

}
