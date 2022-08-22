package com.livk.http.example.controller;

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
 * HttpControllerTest
 * </p>
 *
 * @author livk
 * @date 2022/8/4
 */
@AutoConfigureMockMvc
@SpringBootTest
class HttpControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void get() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/get"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
