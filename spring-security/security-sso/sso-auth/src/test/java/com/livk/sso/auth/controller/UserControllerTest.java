package com.livk.sso.auth.controller;

import com.livk.commons.util.JacksonUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * <p>
 * UserControllerTest
 * </p>
 *
 * @author livk
 */
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    @Autowired
    MockMvc mockMvc;

    String token;

    @BeforeEach
    public void init() throws Exception {
        Map<String, String> body = new HashMap<>();
        body.put("username", "livk");
        body.put("password", "123456");
        MockHttpServletResponse response = mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JacksonUtils.toJsonStr(body)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("code", "200").exists())
                .andReturn().getResponse();
        token = "Bearer " + JacksonUtils.toMap(response.getContentAsString(), String.class, String.class).get("data");
    }


    @Test
    void testList() throws Exception {
        mockMvc.perform(get("/user/list")
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().string("list"));
    }

    @Test
    void testUpdate() throws Exception {
        mockMvc.perform(put("/user/update")
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().string("update"));
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme
