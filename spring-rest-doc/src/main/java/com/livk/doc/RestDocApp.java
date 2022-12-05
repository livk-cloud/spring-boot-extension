package com.livk.doc;

import com.livk.commons.spring.LivkSpring;
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
        LivkSpring.run(RestDocApp.class, args);
    }
}
