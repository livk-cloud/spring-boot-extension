package com.livk.mybatis.example.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.livk.commons.bean.domain.PageInfo;
import com.livk.commons.function.FieldFunc;
import com.livk.mybatis.example.entity.User;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * <p>
 * UserServiceTest
 * </p>
 *
 * @author livk
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
class UserServiceTest {

    @Autowired
    UserService userService;

    Integer id = 10;

    @Order(3)
    @Test
    void getById() {
        User result = userService.getById(id);
        assertNotNull(result);
    }

    @Order(2)
    @Test
    void updateById() {
        User user = new User();
        user.setId(id);
        user.setUsername("livk https");
        boolean result = userService.updateById(user);
        assertTrue(result);
    }

    @Order(1)
    @Test
    void save() {
        User user = new User();
        user.setId(id);
        user.setUsername("livk");
        boolean result = userService.save(user);
        assertTrue(result);
    }

    @Order(5)
    @Test
    void deleteById() {
        boolean result = userService.deleteById(id);
        assertTrue(result);
    }

    @Order(4)
    @Test
    void list() {
        try (Page<User> page = PageHelper.<User>startPage(1, 10)
                .countColumn(FieldFunc.get(User::getId))
                .doSelectPage(userService::list)) {
            PageInfo<User> result = new PageInfo<>(page);
            assertNotNull(result);
        }
    }
}
