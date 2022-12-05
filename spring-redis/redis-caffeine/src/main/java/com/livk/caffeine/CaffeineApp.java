package com.livk.caffeine;

import com.livk.commons.spring.LivkSpring;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * CaffeineApp
 * </p>
 *
 * @author livk
 */
@SpringBootApplication
public class CaffeineApp {

    public static void main(String[] args) {
        LivkSpring.run(CaffeineApp.class, args);
    }

}
