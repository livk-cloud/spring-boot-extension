package com.livk.rsocket;

import com.livk.commons.spring.SpringLauncher;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * RSocketServerApp
 * </p>
 *
 * @author livk
 */
@SpringBootApplication
public class RSocketClientApp {

    public static void main(String[] args) {
        SpringLauncher.run(RSocketClientApp.class, args);
    }

}
