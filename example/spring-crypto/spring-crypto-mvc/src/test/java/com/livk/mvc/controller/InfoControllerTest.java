package com.livk.mvc.controller;

import com.livk.commons.jackson.JacksonUtils;
import com.livk.crypto.CryptoType;
import com.livk.crypto.support.AesSecurity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Locale;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    @Autowired
    AesSecurity aesSecurity;

    @Test
    void info() throws Exception {
        String encoding = aesSecurity.print(123456L, Locale.CHINA);
        encoding = CryptoType.AES.wrapper(encoding);
        String json = JacksonUtils.writeValueAsString(Map.of("variableId", encoding, "paramId", encoding));
        mockMvc.perform(post("/info/{id}", encoding)
                        .param("id", encoding).contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id.paramId", encoding).exists())
                .andExpect(jsonPath("id.variableId", encoding).exists())
                .andExpect(jsonPath("body.paramId", encoding).exists())
                .andExpect(jsonPath("body.variableId", encoding).exists());
    }
}
