package com.livk.dynamic.example.controller;

import com.livk.annotation.DynamicSource;
import com.livk.dynamic.example.entity.User;
import com.livk.dynamic.example.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * UserController
 * </p>
 *
 * @author livk
 * @date 2022/5/26
 */
@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {

    private final UserMapper userMapper;

    @DynamicSource("mysql")
    @PostMapping("mysql")
    public HttpEntity<Boolean> mysqlSave() {
        User user = new User();
        user.setUsername("root");
        user.setPassword("123456");
        return ResponseEntity.ok(userMapper.insert(user, "user") != 0);
    }

    @DynamicSource("mysql")
    @GetMapping("mysql")
    public HttpEntity<List<User>> mysqlUser() {
        return ResponseEntity.ok(userMapper.selectList("user"));
    }

    @DynamicSource("pgsql")
    @PostMapping("pgsql")
    public HttpEntity<Boolean> pgsqlSave() {
        User user = new User();
        user.setUsername("postgres");
        user.setPassword("123456");
        return ResponseEntity.ok(userMapper.insert(user, "\"user\"") != 0);
    }

    @DynamicSource("pgsql")
    @GetMapping("pgsql")
    public HttpEntity<List<User>> pgsqlUser() {
        return ResponseEntity.ok(userMapper.selectList("\"user\""));
    }

}
