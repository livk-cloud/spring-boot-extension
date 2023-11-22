package com.livk.proto.amqp.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author livk
 */
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

	@Autowired
	MockMvc mockMvc;

	@Test
	void send() throws Exception {
		mockMvc.perform(get("/user/send")).andExpect(status().isOk());
	}

}
