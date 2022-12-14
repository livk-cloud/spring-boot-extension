package com.livk.excel.mvc.controller;

import com.livk.autoconfigure.excel.annotation.ExcelReturn;
import com.livk.commons.util.FileUtils;
import com.livk.commons.util.JacksonUtils;
import com.livk.excel.mvc.entity.Info;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * <p>
 * Info2ControllerTest
 * </p>
 *
 * @author livk
 */
@AutoConfigureMockMvc
@SpringBootTest
class Info2ControllerTest {

    @Autowired
    MockMvc mockMvc;

    ClassPathResource resource = new ClassPathResource("outFile.xls");

    MockMultipartFile file = new MockMultipartFile("file", "outFile.xls", MediaType.MULTIPART_FORM_DATA_VALUE, resource.getInputStream());

    Info2ControllerTest() throws IOException {
    }

    @Test
    void uploadAndDownload() throws Exception {
        mockMvc.perform(multipart(POST, "/info/uploadAndDownload")
                        .file(file))
                .andExpect(status().isOk())
                .andDo(result -> {
                    ByteArrayInputStream in = new ByteArrayInputStream(result.getResponse().getContentAsByteArray());
                    FileUtils.testDownload(in, "infoUploadAndDownloadMock" + ExcelReturn.Suffix.XLSM.getName());
                });
        File outFile = new File("./infoUploadAndDownloadMock" + ExcelReturn.Suffix.XLSM.getName());
        assertTrue(outFile.exists());
        assertTrue(outFile.delete());
    }

    @Test
    void download() throws Exception {
        List<Info> infos = LongStream.rangeClosed(1, 100)
                .mapToObj(i -> new Info(i, 13_000_000_000L + i + ""))
                .toList();
        mockMvc.perform(MockMvcRequestBuilders.post("/info/download")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JacksonUtils.toJsonStr(infos)))
                .andExpect(status().isOk())
                .andDo(result -> {
                    ByteArrayInputStream in = new ByteArrayInputStream(result.getResponse().getContentAsByteArray());
                    FileUtils.testDownload(in, "infoDownload" + ExcelReturn.Suffix.XLSM.getName());
                });
        File outFile = new File("./infoDownload" + ExcelReturn.Suffix.XLSM.getName());
        assertTrue(outFile.exists());
        assertTrue(outFile.delete());
    }
}
