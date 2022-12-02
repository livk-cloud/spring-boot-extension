package com.livk.rsocket;

import com.livk.commons.spring.LivkSpring;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * RSocketServerApp
 * </p>
 *
 * @author livk
 * @date 2022/5/23
 */
@SpringBootApplication
public class RSocketClientApp {

    public static void main(String[] args) {
        LivkSpring.run(RSocketClientApp.class, args);
    }

}
