package com.livk.rsocket;

import com.livk.commons.spring.LivkSpring;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * RSocketServerApp
 * </p>
 *
 * @author livk
 */
@SpringBootApplication
public class RSocketServerApp {

    public static void main(String[] args) {
        LivkSpring.run(RSocketServerApp.class, args);
    }

}
