package com.livk.batch.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * <p>
 * JobControllerTest
 * </p>
 *
 * @author livk
 * @date 2022/9/14
 */
@SpringBootTest
@AutoConfigureMockMvc
class JobControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void doJobTest() throws Exception {
        mockMvc.perform(post("/doJob"))
                .andExpect(status().isOk());
    }
}
