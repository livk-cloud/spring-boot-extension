package com.livk.video.example.controller;

import com.livk.util.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.io.ByteArrayInputStream;
import java.io.File;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * <p>
 * VideoControllerTest
 * </p>
 *
 * @author livk
 * @date 2022/11/17
 */
@SpringBootTest
@AutoConfigureMockMvc
class VideoControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void testVideo() throws Exception {
        mockMvc.perform(get("/video"))
                .andExpect(status().isOk())
                .andDo(result -> {
                    ByteArrayInputStream in = new ByteArrayInputStream(result.getResponse().getContentAsByteArray());
                    FileUtils.testDownload(in, "test.mp4");
                });
        File video = new File("./test.mp4");
        Assertions.assertTrue(video.exists());
        Assertions.assertTrue(video.delete());
    }
}
