package com.livk.caffeine;

import com.livk.commons.spring.SpringLauncher;
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
        SpringLauncher.run(CaffeineApp.class, args);
    }

}
