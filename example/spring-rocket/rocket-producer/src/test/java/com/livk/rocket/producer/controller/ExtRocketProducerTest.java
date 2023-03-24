package com.livk.rocket.producer.controller;

import com.livk.commons.jackson.JacksonUtils;
import com.livk.rocket.constant.RocketConstant;
import com.livk.rocket.dto.RocketDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author laokou
 * @author livk
 */
@SpringBootTest
@AutoConfigureMockMvc
class ExtRocketProducerTest {

    @Autowired
    MockMvc mockMvc;

    RocketDTO dto = new RocketDTO();

    @BeforeEach
    public void init() {
        String msg = "Java第一，老寇无敌。千秋万代，一统江湖。";
        dto.setBody(msg);
    }


    @Test
    void sendMessage() throws Exception {
        mockMvc.perform(post("/api/ext/send/{topic}", RocketConstant.LIVK_EXT_MESSAGE_TOPIC)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JacksonUtils.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andDo(log());
    }

    @Test
    void sendAsyncMessage() throws Exception {
        mockMvc.perform(post("/api/ext/sendAsync/{topic}", RocketConstant.LIVK_EXT_MESSAGE_TOPIC)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JacksonUtils.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andDo(log());
    }

    @Test
    void sendOneMessage() throws Exception {
        mockMvc.perform(post("/api/ext/sendOne/{topic}", RocketConstant.LIVK_EXT_MESSAGE_TOPIC)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JacksonUtils.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andDo(log());
    }

    @Test
    void sendTransactionMessage() throws Exception {
        mockMvc.perform(post("/api/ext/sendTransaction/{topic}", RocketConstant.LIVK_EXT_MESSAGE_TOPIC)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JacksonUtils.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andDo(log());
    }

    @Test
    void sendDelay() throws Exception {
        mockMvc.perform(post("/api/ext/sendDelay/{topic}", RocketConstant.LIVK_EXT_MESSAGE_TOPIC)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JacksonUtils.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void sendMessageOrderly() throws Exception {
        for (int i = 1; i <= 6; i++) {
            RocketDTO rocketDTO = new RocketDTO();
            rocketDTO.setBody("同步顺序消息" + i);
            mockMvc.perform(post("/api/ext/sendOrderly/{topic}", RocketConstant.LIVK_EXT_MESSAGE_ORDERLY_TOPIC)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(JacksonUtils.writeValueAsString(rocketDTO)))
                    .andExpect(status().isOk())
                    .andDo(print());
        }
    }

    @Test
    void sendAsyncMessageOrderly() throws Exception {
        for (int i = 1; i <= 6; i++) {
            RocketDTO rocketDTO = new RocketDTO();
            rocketDTO.setBody("异步顺序消息" + i);
            mockMvc.perform(post("/api/ext/sendAsyncOrderly/{topic}", RocketConstant.LIVK_EXT_MESSAGE_ORDERLY_TOPIC)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(JacksonUtils.writeValueAsString(rocketDTO)))
                    .andExpect(status().isOk())
                    .andDo(print());
        }
    }

    @Test
    void sendOneMessageOrderly() throws Exception {
        for (int i = 1; i <= 6; i++) {
            RocketDTO rocketDTO = new RocketDTO();
            rocketDTO.setBody("单向顺序消息" + i);
            mockMvc.perform(post("/api/ext/sendOneOrderly/{topic}", RocketConstant.LIVK_EXT_MESSAGE_ORDERLY_TOPIC)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(JacksonUtils.writeValueAsString(rocketDTO)))
                    .andExpect(status().isOk())
                    .andDo(print());
        }
    }

}
