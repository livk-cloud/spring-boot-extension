package com.livk.retry.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * <p>
 * RetryControllerTest
 * </p>
 *
 * @author livk
 * @date 2022/9/14
 */
@SpringBootTest
@AutoConfigureMockMvc
class RetryControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void retryTest() throws Exception {
        mockMvc.perform(get("/retry"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(MockMvcResultMatchers.content().string("recover SUC"));
    }
}
