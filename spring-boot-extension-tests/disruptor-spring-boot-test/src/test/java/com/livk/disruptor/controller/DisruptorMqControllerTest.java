package com.livk.disruptor.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author livk
 */
@SpringBootTest
@AutoConfigureMockMvc
class DisruptorMqControllerTest {

	@Autowired
	MockMvc mockMvc;

	@Test
	void send() throws Exception {
		mockMvc.perform(post("/msg")).andDo(print()).andExpect(status().isOk());
	}

	@Test
	void sendBatch() throws Exception {
		mockMvc.perform(post("/msg/batch")).andDo(print()).andExpect(status().isOk());
	}

}
