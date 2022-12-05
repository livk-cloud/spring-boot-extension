package com.livk.sso.resource;

import com.livk.commons.spring.LivkSpring;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * SSOResourceApp
 * </p>
 *
 * @author livk
 */
@SpringBootApplication
public class SSOResourceApp {

    public static void main(String[] args) {
        LivkSpring.run(SSOResourceApp.class, args);
    }

}
