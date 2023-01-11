package com.livk.mybatis.example.mapper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.livk.commons.domain.CustomPage;
import com.livk.commons.jackson.JacksonUtils;
import com.livk.commons.util.ReflectionUtils;
import com.livk.mybatis.example.entity.User;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * <p>
 * UserMapperTest
 * </p>
 *
 * @author livk
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
        assertEquals(1, result);
    }

    @Order(2)
    @Test
    public void updateTest() {
        User user = new User();
        user.setId(id);
        user.setUsername("livk https");
        int result = userMapper.updateById(user);
        assertEquals(1, result);
    }

    @Order(3)
    @Test
    public void selectByIdTest() {
        User result = userMapper.selectById(id);
        assertNotNull(result);
    }

    @Order(4)
    @Test
    public void selectAllTest() {
        try (Page<User> page = PageHelper.<User>startPage(1, 10)
                .countColumn(ReflectionUtils.getFieldName(User::getId))
                .doSelectPage(userMapper::list)) {
            CustomPage<User> result = new CustomPage<>(page);
            assertNotNull(result);
            String json = JacksonUtils.toJsonStr(result);
            CustomPage<User> customPage = JacksonUtils.toBean(json, new TypeReference<>() {
            });
            assertNotNull(customPage);
        }
    }

    @Order(5)
    @Test
    public void deleteTest() {
        int result = userMapper.deleteById(id);
        assertEquals(1, result);
    }
}
