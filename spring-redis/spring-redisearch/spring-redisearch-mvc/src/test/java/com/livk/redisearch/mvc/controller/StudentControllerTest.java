package com.livk.redisearch.mvc.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * <p>
 * StudentControllerTest
 * </p>
 *
 * @author livk
 */
@SpringBootTest
@AutoConfigureMockMvc
class StudentControllerTest {
    @Autowired
    MockMvc mockMvc;


    @Test
    void testList() throws Exception {
        mockMvc.perform(get("/student"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("[0:9].name",
                        "livk-0", "livk-1", "livk-2", "livk-3", "livk-4", "livk-5", "livk-6", "livk-7", "livk-8", "livk-9").exists());

        mockMvc.perform(get("/student")
                        .param("query", "@class:{1班}"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("[0].name", "livk-0").exists());

        mockMvc.perform(get("/student")
                        .param("query", "livk"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("[0:9].name",
                        "livk-0", "livk-1", "livk-2", "livk-3", "livk-4", "livk-5", "livk-6", "livk-7", "livk-8", "livk-9").exists());

        mockMvc.perform(get("/student")
                        .param("query", "女"))
                .andExpect(status().isOk())
                .andDo(print());

        mockMvc.perform(get("/student")
                        .param("query", "是一个学生"))
                .andExpect(status().isOk())
                .andDo(print());
    }
}


