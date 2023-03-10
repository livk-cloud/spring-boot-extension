package com.livk.dynamic.example.controller;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * <p>
 * UserControllerTest
 * </p>
 *
 * @author livk
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Order(1)
    @Test
    void testMysqlSave() throws Exception {
        mockMvc.perform(post("/user/mysql"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Order(2)
    @Test
    void testMysqlUser() throws Exception {
        mockMvc.perform(get("/user/mysql"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Order(3)
    @Test
    void testPgsqlSave() throws Exception {
        mockMvc.perform(post("/user/pgsql"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Order(4)
    @Test
    void testPgsqlUser() throws Exception {
        mockMvc.perform(get("/user/pgsql"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme
