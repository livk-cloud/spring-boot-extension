package com.livk.doc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

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
                .andDo(MockMvcRestDocumentation.document("doc"));
    }
}
