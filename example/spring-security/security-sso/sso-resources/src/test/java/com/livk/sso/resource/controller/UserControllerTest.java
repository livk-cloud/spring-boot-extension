package com.livk.sso.resource.controller;

import com.livk.commons.http.annotation.EnableHttpClient;
import com.livk.commons.jackson.JacksonUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * <p>
 * UserControllerTest
 * </p>
 *
 * @author livk
 */
@Disabled("需要启动授权服务器")
@EnableHttpClient
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    RestTemplate template;

    String token;

    @BeforeEach
    public void init() {
        Map<String, String> body = new HashMap<>();
        body.put("username", "livk");
        body.put("password", "123456");
        ResponseEntity<String> responseEntity = template.postForEntity("http://localhost:9987/login", JacksonUtils.writeValueAsString(body), String.class);
        token = "Bearer " + JacksonUtils.readValueMap(responseEntity.getBody(), String.class, String.class).get("data");
    }

    @Test
    public void test() {
        System.out.println(token);
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
