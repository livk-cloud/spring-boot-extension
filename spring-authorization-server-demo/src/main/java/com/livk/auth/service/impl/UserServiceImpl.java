package com.livk.auth.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.livk.auth.domain.Users;
import com.livk.auth.mapper.UsersMapper;
import com.livk.auth.service.UserService;
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
public class UserServiceImpl extends ServiceImpl<UsersMapper, Users> implements UserService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.getOne(Wrappers.lambdaQuery(Users.class).eq(Users::getUsername, username));
    }

}
