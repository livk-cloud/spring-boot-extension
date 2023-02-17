package com.livk.security.controller;

import com.livk.commons.jackson.JacksonUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * <p>
 * HelloControllerTest
 * </p>
 *
 * @author livk
 */
@SpringBootTest
@AutoConfigureMockMvc
class HelloControllerTest {
    @Autowired
    MockMvc mockMvc;

    String token;

    @BeforeEach
    public void init() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.set("username", "livk");
        params.set("password", "123456");
        MockHttpServletResponse response = mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .params(params))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("code", "200").exists())
                .andReturn().getResponse();
        token = JacksonUtils.readValueMap(response.getContentAsString(), String.class, String.class).get("data");
    }


    @Test
    void testHello() throws Exception {
        mockMvc.perform(get("/hello")
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().string("hello"));
    }

    @Test
    void testIndex() throws Exception {
        mockMvc.perform(get("/index")
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().string("index"));
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme
