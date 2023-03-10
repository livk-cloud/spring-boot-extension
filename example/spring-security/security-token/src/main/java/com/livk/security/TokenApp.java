package com.livk.security;

import com.livk.commons.spring.SpringLauncher;
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
        SpringLauncher.run(TokenApp.class, args);
    }

}
