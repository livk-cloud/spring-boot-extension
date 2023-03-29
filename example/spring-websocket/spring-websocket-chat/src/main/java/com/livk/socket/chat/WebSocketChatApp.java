package com.livk.socket.chat;

import com.livk.commons.spring.SpringLauncher;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author livk
 */
@SpringBootApplication
public class WebSocketChatApp {
    public static void main(String[] args) {
        SpringLauncher.run(WebSocketChatApp.class, args);
    }
}
