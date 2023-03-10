package com.livk.admin.client;

import com.livk.commons.spring.SpringLauncher;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * SpringClientApp
 * </p>
 *
 * @author livk
 */
@SpringBootApplication
public class SpringClientApp {
    public static void main(String[] args) {
        SpringLauncher.run(SpringClientApp.class, args);
    }
}
