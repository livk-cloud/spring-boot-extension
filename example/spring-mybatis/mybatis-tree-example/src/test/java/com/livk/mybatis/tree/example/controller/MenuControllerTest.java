package com.livk.mybatis.tree.example.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * <p>
 * MenuControllerTest
 * </p>
 *
 * @author livk
 */
@SpringBootTest({
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.url=jdbc:h2:mem:test",
        "spring.sql.init.schema-locations=classpath:menu.sql",
        "spring.sql.init.platform=h2",
        "spring.sql.init.mode=embedded",
})
@AutoConfigureMockMvc
class MenuControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void list() throws Exception {
        mockMvc.perform(get("/menu"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
