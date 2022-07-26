package com.livk.doc;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Map;

/**
 * <p>
 * DocControllerTest
 * </p>
 *
 * @author livk
 * @date 2022/7/23
 */
@WebMvcTest
@ExtendWith({SpringExtension.class, RestDocumentationExtension.class})
class DocControllerTest {

    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @BeforeEach
    public void setMockMvc(WebApplicationContext webApplicationContext,
                           RestDocumentationContextProvider provider) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(MockMvcRestDocumentation.documentationConfiguration(provider))
                .build();
    }

    @Test
    void testGet() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/doc")
                        .param("name", "world"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("hello world"))
                .andDo(MockMvcRestDocumentation.document("get"));
    }

    @Test
    void testPost() throws Exception {
        Map<String, String> map = Map.of("username", "livk", "password", "123456");
        mockMvc.perform(MockMvcRequestBuilders.post("/doc")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(map)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("livk"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.password").value("123456"))
                .andDo(MockMvcRestDocumentation.document("post"));
    }
}
