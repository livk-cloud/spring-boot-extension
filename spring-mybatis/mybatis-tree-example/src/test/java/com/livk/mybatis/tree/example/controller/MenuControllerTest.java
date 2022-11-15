package com.livk.mybatis.tree.example.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * <p>
 * MenuControllerTest
 * </p>
 *
 * @author livk
 * @date 2022/11/15
 */
@SpringBootTest
@AutoConfigureMockMvc
class MenuControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void list() throws Exception {
        mockMvc.perform(get("/menu"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
