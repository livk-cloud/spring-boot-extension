package com.livk.ip.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * <p>
 * Ip2RegionControllerTest
 * </p>
 *
 * @author livk
 * @date 2022/8/19
 */
@SpringBootTest("ip2region.enabled=true")
@AutoConfigureMockMvc
class Ip2RegionControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void getTest() throws Exception {
        mockMvc.perform(get("/ip")
                        .param("ip", "110.242.68.66"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("nation", "中国").exists())
                .andExpect(jsonPath("province", "河北省").exists())
                .andExpect(jsonPath("city", "保定市").exists())
                .andExpect(jsonPath("operator", "联通").exists());
    }
}
