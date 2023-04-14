package com.livk.redisson.limit.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author livk
 */
@SpringBootTest
@AutoConfigureMockMvc
class RateLimiterControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void rate() throws Exception {
        for (int i = 0; i < 2; i++) {
            mockMvc.perform(get("/limit").accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn();
        }

        mockMvc.perform(get("/limit").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError())
                .andDo(print())
                .andReturn();
    }
}
