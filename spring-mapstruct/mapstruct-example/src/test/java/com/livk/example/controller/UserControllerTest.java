package com.livk.example.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

/**
 * <p>
 * UserControllerTest
 * </p>
 *
 * @author livk
 * @date 2022/8/4
 */
@AutoConfigureMockMvc
@SpringBootTest
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void testList() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("customize[0].username", "livk").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("spring[0].username", "livk").exists());
    }

    @Test
    void testGetById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user/1"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("customize.username", "livk").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("spring.username", "livk").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("adapter.username", "livk").exists());
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme
