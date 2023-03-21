package com.livk.provider.controller;

import com.livk.commons.jackson.JacksonUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author livk
 */
@SpringBootTest
@AutoConfigureMockMvc
class RabbitControllerTest {

    static String body = JacksonUtils.writeValueAsString(Map.of(
            "msg", "hello",
            "data", "By Livk"
    ));
    @Autowired
    MockMvc mockMvc;

    @Test
    void sendMsgDirect() throws Exception {
        mockMvc.perform(post("/provider/sendMsgDirect")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void sendMsgFanout() throws Exception {
        mockMvc.perform(post("/provider/sendMsgFanout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void sendMsgTopic() throws Exception {
        mockMvc.perform(post("/provider/sendMsgTopic/{key}", "rabbit.a.b")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(post("/provider/sendMsgTopic/{key}", "a.b")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void sendMsgHeaders() throws Exception {
        mockMvc.perform(post("/provider/sendMsgHeaders")
                        .queryParam("json", "{\"auth\":\"livk\"}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(post("/provider/sendMsgHeaders")
                        .queryParam("json", "{\"username\":\"livk\",\"password\":\"livk\"}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
