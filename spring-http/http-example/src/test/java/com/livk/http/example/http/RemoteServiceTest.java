package com.livk.http.example.http;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * <p>
 * RemoteServiceTest
 * </p>
 *
 * @author livk
 * @date 2022/8/22
 */
@SpringBootTest
class RemoteServiceTest {

    @Autowired
    RemoteService remoteService;

    @Test
    void getTest() {
        String result = remoteService.get();
        assertNotNull(result);
    }
}
