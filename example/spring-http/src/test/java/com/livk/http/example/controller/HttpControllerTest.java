package com.livk.http.example.controller;

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
 * HttpControllerTest
 * </p>
 *
 * @author livk
 */
@AutoConfigureMockMvc
@SpringBootTest
class HttpControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void getTest() throws Exception {
        mockMvc.perform(get("/get"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
