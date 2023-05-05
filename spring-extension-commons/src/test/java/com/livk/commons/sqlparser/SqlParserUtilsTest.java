package com.livk.commons.sqlparser;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertLinesMatch;

/**
 * @author livk
 */
class SqlParserUtilsTest {

    @Test
    void parseTable() {
        assertLinesMatch(List.of("user"),
                SqlParserUtils.parseTable("select * from user"));
        assertLinesMatch(List.of("user", "account"),
                SqlParserUtils.parseTable("select * from user u left join account a on u.id = a.user_id"));
        assertLinesMatch(List.of("user"),
                SqlParserUtils.parseTable("insert into user values (1,'root','root')"));
        assertLinesMatch(List.of("user"),
                SqlParserUtils.parseTable("update user set username = 'root' where id = 1"));
        assertLinesMatch(List.of("user"),
                SqlParserUtils.parseTable("delete from user where id = 1"));
    }

    @Test
    void getParams() {
        assertLinesMatch(List.of("id", "username", "password"),
                SqlParserUtils.getParams("select id,username,password from user"));
        assertLinesMatch(List.of("u.id", "u.username", "a.account_name"),
                SqlParserUtils.getParams("select u.id,u.username,a.account_name from user u left join account a on u.id = a.user_id"));
        assertLinesMatch(List.of("id", "username", "password"),
                SqlParserUtils.getParams("insert into user (id,username,password) values (1,'root','root')"));
        assertLinesMatch(List.of("username"),
                SqlParserUtils.getParams("update user set username = 'root' where id = 1"));
    }
}
