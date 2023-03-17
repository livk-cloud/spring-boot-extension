package com.livk.mvc.controller;

import com.livk.crypto.support.AesSecurity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Locale;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author livk
 */
@SpringBootTest
@AutoConfigureMockMvc
class InfoControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void info() throws Exception {
        AesSecurity security = new AesSecurity();
        String encoding = security.print(123456L, Locale.CHINA);
        mockMvc.perform(get("/info/{id}", encoding)
                        .param("id", encoding))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("paramId", encoding).exists())
                .andExpect(jsonPath("idStr", encoding).exists());
    }
}
