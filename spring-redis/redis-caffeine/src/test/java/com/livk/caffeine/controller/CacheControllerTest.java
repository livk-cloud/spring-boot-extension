package com.livk.caffeine.controller;

import com.livk.autoconfigure.redis.supprot.UniversalRedisTemplate;
import com.livk.autoconfigure.redis.util.RedisUtils;
import com.livk.commons.test.TestLogUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.Cursor;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * <p>
 * CacheControllerTest
 * </p>
 *
 * @author livk
 * @date 2022/12/26
 */
@SpringBootTest
@AutoConfigureMockMvc
class CacheControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    UniversalRedisTemplate redisTemplate;


    @Test
    void testGet() throws Exception {
        Set<String> result = new HashSet<>();
        String uuid = mockMvc.perform(get("/cache"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();
        result.add(uuid);
        for (int i = 0; i < 3; i++) {
            String newUUID = mockMvc.perform(get("/cache"))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andExpect(content().string(uuid))
                    .andReturn().getResponse().getContentAsString();
            result.add(newUUID);
        }
        assertEquals(result.size(), 1);
    }

    @Test
    void testPut() throws Exception {
        Set<String> result = new HashSet<>();
        for (int i = 0; i < 3; i++) {
            String uuid = mockMvc.perform(post("/cache"))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn().getResponse().getContentAsString();
            result.add(uuid);
            String newUUID = mockMvc.perform(get("/cache"))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andExpect(content().string(uuid))
                    .andReturn().getResponse().getContentAsString();
            result.add(newUUID);
        }
        assertEquals(result.size(), 3);
    }

    @Test
    void testDelete() throws Exception {
        mockMvc.perform(delete("/cache"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().string("over"));
    }

    @Test
    public void test() {
        try (Cursor<String> cursor = RedisUtils.scan(redisTemplate, "*", 100)) {
            while (cursor.hasNext()) {
                TestLogUtils.info("key:{} cursorId:{} position:{}", cursor.next(), cursor.getCursorId(), cursor.getPosition());
            }
        }

        Set<String> keys = RedisUtils.scan(redisTemplate, "*", 10, 1);
        TestLogUtils.info("keys:{}", keys);
        assertEquals(1, keys.size());
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme
