/*
 * Copyright 2021 spring-boot-extension the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.livk.socket.session;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.http.HttpHeaders;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CountDownLatch;

/**
 * <p>
 * WebSocketClientTest
 * </p>
 *
 * @author livk
 */
@Slf4j
@RequiredArgsConstructor
public class WebSocketClientTest implements Runnable {

    private final String uri;

    private final CountDownLatch countDownLatch;

    @Override
    public void run() {
        getClient(uri);
        try {
            countDownLatch.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getClient(String uri) {
        try {
            WebSocketClient webSocketClient = new WebSocketClient(new URI(uri)) {

                @Override
                public void onOpen(final ServerHandshake serverHandshake) {
                    log.info(uri + "===建立连接===");
                }

                @Override
                public void onMessage(final String message) {
                    log.info(uri + "====收到来自服务端的消息===" + message);
                }

                @Override
                public void onClose(final int code, final String reason, final boolean remote) {
                    log.info("关闭连接:::" + "code = " + code + ":::reason = " + reason + ":::remote = " + remote);
                }

                @Override
                public void onError(final Exception ex) {
                    log.error("====出现错误====" + ex.getMessage());
                }
            };
            webSocketClient.addHeader(HttpHeaders.AUTHORIZATION, "livk123");
            webSocketClient.connect();
            while (webSocketClient.getReadyState().ordinal() == 0) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    log.warn("延迟操作出现问题，但并不影响功能");
                }
                log.info("连接中.......");
            }
            if (webSocketClient.getReadyState().ordinal() == 1) {
                return;
            }
            if (!(webSocketClient.getReadyState().ordinal() == 1 || webSocketClient.getReadyState().ordinal() == 0)) {
                log.info(uri + "连接失败.......");
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
