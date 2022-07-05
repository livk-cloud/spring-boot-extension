package com.livk.caffeine;

import com.livk.spring.LivkSpring;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * CaffeineApp
 * </p>
 *
 * @author livk
 * @date 2022/3/30
 */
@SpringBootApplication
public class CaffeineApp {

    public static void main(String[] args) {
        LivkSpring.run(CaffeineApp.class, args);
    }

}
