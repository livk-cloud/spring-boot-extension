package com.livk.socket;

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
public class WebSocketApp {

    public static void main(String[] args) {
        SpringLauncher.run(WebSocketApp.class, args);
    }

}
