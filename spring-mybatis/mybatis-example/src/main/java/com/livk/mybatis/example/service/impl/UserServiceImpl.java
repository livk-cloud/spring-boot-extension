package com.livk.mybatis.example.service.impl;

import com.livk.mybatis.example.entity.User;
import com.livk.mybatis.example.mapper.UserMapper;
import com.livk.mybatis.example.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * UserServiceImpl
 * </p>
 *
 * @author livk
 * @date 2023/1/5
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    @Override
    public User getById(Integer id) {
        return userMapper.selectById(id);
    }

    @Override
    public boolean updateById(User user) {
        return userMapper.updateById(user) > 0;
    }

    @Override
    public boolean save(User user) {
        return userMapper.insert(user) > 0;
    }

    @Override
    public boolean deleteById(Integer id) {
        return userMapper.deleteById(id) > 0;
    }

    @Override
    public List<User> list() {
        return userMapper.list();
    }
}
