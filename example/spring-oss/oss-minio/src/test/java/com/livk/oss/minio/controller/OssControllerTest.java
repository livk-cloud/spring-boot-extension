package com.livk.oss.minio.controller;

import com.livk.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author livk
 */
@SpringBootTest
@AutoConfigureMockMvc
class OssControllerTest {
    @Autowired
    MockMvc mockMvc;


    @Test
    void test() throws Exception {
        String url = mockMvc.perform(get("/oss/test"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn()
                .getResponse()
                .getContentAsString();
        try (InputStream stream = new URL(url).openStream()) {
            FileUtils.download(stream, "./test.jpg");
        }
        File file = new File("./test.jpg");
        assertTrue(file.exists());
        assertTrue(file.delete());
    }
}

