package com.livk.doc;

import com.livk.commons.spring.SpringLauncher;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * RestDocApp
 * </p>
 *
 * @author livk
 */
@SpringBootApplication
public class RestDocApp {
    public static void main(String[] args) {
        SpringLauncher.run(RestDocApp.class, args);
    }
}
