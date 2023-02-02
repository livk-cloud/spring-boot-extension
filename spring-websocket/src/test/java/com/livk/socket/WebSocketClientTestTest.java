package com.livk.socket;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * <p>
 * WebSocketClientTestTest
 * </p>
 *
 * @author livk
 * @date 2023/2/2
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        useMainMethod = SpringBootTest.UseMainMethod.ALWAYS)
class WebSocketClientTestTest {

    @Test
    void testMain() {
        String uri = "ws://127.0.0.1:8888/websocket/";
        int threadNum = 1000;
        ExecutorService service = Executors.newFixedThreadPool(threadNum);
        CountDownLatch countDownLatch = new CountDownLatch(threadNum);
        for (int i = 0; i < threadNum; i++) {
            String url = uri + "No" + i;
            service.submit(new WebSocketClientTest(url, countDownLatch));
            countDownLatch.countDown();
        }
        assertEquals(0, countDownLatch.getCount());
    }
}

