package com.livk.yauaa.webflux.example.controller;

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
 * UserAgentControllerTest
 * </p>
 *
 * @author livk
 */
@SpringBootTest
@AutoConfigureMockMvc
class UserAgentControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void getTest() throws Exception {
        mockMvc.perform(get("/user-agent"))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
