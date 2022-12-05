package com.livk.auth.resource;

import com.livk.commons.spring.LivkSpring;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * AuthResourceApp
 * </p>
 *
 * @author livk
 *
 */
@SpringBootApplication
public class AuthResourceApp {

    public static void main(String[] args) {
        LivkSpring.run(AuthResourceApp.class, args);
    }

}
