package com.livk.mybatis.util;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * <p>
 * SqlUtilsTest
 * </p>
 *
 * @author livk
 * @date 2022/8/18
 */
class SqlUtilsTest {

    @Test
    void parseTable() {
        String sql = "select * from user";
        List<String> result = SqlUtils.parseTable(sql);
        assertEquals(result, List.of("user"));
    }

    @Test
    void getParams() {
        String sql = "select id,username from user";
        List<String> params = SqlUtils.getParams(sql);
        assertEquals(params, List.of("id", "username"));
    }
}
