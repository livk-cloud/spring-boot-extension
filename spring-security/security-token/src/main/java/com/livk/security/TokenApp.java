package com.livk.security;

import com.livk.commons.spring.LivkSpring;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * ToeknApp
 * </p>
 *
 * @author livk
 */
@SpringBootApplication
public class TokenApp {

    public static void main(String[] args) {
        LivkSpring.run(TokenApp.class, args);
    }

}
