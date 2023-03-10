package com.livk.browscap.mvc.example;

import com.livk.commons.spring.SpringLauncher;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * BrowscapWebApp
 * </p>
 *
 * @author livk
 */
@SpringBootApplication
public class BrowscapWebApp {
    public static void main(String[] args) {
        SpringLauncher.run(BrowscapWebApp.class, args);
    }
}
