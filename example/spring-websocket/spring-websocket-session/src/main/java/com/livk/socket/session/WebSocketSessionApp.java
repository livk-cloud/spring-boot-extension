package com.livk.socket.session;

import com.livk.commons.spring.SpringLauncher;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * WebSocketApp
 * </p>
 *
 * @author livk
 */
@SpringBootApplication
public class WebSocketSessionApp {

    public static void main(String[] args) {
        SpringLauncher.run(WebSocketSessionApp.class, args);
    }

}
