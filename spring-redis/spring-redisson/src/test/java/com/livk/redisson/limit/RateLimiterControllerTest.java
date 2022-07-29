package com.livk.redisson.limit;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 * <p>
 * RateLimiterControllerTest
 * </p>
 *
 * @author livk
 * @date 2022/6/14
 */
@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
class RateLimiterControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void testRate() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/limit").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print()).andReturn();
    }

}
