package com.livk.mybatis.tree.example.mapper;

import com.livk.mybatis.tree.example.entity.Menu;
import com.livk.util.JacksonUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * <p>
 * MenuMapperTest
 * </p>
 *
 * @author livk
 * @date 2022/11/15
 */
@SpringBootTest
class MenuMapperTest {

    @Autowired
    MenuMapper mapper;

    @Test
    void list() {
        List<Menu> menus = mapper.list();
        System.out.println(JacksonUtils.toJsonStr(menus));
    }
}
