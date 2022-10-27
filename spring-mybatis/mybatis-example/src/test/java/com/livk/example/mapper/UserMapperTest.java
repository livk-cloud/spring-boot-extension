package com.livk.example.mapper;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.livk.common.LivkPage;
import com.livk.example.entity.User;
import com.livk.util.FieldUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * <p>
 * UserMapperTest
 * </p>
 *
 * @author livk
 * @date 2022/7/26
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
class UserMapperTest {
    @Autowired
    UserMapper userMapper;

    Integer id = 10;


    @Order(1)
    @Test
    public void saveTest() {
        User user = new User();
        user.setId(id);
        user.setUsername("livk");
        int result = userMapper.insert(user);
        Assertions.assertEquals(1, result);
    }

    @Order(2)
    @Test
    public void updateTest() {
        User user = new User();
        user.setId(id);
        user.setUsername("livk https");
        int result = userMapper.updateById(user);
        Assertions.assertEquals(1, result);
    }

    @Order(3)
    @Test
    public void selectByIdTest() {
        User result = userMapper.selectById(id);
        Assertions.assertNotNull(result);
    }

    @Order(4)
    @Test
    public void selectAllTest() {
        try (Page<User> page = PageHelper.<User>startPage(1, 10)
                .countColumn(FieldUtils.getFieldName(User::getId))
                .doSelectPage(userMapper::list)) {
            LivkPage<User> result = new LivkPage<>(page);
            Assertions.assertNotNull(result);
        }
    }

    @Order(5)
    @Test
    public void deleteTest() {
        int result = userMapper.deleteById(id);
        Assertions.assertEquals(1, result);
    }
}
