package com.livk.example.mapper;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.livk.common.LivkPage;
import com.livk.example.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
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
@SpringBootTest
class UserMapperTest {
    @Autowired
    UserMapper userMapper;

    @Test
    public void updateTest() {
        User user = new User();
        user.setId(3);
        user.setUsername("livk https");
        int result = userMapper.updateById(user);
        Assertions.assertEquals(1, result);
    }

    @Test
    public void selectByIdTest() {
        User result = userMapper.selectById(2);
        Assertions.assertNotNull(result);
    }

    @Test
    public void selectAllTest() {
        try (Page<User> page = PageHelper.<User>startPage(1, 10)
                .countColumn(User.column(User::getId))
                .doSelectPage(userMapper::list)) {
            LivkPage<User> result = LivkPage.of(page);
            Assertions.assertNotNull(result);
        }
    }

    @Test
    public void deleteTest() {
        int result = userMapper.deleteById(5);
        Assertions.assertEquals(1, result);
    }
}
